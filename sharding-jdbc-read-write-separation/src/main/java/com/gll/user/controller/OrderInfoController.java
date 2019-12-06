package com.gll.user.controller;


import com.gll.user.entity.OrderInfo;
import com.gll.user.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto-generator
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/user/order-info")
public class OrderInfoController {

    @Autowired
    private IOrderInfoService orderInfoService;
    @RequestMapping("/list")
    public Object list(){
        List<OrderInfo> result = orderInfoService.list();
        return result;
    }
}
