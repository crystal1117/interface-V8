package com.lemon.cases;


import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;

import ognl.Token;


public class LoginCase extends BaseCase
{
    public static void main(String[] args) {
        String json = "{\"code\":0,\"msg\":\"OK\",\"data\":{\"id\":8029113,\"leave_amount\":0.0,\"mobile_phone\":\"13999880410\",\"reg_name\":\"小柠檬\",\"reg_time\":\"2020-04-10 21:49:47.0\",\"type\":1,\"token_info\":{\"token_type\":\"Bearer\",\"expires_in\":\"2020-04-10 21:55:04\",\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJfaWQiOjgwMjkxMTMsImV4cCI6MTU4NjUyNjkwNH0.myt4xyjaMifeEUInySQWoySlh8fVVawNhwimZT8CCYmk8LsttUXJLbnYfirHnpBuoPZqSIDuFEDAYHtOIE6O4A\"}},\"copyright\":\"Copyright 柠檬班 © 2017-2019 湖南省零檬信息技术有限公司 All Rights Reserved\"}";
        //1 绝对定位找到token值
        //Object read = JSONPath.read(json, "$.data.token_info.token");
        //2 相对定位找到token：
        Object read = JSONPath.read(json, "$..token");
        System.out.println(read);
    }
    
    @Test (dataProvider="datas")
    public void test(API api, Case c) {
        System.out.println("========================login test开始执行================");
        //1参数化替换
        //{"mobile_phone":"${register_mobilephone}","pwd":"${register_password}"}
        //怎么建立mapping，键值对放在map里
        //使得"${register_mobilephone}"和"17792853901"关联起来
        //使得"${register_password}"和具体的一个密码"12345678"关联起来
        String params = paramsReplace(c.getParams());
        c.setParams(params);
        String sql = paramsReplace(c.getSql());
        c.setSql(sql);
        
        //2数据库前置查询结果（数据断言必须在接口执行前后都查询）
        //3调用接口
      //请求之前创建一个Map类型的headers 并存储默认值（"X-Lemonban-Media-Type"、"Content-Type"），headers提交到call方法中
        Map<String, String> headers = new HashMap<>();
        setDefaultHeaders(headers);
      //修改call/post/get/patch方法：返回值由void改成String，这样就能拿到响应体body
        String body = HttpUtils.call(api.getUrl(), api.getMethod(), c.getParams(), api.getContentType(),headers);
        //3.1 从body里取出token/memberId,存储到环境变量中
        //参数化之后更新成为：
        setEnvVariable(body,"$.data.token_info.token",Constants.PARAM_TOKEN);
        setEnvVariable(body,"$.data.id",Constants.PARAM_MEMBER_ID);
        //参数化之前操作
//        setEnvVariable(body,"$.data.token_info.token","${token}");
//        setEnvVariable(body,"$.data.id","${member_id}");
        //4 断言响应结果
        String responseAssert = responseAssert(c.getExpect(), body);
        //5 添加接口响应回写内容
        addWriteBackData(1, c.getId(), Constants.ACTUAL_RESPONSE_CELLNUM, body);
        //6 数据库后置查询结果
        //7 数据库断言
        //8 添加断言回写内容
        addWriteBackData(1, c.getId(), Constants.RESPONSE_ASSERT_CELLNUM, responseAssert);
        //9 添加日志
        //10 报表断言

    }

    

    @DataProvider   
    public Object[][] datas(){
        System.out.println("========================login datas开始执行================");
         Object[][] datas = ExcelUtils.getAPIAndCaseByApiId("2");
         return datas;
    }
}
