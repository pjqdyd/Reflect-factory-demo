package com.pjqdyd.service.impl;

import com.pjqdyd.service.PartyService;
import org.springframework.stereotype.Service;

/**
 * 班级派对
 */
@Service("classPartyService")
public class ClassPartyServiceImpl implements PartyService {
    @Override
    public String sing() {
        System.out.println("在班里唱");
        return "在班里唱";
    }
    @Override
    public String dance() {
        System.out.println("在班里跳");
        return "在班里跳";
    }
    @Override
    public String rap() {
        System.out.println("在班里rap");
        return "在班里rap";
    }
    @Override
    public String basketball() {
        System.out.println("在班里打篮球");
        return "在班里打篮球";
    }
}
