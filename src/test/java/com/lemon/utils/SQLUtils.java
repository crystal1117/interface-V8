package com.lemon.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import com.lemon.pojo.Member;

public class SQLUtils
{
    /***
     * 根据sql语句，查找一个随机member对象
     * @TODO 
     * @returnType: Object
     * @Author: shuailiuq
     * @DateTime: 2020年4月16日 下午3:29:52
     */
    public static Member getOneRandomMember() {
        String sql = "select * from member order by rand() LIMIT 1";
        //1 DBUtils操作SQL语句的核心类
        QueryRunner qr = new QueryRunner();
        //2 获取数据库连接
        Connection conn = JDBCUtils.getConnection();
        //2.1 定义返回值
        Member result = null;
        try
        {
            //3 执行SQL语句
            result = qr.query(conn, sql, new BeanHandler<Member>(Member.class));
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /***
     * 根据sql语句，执行查询单个结果
     * @TODO 
     * @returnType: Object
     * @Author: shuailiuq
     * @DateTime: 2020年4月16日 下午3:29:52
     */
    public static Object getSQLSingleResult(String sql) {
        if(StringUtils.isBlank(sql)) {
            return null;
        }
        //1 DBUtils操作SQL语句的核心类
        QueryRunner qr = new QueryRunner();
        //2 获取数据库连接
        Connection conn = JDBCUtils.getConnection();
        //2.1 定义返回值
        Object result = null;
        try
        {
            //3 执行SQL语句
            result = qr.query(conn, sql, new ScalarHandler<Object>());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
