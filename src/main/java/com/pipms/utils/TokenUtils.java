package com.pipms.utils;

import java.util.Random;

/**
 * @ClassName TokenUtils
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/2115:46
 * @Version 1.0
 **/
public class TokenUtils {
    public static String generateToken(int num,String base){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i <num ; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
