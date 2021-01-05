package com.pjqdyd.controller;

import com.pjqdyd.factory.PartyFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 派对Controller
 */
@RestController
@RequestMapping("/party")
public class PartyController {
    @Resource
    private PartyFactory partyFactory;

    /**
     * @param serviceName 表示才艺地点: 学校Service或班级Service
     * @param methodName  表示才艺方法: 唱、跳、rap、篮球
     */
    @GetMapping("/{serviceName}/{methodName}")
    public String party(
            @PathVariable( value = "serviceName") String serviceName,
            @PathVariable( value = "methodName") String methodName) {
        try {
            // 获取工厂中的对象和方法，并执行对象的方法
           return partyFactory.getServiceMethod(serviceName, methodName).invoke(partyFactory.getService(serviceName), null).toString();
        } catch (Exception e) {
            return "未找到方法，派对活动不存在!";
        }
    }
}
