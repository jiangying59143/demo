package com.mall.demo.common.utils;

import org.springframework.cache.annotation.Cacheable;

import java.util.Random;

public class StringUtils {

    public static boolean isEmpty(String value) {
        if (null == value)
            return true;
        return value.isEmpty();
    }

    public static String getRandomNumCode(int number){
        String codeNum = "";
        int [] numbers = {0,1,2,3,4,5,6,7,8,9};
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int next = random.nextInt(10000);//目的是产生足够随机的数，避免产生的数字重复率高的问题
//    			System.out.println(next);
            codeNum+=numbers[next%10];
        }
        System.out.println("--------");
        System.out.println(codeNum);
        return codeNum;
    }

    public static void main(String[] args) {
        getRandomNumCode(4);
    }
}
