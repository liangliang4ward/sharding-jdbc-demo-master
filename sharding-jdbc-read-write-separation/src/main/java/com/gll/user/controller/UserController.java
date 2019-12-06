package com.gll.user.controller;


import com.gll.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
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
        return userService.list();
    }

    @PostMapping("/add")
    @Transactional
    public Object add(@RequestBody User user){
        //插入一条数据
        userService.save(user);
        //查询到的数据是写库的数据
        return userService.list();
    }

    @PostMapping("/addNoTransactional")
    public Object addNoTransactional(@RequestBody User user){
        //插入一条数据
        userService.save(user);
        //查询到的结果仍然是从库的数据
        return userService.list();
    }
}
