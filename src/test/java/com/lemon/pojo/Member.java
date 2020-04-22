package com.lemon.pojo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Member
{
    //要跟DB字段完全一样
    private int id;
    private String reg_name;
    private String pwd;
    private String mobile_phone;
    private int type;
    private BigDecimal leave_amount;
    private Timestamp reg_time;

    //同样需要有空参构造、有参构造、getter/setter
    public Member()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    public Member(int id, String reg_name, String pwd, String mobile_phone, int type, BigDecimal leave_amount,
            Timestamp reg_time)
    {
        super();
        this.id = id;
        this.reg_name = reg_name;
        this.pwd = pwd;
        this.mobile_phone = mobile_phone;
        this.type = type;
        this.leave_amount = leave_amount;
        this.reg_time = reg_time;
    }
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getReg_name()
    {
        return reg_name;
    }
    public void setReg_name(String reg_name)
    {
        this.reg_name = reg_name;
    }
    public String getPwd()
    {
        return pwd;
    }
    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }
    public String getMobile_phone()
    {
        return mobile_phone;
    }
    public void setMobile_phone(String mobile_phone)
    {
        this.mobile_phone = mobile_phone;
    }
    public int getType()
    {
        return type;
    }
    public void setType(int type)
    {
        this.type = type;
    }
    public BigDecimal getLeave_amount()
    {
        return leave_amount;
    }
    public void setLeave_amount(BigDecimal leave_amount)
    {
        this.leave_amount = leave_amount;
    }
    public Timestamp getReg_time()
    {
        return reg_time;
    }
    public void setReg_time(Timestamp reg_time)
    {
        this.reg_time = reg_time;
    }
    @Override
    public String toString()
    {
        return "Member [id=" + id + ", reg_name=" + reg_name + ", pwd=" + pwd + ", mobile_phone=" + mobile_phone
                + ", type=" + type + ", leave_amount=" + leave_amount + ", reg_time=" + reg_time + "]";
    }

    
}
