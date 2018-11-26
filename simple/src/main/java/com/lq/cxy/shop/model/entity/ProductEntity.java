package com.lq.cxy.shop.model.entity;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 所有产品实体类
 *
 * @author summer
 */
public class ProductEntity implements Parcelable {
    private boolean follow;
    private String id;
    private String createTime;
    private String updateTime;
    private String orderId;
    private String goodsName;
    private String goodsCode;
    private double price;
    private String avatar;
    private String detail;
    private String remark;
    private String categoryId;
    private String storeId;
    private int goodStatus;
    private int stock;

    /**
     * 以下为收藏相关字段
     */
    private String goodsId;
    private String customerId;

    /**
     * 购买数量
     */
    private int num;


    /**
     * 店铺名称
     */
    private String storeName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime=" + updateTime +
                ", goodsName='" + goodsName + '\'' +
                ", goodsCode='" + goodsCode + '\'' +
                ", price=" + price +
                ", avatar='" + avatar + '\'' +
                ", detail='" + detail + '\'' +
                ", remark='" + remark + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", goodStatus=" + goodStatus +
                ", stock=" + stock +
                '}';
    }

    public int getGoodStatus() {
        return goodStatus;
    }

    public void setGoodStatus(int goodStatus) {
        this.goodStatus = goodStatus;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ProductEntity() {
    }

    public String getStatusStr() {
        switch (goodStatus) {
            case 0:
                return "待上架";
            case 1:
                return "已上架";
            case 2:
                return "已下架";
            case 3:
                return "待审核";
            case 4:
                return "未通过审核";
        }
        return "审核通过";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.follow ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.orderId);
        dest.writeString(this.goodsName);
        dest.writeString(this.goodsCode);
        dest.writeDouble(this.price);
        dest.writeString(this.avatar);
        dest.writeString(this.detail);
        dest.writeString(this.remark);
        dest.writeString(this.categoryId);
        dest.writeString(this.storeId);
        dest.writeInt(this.goodStatus);
        dest.writeInt(this.stock);
        dest.writeString(this.goodsId);
        dest.writeString(this.customerId);
        dest.writeInt(this.num);
        dest.writeString(this.storeName);
    }

    protected ProductEntity(Parcel in) {
        this.follow = in.readByte() != 0;
        this.id = in.readString();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.orderId = in.readString();
        this.goodsName = in.readString();
        this.goodsCode = in.readString();
        this.price = in.readDouble();
        this.avatar = in.readString();
        this.detail = in.readString();
        this.remark = in.readString();
        this.categoryId = in.readString();
        this.storeId = in.readString();
        this.goodStatus = in.readInt();
        this.stock = in.readInt();
        this.goodsId = in.readString();
        this.customerId = in.readString();
        this.num = in.readInt();
        this.storeName = in.readString();
    }

    public static final Creator<ProductEntity> CREATOR = new Creator<ProductEntity>() {
        @Override
        public ProductEntity createFromParcel(Parcel source) {
            return new ProductEntity(source);
        }

        @Override
        public ProductEntity[] newArray(int size) {
            return new ProductEntity[size];
        }
    };
}