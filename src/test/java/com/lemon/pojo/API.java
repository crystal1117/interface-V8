package com.lemon.pojo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class API
{
    //接口编号           
    @Excel(name = "接口编号") //@Excel: excel列和实体类的成员变量间的映射关系
    @NotNull
    private String id;
    //接口名称 
    @Excel(name = "接口名称")
    private String name;
    //接口提交方式
    @Excel(name = "接口提交方式")
    private String method;
    //接口地址
    @Excel(name = "接口地址")
    @URL(protocol="http",host="api.lemonban.com")
    private String url;
    //参数类型
    @Excel(name = "参数类型")
    private String contentType;
    
    public API() {
    }
    public API(String id, String name, String method, String url,String contentType) {
        this.id = id;
        this.name = name;
        this.method = method;
        this.url = url;
        this.contentType = contentType;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return this.method;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getContentType() {
        return this.contentType;
    }
    @Override
    public String toString()
    {
        return "API [id=" + id + ", name=" + name + ", method=" + method + ", url=" + url + ", contentType="
                + contentType + "]";
    }
}
