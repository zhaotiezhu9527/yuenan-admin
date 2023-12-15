package com.juhai.business.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.juhai.common.annotation.Excel;
import com.juhai.common.core.domain.BaseEntity;

/**
 * 收集用户列表对象 t_information
 * 
 * @author zhaotiezhu
 * @date 2023-12-15
 */
public class Information extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String userPhone;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long userAge;

    /** 月薪 */
    @Excel(name = "月薪")
    private String userSalary;

    /** 工作 */
    @Excel(name = "工作")
    private String userJob;

    /** 车房 */
    @Excel(name = "车房")
    private String userCar;

    /** 婚姻情况 */
    @Excel(name = "婚姻情况")
    private String userMarriage;

    /** FB帐号 */
    @Excel(name = "FB帐号")
    private String userFb;

    /** 修改时间 */
    private Date modify;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setUserPhone(String userPhone) 
    {
        this.userPhone = userPhone;
    }

    public String getUserPhone() 
    {
        return userPhone;
    }
    public void setUserAge(Long userAge) 
    {
        this.userAge = userAge;
    }

    public Long getUserAge() 
    {
        return userAge;
    }
    public void setUserSalary(String userSalary) 
    {
        this.userSalary = userSalary;
    }

    public String getUserSalary() 
    {
        return userSalary;
    }
    public void setUserJob(String userJob) 
    {
        this.userJob = userJob;
    }

    public String getUserJob() 
    {
        return userJob;
    }
    public void setUserCar(String userCar) 
    {
        this.userCar = userCar;
    }

    public String getUserCar() 
    {
        return userCar;
    }
    public void setUserMarriage(String userMarriage) 
    {
        this.userMarriage = userMarriage;
    }

    public String getUserMarriage() 
    {
        return userMarriage;
    }
    public void setUserFb(String userFb) 
    {
        this.userFb = userFb;
    }

    public String getUserFb() 
    {
        return userFb;
    }
    public void setModify(Date modify) 
    {
        this.modify = modify;
    }

    public Date getModify() 
    {
        return modify;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userName", getUserName())
            .append("userPhone", getUserPhone())
            .append("userAge", getUserAge())
            .append("userSalary", getUserSalary())
            .append("userJob", getUserJob())
            .append("userCar", getUserCar())
            .append("userMarriage", getUserMarriage())
            .append("userFb", getUserFb())
            .append("createTime", getCreateTime())
            .append("modify", getModify())
            .toString();
    }
}
