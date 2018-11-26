package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class ShopAddEntity implements Parcelable {
    private String id;
    private String createTime;
    private String updateTime;
    private String customerId;
    private String storeName;
    private String storeCode;
    private String storeImg;
    private String storeTel;
    private String longitude;
    private String latitude;
    private String storeAddress;
    private String reason;
    private int status;
    private File file;
    private int categoryId;

    protected ShopAddEntity(Parcel in) {
        id = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
        customerId = in.readString();
        storeName = in.readString();
        storeCode = in.readString();
        storeImg = in.readString();
        storeTel = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        storeAddress = in.readString();
        reason = in.readString();
        status = in.readInt();
        categoryId = in.readInt();
    }

    public static final Creator<ShopAddEntity> CREATOR = new Creator<ShopAddEntity>() {
        @Override
        public ShopAddEntity createFromParcel(Parcel in) {
            return new ShopAddEntity(in);
        }

        @Override
        public ShopAddEntity[] newArray(int size) {
            return new ShopAddEntity[size];
        }
    };

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ShopAddEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(createTime);
        parcel.writeString(updateTime);
        parcel.writeString(customerId);
        parcel.writeString(storeName);
        parcel.writeString(storeCode);
        parcel.writeString(storeImg);
        parcel.writeString(storeTel);
        parcel.writeString(longitude);
        parcel.writeString(latitude);
        parcel.writeString(storeAddress);
        parcel.writeString(reason);
        parcel.writeInt(status);
        parcel.writeInt(categoryId);
    }
}
