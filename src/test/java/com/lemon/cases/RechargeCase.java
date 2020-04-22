package com.lemon.cases;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Null;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.utils.EnvironmentUtils;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.SQLUtils;


public class RechargeCase extends BaseCase
{
    public static void main(String[] args) {
//        String json = "{\"code\":0,\"msg\":\"OK\",\"data\":{\"id\":8029113,\"leave_amount\":0.0,\"mobile_phone\":\"13999880410\",\"reg_name\":\"小柠檬\",\"reg_time\":\"2020-04-10 21:49:47.0\",\"type\":1,\"token_info\":{\"token_type\":\"Bearer\",\"expires_in\":\"2020-04-10 21:55:04\",\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJfaWQiOjgwMjkxMTMsImV4cCI6MTU4NjUyNjkwNH0.myt4xyjaMifeEUInySQWoySlh8fVVawNhwimZT8CCYmk8LsttUXJLbnYfirHnpBuoPZqSIDuFEDAYHtOIE6O4A\"}},\"copyright\":\"Copyright 柠檬班 © 2017-2019 湖南省零檬信息技术有限公司 All Rights Reserved\"}";
//        //1 绝对定位找到token值
//        //Object read = JSONPath.read(json, "$.data.token_info.token");
//        //2 相对定位找到token：
//        Object read = JSONPath.read(json, "$..token");
//        System.out.println(read);
    }
    
    @Test (dataProvider="datas")
    public void test(API api, Case c) {
        System.out.println("========================register test开始执行================");
        //1参数化替换
        String params = paramsReplace(c.getParams());
        c.setParams(params);
        String sql = paramsReplace(c.getSql());
        c.setSql(sql);
        //2数据库前置查询结果（数据断言必须在接口执行前后都查询）
        Object beforeSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
        //3调用接口
        Map<String, String> headers = new HashMap<>();
        //3.1 设置默认请求头
        setDefaultHeaders(headers);
        //3.2 从环境变量中获取token
        getTokenToHeader(headers);
        String body = HttpUtils.call(api.getUrl(), api.getMethod(), c.getParams(), api.getContentType(),headers);
        //4 断言响应结果
        String responseAssert = responseAssert(c.getExpect(), body);
        //5 添加接口响应回写内容
        addWriteBackData(1, c.getId(), Constants.ACTUAL_RESPONSE_CELLNUM, body);
        //6 数据库后置查询结果
        Object afterSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
        System.out.println("beforeSQLResult" + beforeSQLResult);
        System.out.println("afterSQLResult" + afterSQLResult);
        //7 数据库断言
        if(StringUtils.isNotBlank(c.getSql())) {
            boolean sqlAssertFlag = sqlAssert(beforeSQLResult, afterSQLResult,c);
            System.out.println("数据库断言： " +sqlAssertFlag);
            addWriteBackData(1, c.getId(), Constants.SQL_ASSERT_CELLNUM, sqlAssertFlag?"Pass":"Fail");
        }
        //8 添加断言回写内容
        addWriteBackData(1, c.getId(), Constants.RESPONSE_ASSERT_CELLNUM, responseAssert);
        //9 添加日志
        //10 报表断言

    }
    
    /***
     * 注册：数据库查询断言
     * @TODO 
     * @returnType: boolean
     * @Author: shuailiuq
     * @DateTime: 2020年4月16日 下午4:14:51
     */
    public boolean sqlAssert(Object beforeSQLResult,Object afterSQLResult,Case c) {
        if(beforeSQLResult == null || afterSQLResult == null) {
            return false;
        }
        //1从参数中拿到本次要充的amount，String类型
        String params = c.getParams();
        String amount = JSONPath.read(params, "$.amount").toString();
        //2 把String amount转成BigDecimal类型，把object类型beforeSQLResult、afterSQLResult转BigDecimal
        BigDecimal amountValue = new BigDecimal(amount);
        BigDecimal beforeValue = (BigDecimal) beforeSQLResult;
        BigDecimal afterValue = (BigDecimal) afterSQLResult;
        //3 afterValue.subtract(beforeValue)得到实际充值结果subtractResult
        BigDecimal subtractResult = afterValue.subtract(beforeValue);
        //4 subtractResult和amountValue进行比较，如果是0，说明相等。
        if(subtractResult.compareTo(amountValue)==0) {
            return true;
        }else {
            return false;
        }
    }

    
    @DataProvider   
    public Object[][] datas(){
        System.out.println("========================recharge datas开始执行================");
         Object[][] datas = ExcelUtils.getAPIAndCaseByApiId("3");
         return datas;
    }
    
    @BeforeSuite
    public void init() {
        ExcelUtils.apiList = ExcelUtils.read(0, 1, API.class);
        ExcelUtils.caseList = ExcelUtils.read(1, 1, Case.class);
    }
}
