package com.pjqdyd.service.impl;

import com.pjqdyd.service.PartyService;
import org.springframework.stereotype.Service;

/**
 * 学校派对
 */
@Service("schoolPartyService")
public class SchoolPartyServiceImpl implements PartyService {
    @Override
    public String sing() {
        System.out.println("在学校唱");
        return "在学校唱";
    }
    @Override
    public String dance() {
        System.out.println("在学校跳");
        return "在学校跳";
    }
    @Override
    public String rap() {
        System.out.println("在学校rap");
        return "在学校rap";
    }
    @Override
    public String basketball() {
        System.out.println("在学校打篮球");
        return "在学校打篮球";
    }
}
