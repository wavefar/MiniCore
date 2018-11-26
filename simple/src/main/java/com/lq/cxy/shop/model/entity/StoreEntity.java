package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author summer
 * @date 2018/9/4 下午10:00
 */
public class StoreEntity implements Parcelable {

    /**
     * id : 2
     * storeName : 成都王府井购物中心总府店
     * distance : 9.78
     * longitude : 104.08618122339249
     * latitude : 30.66419604784904
     * goods : [{"id":"478614935660658688","createTime":"2018-08-13 17:24:53","updateTime":null,"goodsName":"oppo find x","goodsCode":"oppo find x","price":8000.23,"avatar":"http://118.126.113.73/images/20180813/43d8e959fe27de75e30e94d38f25355c.jpg","detail":"http://118.126.113.73/images/22222","remark":"2222","categoryId":"1","storeId":"2","goodStatus":1,"stock":100}]
     */

    private String id;
    private String storeName;
    private double distance;
    private String longitude;
    private String latitude;
    private List<ProductEntity> goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public List<ProductEntity> getGoods() {
        return goods;
    }

    public void setGoods(List<ProductEntity> goods) {
        this.goods = goods;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.storeName);
        dest.writeDouble(this.distance);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeList(this.goods);
    }

    public StoreEntity() {
    }

    protected StoreEntity(Parcel in) {
        this.id = in.readString();
        this.storeName = in.readString();
        this.distance = in.readDouble();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.goods = new ArrayList<ProductEntity>();
        in.readList(this.goods, ProductEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<StoreEntity> CREATOR = new Parcelable.Creator<StoreEntity>() {
        @Override
        public StoreEntity createFromParcel(Parcel source) {
            return new StoreEntity(source);
        }

        @Override
        public StoreEntity[] newArray(int size) {
            return new StoreEntity[size];
        }
    };
}
