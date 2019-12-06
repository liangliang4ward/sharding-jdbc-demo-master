package com.gll.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gll.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.gll.user.service.IUserService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto-generator
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/list")
    public Object list(){
        //查询的是从库
        return userService.list(new LambdaQueryWrapper<User>().orderByAsc(User::getId));
    }
    @RequestMapping("/page")
    public Object page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer size){
        return userService.page(new Page<>(page,size));
    }

    @PostMapping("/add")
    @Transactional
    public Object add(@RequestBody User user){
        //插入一条数据
        userService.save(user);
        return "ok";
    }
}
