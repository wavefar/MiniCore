package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MerchantAssesItemEntity implements Parcelable {
    private String id;
    private String customerId;
    private String evaluate;
    private int star;
    private String goodsId;
    private String orderId;
    private String createTime;
    private Object updateTime;
    private String loginName;
    private String name;
    private String phoneNum;
    private String avatar;
    private String goodsName;
    private String goodsCode;
    private double price;
    private String goodsAvatar;
    private String storeName;

    public MerchantAssesItemEntity() {
    }

    protected MerchantAssesItemEntity(Parcel in) {
        id = in.readString();
        customerId = in.readString();
        evaluate = in.readString();
        star = in.readInt();
        goodsId = in.readString();
        orderId = in.readString();
        createTime = in.readString();
        loginName = in.readString();
        name = in.readString();
        phoneNum = in.readString();
        avatar = in.readString();
        goodsName = in.readString();
        goodsCode = in.readString();
        price = in.readDouble();
        goodsAvatar = in.readString();
        storeName = in.readString();
    }

    public static final Creator<MerchantAssesItemEntity> CREATOR = new Creator<MerchantAssesItemEntity>() {
        @Override
        public MerchantAssesItemEntity createFromParcel(Parcel in) {
            return new MerchantAssesItemEntity(in);
        }

        @Override
        public MerchantAssesItemEntity[] newArray(int size) {
            return new MerchantAssesItemEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGoodsAvatar() {
        return goodsAvatar;
    }

    public void setGoodsAvatar(String goodsAvatar) {
        this.goodsAvatar = goodsAvatar;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(customerId);
        parcel.writeString(evaluate);
        parcel.writeInt(star);
        parcel.writeString(goodsId);
        parcel.writeString(orderId);
        parcel.writeString(createTime);
        parcel.writeString(loginName);
        parcel.writeString(name);
        parcel.writeString(phoneNum);
        parcel.writeString(avatar);
        parcel.writeString(goodsName);
        parcel.writeString(goodsCode);
        parcel.writeDouble(price);
        parcel.writeString(goodsAvatar);
        parcel.writeString(storeName);
    }
}
