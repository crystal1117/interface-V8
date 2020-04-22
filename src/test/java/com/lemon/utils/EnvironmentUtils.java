package com.lemon.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections4.functors.SwitchClosure;

public class EnvironmentUtils
{
    //弄一个map集合来模拟环境变量
    public static Map<String, String> env = new HashMap<>();
    
    
    public static String getRegisterPhone() {
        Integer headRandom = new Random().nextInt(5);
        String mobile = getHeadMobile(headRandom) + getEndMobile();
        return mobile;
    }
    
    public static String getHeadMobile(Integer type) {
        switch(type)
        {
            case 1 :
                return "131";
            case 2 :
                return "132";
            case 3 :
                return "133";
            case 4 :
                return "134";
            case 5 :
                return "135";
            default:
                return "177";
        }
    }
    
    public static String getEndMobile() {
        String ychar = "0,1,2,3,4,5,6,7,8,9";
        int wei = 8;
        String[] ychars = ychar.split(",");
        String endMobile = "";
        Random rdm = new Random();
        for(int i=0;i<wei;i++) {
            int j = (rdm.nextInt()>>>1) % 10;
            if(j>10) {
                j = 0;
            }
            endMobile = endMobile + ychars[j];   
        }
        return endMobile;
    }
}

