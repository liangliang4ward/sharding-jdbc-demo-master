#### 数据准备：

> 1. 创建两个数据库sharding-write,sharding-read，分别作为写库和读库。
> 2. 创建表语句
>
> ```sql
> drop table if exists user;
> CREATE TABLE `user` (
>   `id` bigint(20) DEFAULT NULL,
>   `username` varchar(100) DEFAULT NULL,
>   `email` varchar(200) DEFAULT NULL,
>   `age` int(20) DEFAULT NULL,
>   `town_code` varchar(20) DEFAULT NULL
> ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
> ```
>
> 3. 两个库分别创建`user`表

#### 配置

```properties
#设置数据源名称
spring.shardingsphere.datasource.names=ds0,ds0slave

#配置写库信息
spring.shardingsphere.datasource.ds0.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.ds0.driver-class-name=com.mysql.jdbc.Driver
spring.shardingsphere.datasource.ds0.url=jdbc:mysql://localhost:3306/sharding_write?characterEncoding=utf-8
spring.shardingsphere.datasource.ds0.username=root
spring.shardingsphere.datasource.ds0.password=123456

#配置读库信息
spring.shardingsphere.datasource.ds0slave.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.ds0slave.driver-class-name=com.mysql.jdbc.Driver
spring.shardingsphere.datasource.ds0slave.url=jdbc:mysql://localhost:3306/sharding_read?characterEncoding=utf-8
spring.shardingsphere.datasource.ds0slave.username=root
spring.shardingsphere.datasource.ds0slave.password=123456

#设置主从规则
spring.shardingsphere.sharding.master-slave-rules.ds0.master-data-source-name=ds0
spring.shardingsphere.sharding.master-slave-rules.ds0.slave-data-source-names=ds0slave

#配置默认表字段生成规则
spring.shardingsphere.sharding.default-key-generator.type=SNOWFLAKE
spring.shardingsphere.sharding.default-key-generator.column=id
```

- 代码片段1：

```java
@RequestMapping("/list")
public Object list(){
	//查询的是从库
	return userService.list();
}
```

- 代码片段2：

```java
@PostMapping("/add")
@Transactional
public Object add(@RequestBody User user){
	//插入一条数据
	userService.save(user);
	//查询到的数据是写库的数据
	return userService.list();
}
```

- 代码片段3

```java
@PostMapping("/addNoTransactional")
public Object addNoTransactional(@RequestBody User user){
	//插入一条数据
	userService.save(user);
	//查询到的结果仍然是从库的数据
	return userService.list();
}
```

#### 结果

从`代码片段1`测试结果来看，查询的数据是从库的数据。

从`代码片段2`和`代码片段3`的测试结果来看：

如果当前操作有事务，则当前事务查询的数据源都是一个。

如果当前操作没有事务，那么操作是分离的，即写是操作的是写库，读是操作的是读库。

> 代码地址：https://github.com/qq54903099/sharding-jdbc-demo-master/tree/master/sharding-jdbc-read-write-separation