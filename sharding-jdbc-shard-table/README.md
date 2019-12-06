### 同库分表

> 在一个库里，对一个表进行拆分。
>
> user表，分表创建4个分表,user_0,user_1,user_2,user_3。
>
> 分表策略采用id%4的方式，找到对应的数据表。

#### 配置

```properties
spring.shardingsphere.datasource.names=master

spring.shardingsphere.datasource.master.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.master.driver-class-name=com.mysql.jdbc.Driver
spring.shardingsphere.datasource.master.url=jdbc:mysql://localhost:3306/sharding_table?characterEncoding=utf-8
spring.shardingsphere.datasource.master.username=root
spring.shardingsphere.datasource.master.password=123456


#实际的表节点
spring.shardingsphere.sharding.tables.user.actual-data-nodes=master.user_${0..3}

# 根据字段进行拆分
spring.shardingsphere.sharding.tables.user.table-strategy.inline.sharding-column=id
# 拆分规则
spring.shardingsphere.sharding.tables.user.table-strategy.inline.algorithm-expression=user_${id.longValue() % 4}

# 显示SQL
spring.shardingsphere.props.sql.show=true
```

#### 操作

##### 列表查询

```java
    @RequestMapping("/list")
    public Object list(){
        //查询的是从库
        return userService.list();
    }
```

当我们执行完这个请求之后，可以看下日志输出：

```sql
Rule Type: sharding
Logic SQL: SELECT  id,username,email,age,town_code  FROM user
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_0
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_1
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_2
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_3
```

可以看到查询一次列表的数据，会同时查询所有的数据，sharding-sephere会将返回结果统一汇聚。如果有排序的话，会将返回结果再次根据结果进行排序。

##### 分页查询

```java
    @RequestMapping("/page")
    public Object page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer size){
        return userService.page(new Page<>(page,size));
    }
```

这个操作为分页查询数据，我们执行以下，看下输出语句。

```sql
Rule Type: sharding
Logic SQL: SELECT COUNT(1) FROM user
Actual SQL: master ::: SELECT COUNT(1) FROM user_0
Actual SQL: master ::: SELECT COUNT(1) FROM user_1
Actual SQL: master ::: SELECT COUNT(1) FROM user_2
Actual SQL: master ::: SELECT COUNT(1) FROM user_3
Rule Type: sharding
Logic SQL: SELECT  id,username,email,age,town_code  FROM user LIMIT ?,?
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_0 LIMIT ?,? ::: [0, 20]
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_1 LIMIT ?,? ::: [0, 20]
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_2 LIMIT ?,? ::: [0, 20]
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_3 LIMIT ?,? ::: [0, 20]
```

从上面日志输出我们可以看到，查询的方式与list类似，默认查询第一页的数据，从每个分表里面分别查询20条数据，然后再将结果返回20条数据。

> 试着想一下：如果查询第二页的数据，应该要怎么做呢？
>
> 如果是单表的情况下，我们只需要limit 20,20就好了。
>
> 可是如果是在分表的情况下会出现什么问题呢？
>
> 举个例子：
>
> user_0 ,user_1,user_2,user_3三个表中：user_1里面有40条数据，user_0和user_2,user_3里面只有20条数据。
>
> 如果我们查询第二页的数据，select * from user_[0,2,3] limit 20,20，数据会为空，只能查询到user_1里的数据。如果顺序正常的情况下可能会没有问题，可是如果乱序的话，不就有问题了吗？
>
> 我们可以接着看下，这种情况是如何处理的

执行方法，传递参数为，page=2,size=20，看下日志：

```sql
Rule Type: sharding
Logic SQL: SELECT  id,username,email,age,town_code  FROM user LIMIT ?,?
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_0 LIMIT ?,? ::: [0, 40]
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_1 LIMIT ?,? ::: [0, 40]
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_2 LIMIT ?,? ::: [0, 40]
Actual SQL: master ::: SELECT  id,username,email,age,town_code  FROM user_3 LIMIT ?,? ::: [0, 40]
```

可以看到，查询第二页的数据，查询条件会是0,40，然后再将这些数据合并返回。

