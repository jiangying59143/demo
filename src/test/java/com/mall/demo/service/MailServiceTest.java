package com.mall.demo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService MailService;

    @Test
    public void sendSimpleMail() {
        MailService.sendSimpleMail("907292671@qq.com","test simple mail"," hello this is simple mail");
    }
}