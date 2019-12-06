package com.gll.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gll.user.entity.OrderInfo;
import com.gll.user.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto-generator
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/user/order")
public class OrderInfoController {

    @Autowired
    private IOrderInfoService orderInfoService;
    @PostMapping("/add")
    public Object add(@RequestBody OrderInfo orderInfo){
        orderInfoService.save(orderInfo);
        return "ok";
    }

    @GetMapping("/queryByName")
    public Object queryByName(String name){
        return orderInfoService.list(new LambdaQueryWrapper<OrderInfo>().like(OrderInfo::getName,name));
    }

    @GetMapping("/queryByUserId")
    public Object queryByUserId(Integer userId){
        return orderInfoService.list(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getUserId,userId));
    }

}
