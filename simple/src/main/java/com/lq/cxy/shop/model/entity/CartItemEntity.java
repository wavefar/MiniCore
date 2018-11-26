package com.lq.cxy.shop.model.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 购物车商品项
 */
public class CartItemEntity extends BaseObservable implements Parcelable {
    private String id;
    private String createTime;
    private String updateTime;
    private String customerId;
    private String goodsId;
    private int num;
    private String goodsName;
    private String goodsCode;
    private double price;
    private String avatar;

    @Bindable
    public boolean isItemSeleted() {
        return isItemSeleted;
    }

    public void setItemSeleted(boolean itemSeleted) {
        isItemSeleted = itemSeleted;
    }

    // ui 上用的字段
    private boolean isItemSeleted;

    public CartItemEntity() {

    }

    protected CartItemEntity(Parcel in) {
        id = in.readString();
        createTime = in.readString();
        customerId = in.readString();
        goodsId = in.readString();
        num = in.readInt();
        goodsName = in.readString();
        goodsCode = in.readString();
        price = in.readDouble();
        avatar = in.readString();
    }

    public static final Creator<CartItemEntity> CREATOR = new Creator<CartItemEntity>() {
        @Override
        public CartItemEntity createFromParcel(Parcel in) {
            return new CartItemEntity(in);
        }

        @Override
        public CartItemEntity[] newArray(int size) {
            return new CartItemEntity[size];
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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @Bindable
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
//        notifyPropertyChanged(BR.);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(createTime);
        parcel.writeString(customerId);
        parcel.writeString(goodsId);
        parcel.writeInt(num);
        parcel.writeString(goodsName);
        parcel.writeString(goodsCode);
        parcel.writeDouble(price);
        parcel.writeString(avatar);
    }

    public static ProductEntity toProduct(CartItemEntity cartItem) {
        ProductEntity entity = new ProductEntity();
        if (cartItem != null) {
            entity.setAvatar(cartItem.getAvatar());
            entity.setId(cartItem.getId());
            entity.setGoodsCode(cartItem.getGoodsCode());
            entity.setGoodsName(cartItem.getGoodsName());
            entity.setGoodsId(cartItem.getGoodsId());
            entity.setNum(cartItem.getNum());
            entity.setCustomerId(cartItem.getCustomerId());
            entity.setPrice(cartItem.getPrice());
        }
        return entity;
    }
}
