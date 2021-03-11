package com.xxxx.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/*
md5工具类
 */
@Component
public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3d4c";

    public static String inputPassToFromPass(String inputPass){
        String str = salt.charAt(0) + salt.charAt(2) + inputPass+ salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String fromPass, String salt){
        String str = salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    public static String inputPassToDBPass(String inputPass, String salt){
        //传入的盐是存数据库的盐
        String fromPass = inputPassToFromPass(inputPass);
        return formPassToDBPass(fromPass, salt);
    }

    public static void main(String[] args){
        //7c1242bc0fca66cf32bdac77ad07fd3d
        System.out.println(inputPassToFromPass("123456"));
        //5f4a4c2fdc685e36eedd07afefe88579
        System.out.println(formPassToDBPass("7c1242bc0fca66cf32bdac77ad07fd3d", "1a2b3c4d"));
        //5f4a4c2fdc685e36eedd07afefe88579
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));
    }
}
