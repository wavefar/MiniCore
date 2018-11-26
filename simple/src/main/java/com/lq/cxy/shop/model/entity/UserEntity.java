package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户实体
 *
 * @author summer
 */
public class UserEntity implements Parcelable {

    /**
     * 用户主键ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户token
     */
    private String token;

    private String createTime;

    private String updateTime;

    private String loginName;

    private String password;

    private String address;

    private String email;

    /**
     * 是否过期0正常1停用
     */
    private int expire;

    /**
     * 性别0男1女2未知
     */
    private int sex;

    /**
     * 是否是商家0普通消费者1商家
     */
    private int business;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 电话号码
     */
    private String phoneNum;

    /**
     * 昵称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getBusiness() {
        return business;
    }

    public void setBusiness(int business) {
        this.business = business;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


    public UserEntity() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isMerchantUser() {
        return business == 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", expire=" + expire +
                ", sex=" + sex +
                ", business=" + business +
                ", avatar='" + avatar + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.token);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.loginName);
        dest.writeString(this.password);
        dest.writeString(this.address);
        dest.writeString(this.email);
        dest.writeInt(this.expire);
        dest.writeInt(this.sex);
        dest.writeInt(this.business);
        dest.writeString(this.avatar);
        dest.writeString(this.phoneNum);
        dest.writeString(this.name);
    }

    protected UserEntity(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.token = in.readString();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.loginName = in.readString();
        this.password = in.readString();
        this.address = in.readString();
        this.email = in.readString();
        this.expire = in.readInt();
        this.sex = in.readInt();
        this.business = in.readInt();
        this.avatar = in.readString();
        this.phoneNum = in.readString();
        this.name = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}
