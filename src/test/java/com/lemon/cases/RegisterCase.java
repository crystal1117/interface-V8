package com.lemon.cases;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.SQLUtils;

public class RegisterCase extends BaseCase
{

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
        setDefaultHeaders(headers);
        String body = HttpUtils.call(api.getUrl(), api.getMethod(), c.getParams(), api.getContentType(),headers);
        //4 断言响应结果
        String responseAssert = responseAssert(c.getExpect(), body);
        //5 添加接口响应回写内容
        addWriteBackData(1, c.getId(), Constants.ACTUAL_RESPONSE_CELLNUM, body);
        //6 数据库后置查询结果
        Object afterSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
        //7 数据库断言，拿手机号验证：没注册前这个手机号数量是0，注册后数量是1
        if(StringUtils.isNotBlank(c.getSql())) {
            boolean sqlAssertFlag = sqlAssert(beforeSQLResult, afterSQLResult);
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
    public boolean sqlAssert(Object beforeSQLResult,Object afterSQLResult) {
        long beforeValue = (long) beforeSQLResult;
        long afterValue = (long) afterSQLResult;
        if(beforeValue == 0 && afterValue == 1) {
            return true;
        }
        else {
            return false;
        }
    }
    
    @DataProvider   
    public Object[][] datas(){
        System.out.println("========================login datas开始执行================");
         Object[][] datas = ExcelUtils.getAPIAndCaseByApiId("1");
         return datas;
    }
}
