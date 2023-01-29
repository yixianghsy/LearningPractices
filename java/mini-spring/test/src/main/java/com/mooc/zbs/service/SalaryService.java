package com.mooc.zbs.service;

import com.mooc.zbs.beans.Bean;

/**
 * @author zbs
 */
@Bean
public class SalaryService {
    public Integer calSalary(Integer experience) {
        return experience * 5000;
    }
}
