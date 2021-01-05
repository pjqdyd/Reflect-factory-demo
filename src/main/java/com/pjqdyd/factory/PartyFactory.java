package com.pjqdyd.factory;

import com.pjqdyd.service.PartyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 派对工厂
 */
@Component
public class PartyFactory implements CommandLineRunner {
    @Resource
    private ApplicationContext applicationContext;

    private static Map<String, PartyService> partyServiceMap = new HashMap<>(); // 存放所有partyService
    private static Map<String, Method> partyMethodMap = new HashMap<>();        // 存放所有partyService的方法

    public PartyService getService(String serviceName){ // 提供Service对象
        return partyServiceMap.get(serviceName);
    }

    public Method getServiceMethod(String serviceName, String methodName){ // 提供Service方法
        return partyMethodMap.get(serviceName + ":" + methodName);
    }

    /**
     * 初始方法，启动执行
     */
    @Override
    public void run(String... args) throws Exception {
        partyServiceMap = applicationContext.getBeansOfType(PartyService.class); // 获取所有实现PartyService接口的实现类
        partyServiceMap.forEach((key, value) -> {
            Arrays.stream(value.getClass().getDeclaredMethods()).forEach(method -> { // 反射获取所有的方法
                partyMethodMap.put(key + ":" + method.getName(), method); // 将所有的方法push到map中
            });
        });
        partyMethodMap.keySet().forEach(System.out::println); // 打印所有的方法
    }
}
