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
    private Long userId;//用户编号
    private String nickName;//用户昵称
    private String mobilePhone;// 手机号
    private Date createTime;// 创建时间
    private Date updateTime;// 更新时间

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("userId=").append(userId);
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", mobilePhone='").append(mobilePhone).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}