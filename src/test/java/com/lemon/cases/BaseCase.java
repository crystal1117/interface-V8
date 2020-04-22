package com.lemon.cases;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.pojo.Member;
import com.lemon.utils.EnvironmentUtils;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.SQLUtils;
import com.lemon.utils.WriteBackData;

public class BaseCase
{
    private Logger log = Logger.getLogger(BaseCase.class);
    @BeforeSuite
    public void init() {
        log.info("===============项目初始化====================");
        ExcelUtils.apiList = ExcelUtils.read(0, 1, API.class);
        ExcelUtils.caseList = ExcelUtils.read(1, 1, Case.class);
        log.info("===============参数初始化====================");
        //项目初始化时候，要把初始值先准备好
        //注册时候，调用方法随机生成手机号来注册
        EnvironmentUtils.env.put(Constants.PARAM_REGISTER_MOBILEPHONE, EnvironmentUtils.getRegisterPhone());
        EnvironmentUtils.env.put(Constants.PARAM_REGISTER_PASSWORD, "12345678");
        //登录时候：DB里随便找一个member信息
        Member member = SQLUtils.getOneRandomMember();
        EnvironmentUtils.env.put(Constants.PARAM_LOGIN_MOBILEPHONE,member.getMobile_phone());
        //要找开发，看密码能不能解密、如何解密，直接从数据库取得密码都是加密的，登录不了；或者所有密码改成一个已知密码
        EnvironmentUtils.env.put(Constants.PARAM_LOGIN_PASSWORD, member.getPwd());
    }

    /***
     * 参数化替换方法
     * @param params  传入：需要替换的 字符串
     * @return        返回：替换后的字符串
     * @Author: shuailiuq
     * @DateTime: 2020年4月17日 下午3:23:49
     */
    public String paramsReplace(String params)
    {
        if(StringUtils.isBlank(params)) {
            return null;
        }
      //1 从环境变量中获取所有的占位符
        Set<String> keySet = EnvironmentUtils.env.keySet();
        //2 遍历环境变量env
        //key是参数化的占位符，value就是参数化具体要替换的值
        for(String key : keySet)
        {
            String value = EnvironmentUtils.env.get(key);
            //3 把需要执行的字符串执行replace方法，且重新接收params
            params = params.replace(key, value);
        }
        return params;
    }
    
    /***
     * 设置默认请求头
     * @TODO 
     * 
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年4月11日 下午3:38:29
     */
    public void setDefaultHeaders(Map<String, String> headers)
    {
        headers.put("X-Lemonban-Media-Type",Constants.MEDIA_TYPE);
        headers.put("Content-Type", "application/json");
    }
    
    /***
     * 从body中取到jsonPath对应的值，存储到环境变量envKey中
     * @TODO 
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年4月11日 下午7:19:10
     */
        public void setEnvVariable(String body,String jsonPath, String envKey)
        {
            Object value = JSONPath.read(body, jsonPath);
            if (value != null) {
                EnvironmentUtils.env.put(envKey, value.toString());
            }
        }
        
    /***
     * 从环境变量中获取token，设置到header中
     * @TODO 
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年4月12日 上午7:19:38
     */
    public void getTokenToHeader(Map<String, String> headers)
    {
        String token = EnvironmentUtils.env.get("${token}");
        if (StringUtils.isNotBlank(token)) {
            headers.put("Authorization", "Bearer " + token);
        }
    }
    
    /***
     * 做响应断言，返回断言成功/body不包含以下期望值。。。
     * @params    expect   
     * @returnType: boolean
     * @Author: shuailiuq
     * @DateTime: 2020年4月15日 下午10:57:26
     */
    public String responseAssert(String expect, String body)
    {
        //1 根据特殊分隔符@@，来切割期望值  "code":0@@"msg":"OK"
        String[] expectArray = expect.split("@@");
        //2 定义返回值
        String responseAssertResult = "断言成功";
        //3 循环期望值切割之后的数组
        for(String expectValue : expectArray)
        {
            //4 如果响应体包含所有的期望值，则认为当前断言成功
            boolean flag = body.contains(expectValue);
            //5 如果不包含期望值，则认为断言失败
            if (flag == false) {
                responseAssertResult = "body不包含以下期望值：" + expectValue;
                break;
            }
        }
        System.out.println(responseAssertResult);
        return responseAssertResult;
    }
    
    /***
     * @TODO 添加wbd回写对象到wbdList集合中
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年4月13日 下午10:55:07
     */
    public void addWriteBackData(int sheetIndex,int rowNum, int cellNum, String content)
    {
        WriteBackData wbd = new WriteBackData(sheetIndex, rowNum, cellNum, content);
        ExcelUtils.wbdList.add(wbd);
    }
    @AfterSuite
    public void finish() {
        ExcelUtils.batchWrite();
        log.info("===============项目结束====================");
    }
}
