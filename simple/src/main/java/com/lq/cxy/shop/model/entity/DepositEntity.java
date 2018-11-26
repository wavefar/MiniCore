package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * 押金实体类
 *
 * @author summer
 * @date 2018/11/13 17:44
 */
public class DepositEntity implements Parcelable {

    public final static HashMap<Integer, String> ORDER_STATUS_MAP = new HashMap<>();

    static {
        ORDER_STATUS_MAP.put(0, "待付款");
        ORDER_STATUS_MAP.put(1, "已付款");
        ORDER_STATUS_MAP.put(2, "已退款");
        ORDER_STATUS_MAP.put(3, "待退款");
    }

    private String id;
    private String createTime;
    private String updateTime;
    private String storeId;
    private String customerId;
    private String name;
    private double money;
    private String orderId;
    private String remark;
    private String loginName;

    private String avatar;
    /**
     * 0待支付1已支付2已退款3待退款
     */
    private int orderStatus;
    private String storeName;

    protected DepositEntity(Parcel in) {
        id = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
        storeId = in.readString();
        customerId = in.readString();
        name = in.readString();
        money = in.readDouble();
        orderId = in.readString();
        remark = in.readString();
        loginName = in.readString();
        avatar = in.readString();
        orderStatus = in.readInt();
        storeName = in.readString();
    }

    public static final Creator<DepositEntity> CREATOR = new Creator<DepositEntity>() {
        @Override
        public DepositEntity createFromParcel(Parcel in) {
            return new DepositEntity(in);
        }

        @Override
        public DepositEntity[] newArray(int size) {
            return new DepositEntity[size];
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public DepositEntity() {
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
        parcel.writeString(storeId);
        parcel.writeString(customerId);
        parcel.writeString(name);
        parcel.writeDouble(money);
        parcel.writeString(orderId);
        parcel.writeString(remark);
        parcel.writeString(loginName);
        parcel.writeString(avatar);
        parcel.writeInt(orderStatus);
        parcel.writeString(storeName);
    }
}
