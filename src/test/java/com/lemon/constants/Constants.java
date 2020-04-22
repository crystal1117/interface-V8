package com.lemon.constants;

public class Constants
{
    //final:修饰类：类不能被继承；修饰方法：方法不能被重写；修饰变量：就是常量
    //常量只能赋值一次：如果是基本类型：值不能变；引用类型：可以调用方法，但是地址值不能变、不能再次=来赋值
    //final使用时候一般搭配static：用类名来调用
    //常量：类似于配置文件，所有字母大写，单词之间用_分隔开
    public static final String EXCEL_PATH = "src\\test\\resources\\cases_v5.xlsx";
    //鉴权请求头
    public static final String MEDIA_TYPE = "lemonban.v2";
    //实际响应列号
    public static final int ACTUAL_RESPONSE_CELLNUM = 5;
    //响应断言列号
    public static final int RESPONSE_ASSERT_CELLNUM = 6;
    //数据库断言列号
    public static final int SQL_ASSERT_CELLNUM = 8;
    
    public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    public static final String JDBC_USER = "future";
    public static final String JDBC_PASSWORD = "123456";
    
    
    //参数化
    public static final String PARAM_REGISTER_MOBILEPHONE = "${register_mobilephone}";
    public static final String PARAM_REGISTER_PASSWORD = "${register_password}";
    public static final String PARAM_LOGIN_MOBILEPHONE = "${login_mobilephone}";
    public static final String PARAM_LOGIN_PASSWORD = "${login_password}";
    public static final String PARAM_TOKEN = "${token}";
    public static final String PARAM_MEMBER_ID = "${member_id}";
    
    
    
    
}
