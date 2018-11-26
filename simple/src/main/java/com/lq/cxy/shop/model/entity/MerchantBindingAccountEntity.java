package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class MerchantBindingAccountEntity implements Parcelable {
    private String id;
    private String createTime;
    private String updateTime;
    private String customerId;
    private String account;
    private String accounType;
    private String realName;

    public MerchantBindingAccountEntity() {
    }


    protected MerchantBindingAccountEntity(Parcel in) {
        id = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
        customerId = in.readString();
        account = in.readString();
        accounType = in.readString();
        realName = in.readString();
    }

    public static final Creator<MerchantBindingAccountEntity> CREATOR = new Creator<MerchantBindingAccountEntity>() {
        @Override
        public MerchantBindingAccountEntity createFromParcel(Parcel in) {
            return new MerchantBindingAccountEntity(in);
        }

        @Override
        public MerchantBindingAccountEntity[] newArray(int size) {
            return new MerchantBindingAccountEntity[size];
        }
    };

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccounType() {
        return accounType;
    }

    public int getAccountTypeInt() {
        return TextUtils.isEmpty(accounType) ? -1 : Integer.valueOf(accounType);
    }

    public void setAccounType(String accounType) {
        this.accounType = accounType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
        parcel.writeString(account);
        parcel.writeString(accounType);
        parcel.writeString(realName);
    }
}
