package com.stephenshen.raws.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName : User  //类名
 * @Description :   //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-02 12:46  //时间
 */
public class User implements Serializable {
    private Integer userId;//用户编号
    private String nickName;//用户昵称
    private Integer mobilePhone;// 手机号
    private Date createTime;// 创建时间
    private Date updateTime;// 更新时间

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(Integer mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}