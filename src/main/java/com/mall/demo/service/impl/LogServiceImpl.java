package com.mall.demo.service.impl;

import com.mall.demo.model.sys.Log;
import com.mall.demo.repository.LogRepository;
import com.mall.demo.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public Integer saveLog(Log log) {
        return logRepository.save(log).getId();
    }
}
