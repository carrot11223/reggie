package com.atheima.reggie.Test;


import org.springframework.util.DigestUtils;

public class Test {
    public static void main(String[] args) {
        byte[] bytes = "123456789".getBytes();
        String s = DigestUtils.md5DigestAsHex(bytes);
        System.out.println(s);
    }
}
