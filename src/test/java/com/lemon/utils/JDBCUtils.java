package com.lemon.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.lemon.constants.Constants;

public class JDBCUtils
{
    public static Connection getConnection() {
        //现在项目里主要用连接池，我们这种都是作为教学用
        //定义数据库连接及连接对象
        Connection conn = null;
        try {
            //你导入的数据库驱动包，如MySQL
            conn = DriverManager.getConnection(Constants.JDBC_URL, Constants.JDBC_USER, 
                    Constants.JDBC_PASSWORD);
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
    
    public static void close(Connection conn) {
        if(conn !=null) {
            try{
                conn.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
