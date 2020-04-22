package com.lemon.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.lemon.cases.BaseCase;

public class HttpUtils
{
    private static Logger log = Logger.getLogger(HttpUtils.class);
//    public static void main(String[] args) throws Exception {
////        get("http://api.lemonban.com/futureloan/member/1/info");
////        get("http://api.lemonban.com/futureloan/member/2/info");
//        
////        post("http://api.lemonban.com/futureloan/member/login","{\"mobile_phone\": \"18900000000\",\"pwd\": \"12345678\"}");
////        post("http://api.lemonban.com/futureloan/member/login","{\"mobile_phone\": \"18900000000\",\"pwd\": \"12345679\"}");
//          
////        patch("http://api.lemonban.com/futureloan/member/login","{\"mobile_phone\": \"18900000000\",\"pwd\": \"12345678\"}");
//          formPost("http://test.lemonban.com/futureloan/mvc/api/member/login", "mobilephone=13212312333&pwd=123456");
//          
//    }
    //接口调用方法
    public static String call(String url,String method,String params, String contentType, Map<String, String> headers) {
        String body = null;
        try
        {
            if ("post".equalsIgnoreCase(method)) {
                if("form".equalsIgnoreCase(contentType)) {
                    params = json2KeyValue(params);
                    body = HttpUtils.formPost(url, params,headers);
                }else if("json".equalsIgnoreCase(contentType)) {
                    body = HttpUtils.post(url, params,headers);
                }
            } else if("get".equalsIgnoreCase(method)) {
                body = HttpUtils.get(url,headers);
            } else if("patch".equalsIgnoreCase(method)) {
                body = HttpUtils.patch(url, params,headers);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return body;
    }
    
    private static void setHeaders(HttpRequest request, Map<String, String> headers) {
        //1 获取所有请求头里的key
        Set<String> keySet = headers.keySet();
        //2 遍历所有键
        for(String key : keySet)
        {
            //3 把对应的键和值设置到request的header中
            request.setHeader(key, headers.get(key));
        }
    }
    
    
    /***
     * 
     * @TODO json字符串转成key=value字符串
     * @returnType: String
     * @Author: shuailiuq
     * @DateTime: 2020年3月31日 下午6:12:21
     */
    private static String json2KeyValue(String jsonStr)
    {
        //需求：json数据：{"mobilephone":"13877788811","pwd":"12345678"}转换成：
        //form格式：mobilephone=13877788811&pwd=12345678
        //方法1：字符串替换：String的replace方法，替换掉格式
        //params = params.replace("{", "").replace("}","").replace("\"","").replace(":", "=").replace(",", "&");
        //方法2：字符串json转map对象，再用map拼装成一个字符串String
        //1 导fastjson dependency到pom.xml
               Map<String,String> map = JSON.parseObject(jsonStr, Map.class);
               //String string = map.toString();
               //System.out.println("String is: ----" + string);
               Set<String> keySet = map.keySet();
               String result = "";
               for(String key : keySet)
               {
                   String value = map.get(key);
                   result += key + "=" + value + "&";
               }
               jsonStr = result.substring(0, result.length()-1);
               return jsonStr;
    }
    
    /***
     * 
     * @TODO         发送一个get请求
     * params        url: 接口的请求地址+请求参数
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年3月30日 下午11:25:16
     */
    public static String get(String url, Map<String, String> headers) throws Exception{
        //1 New request 创建请求
        //2 Method 请求方式
        //3 url：填写url
        HttpGet get = new HttpGet(url);
        //4 body/header：如果有header和参数，进行添加
        //get.setHeader("X-lemonban-Media-Type", "lemonban.v1");
        setHeaders(get, headers);
        //5 创建HTTP client客户端，send
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(get);
        //6 response body:格式化响应体
        return printResponseAndReturnBody(response);
    }
    /***
     * 
     * @TODO 发一个post请求
     * @url         接口请求地址
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年3月30日 下午11:35:37
     */
    public static String post(String url,String json, Map<String, String> headers) throws Exception {
        //String url = "http://api.lemonban.com/futureloan/member/login";
        HttpPost post = new HttpPost(url);
//        post.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//        post.setHeader("Content-Type", "application/json");
        setHeaders(post, headers);
        //String json = "{\"mobile_phone\": \"18900000000\",\"pwd\": \"12345678\"}";
        StringEntity stringEntity = new StringEntity(json);
        post.setEntity(stringEntity);
        //send发送
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(post);
        return printResponseAndReturnBody(response);
    }
    
    /***
     * 
     * @TODO 发一个form post请求
     * @param  form        form格式的请求参数 
     * @param  url         接口请求地址
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年3月30日 下午11:35:37
     */
    public static String formPost(String url,String form, Map<String, String> headers) throws Exception {
        HttpPost post = new HttpPost(url);
//        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        setHeaders(post, headers);
        StringEntity stringEntity = new StringEntity(form);
        post.setEntity(stringEntity);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(post);
        return printResponseAndReturnBody(response);
    }
    
    /***
     * 
     * @TODO 发一个patch请求
     * @url         接口请求地址
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年3月30日 下午11:35:37
     */
    public static String patch(String url,String json, Map<String, String> headers) throws Exception {
        HttpPatch patch = new HttpPatch(url);
        //添加头
//        patch.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//        patch.setHeader("Content-Type", "application/json");
        setHeaders(patch, headers);
        //添加body
        StringEntity stringEntity = new StringEntity(json);
        patch.setEntity(stringEntity);
        //send发送
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(patch);
        return printResponseAndReturnBody(response);
    }
    
/***
 * 打印响应内容，并返回响应体
 * @Param     response   接口响应对象 
 * @returnType: String   接口响应体
 * @DateTime: 2020年3月30日 下午11:19:05
 */
    private static String printResponseAndReturnBody(HttpResponse response) throws IOException
    {
        Header[] allHeaders = response.getAllHeaders();
        log.info("响应头： " + Arrays.toString(allHeaders));
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        log.info("响应体： " + body);
        int statusCode = response.getStatusLine().getStatusCode();
        log.info("状态码： " + statusCode);
        return body;
    }
    
}
