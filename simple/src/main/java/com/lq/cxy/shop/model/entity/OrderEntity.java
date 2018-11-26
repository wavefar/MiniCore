package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

/**
 * 订单实体
 *
 * @author summer
 * @date 2018/9/8 下午3:29
 */
public class OrderEntity implements Parcelable {

    public static final int ORDER_HAS_COMPLETED = 4;
    public static final int ORDER_IN_SENDING = 3;
    public final static HashMap<Integer, String> ORDER_STATUS_MAP = new HashMap<>();

    static {
        ORDER_STATUS_MAP.put(0, "待付款");
        ORDER_STATUS_MAP.put(1, "已付款");
        ORDER_STATUS_MAP.put(2, "待发货");
        ORDER_STATUS_MAP.put(3, "配送中");
        ORDER_STATUS_MAP.put(ORDER_HAS_COMPLETED, "已完成");
        ORDER_STATUS_MAP.put(5, "待评价");

    }

    private List<ProductEntity> goods;
    private AddressEntity address;
    private String disDate;
    private String disTime;


    /**
     * 订单ID
     */
    private String id;
    /**
     * 订单状态 0待付款1已购买2待分配3配送中4已完成5待评价
     */
    private int status;

    private String createTime;
    private String updateTime;
    private String orderCode;
    private double money;
    /**
     * 用户收货地址
     */
    private String receiveId;
    /**
     * 购买者ID
     */
    private String customerId;
    /**
     * 配送方式0邮寄1自提
     */
    private String disType = "0";
    /**
     * 自提地址
     */
    private String liftAddress;
    /**
     * 运单号
     */
    private String waybillId;

    /**
     * 物流公司名称
     */
    private String waybillName;

    /**
     * 是否是购物车下单
     */
    private boolean isShopCar;

    /**
     * 需要缴押金特殊字段
     * @return
     */
    private double price;
    private String storeId;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isShopCar() {
        return isShopCar;
    }

    public void setShopCar(boolean shopCar) {
        isShopCar = shopCar;
    }

    public String getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(String waybillId) {
        this.waybillId = waybillId;
    }

    public String getWaybillName() {
        return waybillName;
    }

    public void setWaybillName(String waybillName) {
        this.waybillName = waybillName;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDisType() {
        return disType;
    }

    public void setDisType(String disType) {
        this.disType = disType;
    }

    public String getLiftAddress() {
        return liftAddress;
    }

    public void setLiftAddress(String liftAddress) {
        this.liftAddress = liftAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static Creator<OrderEntity> getCREATOR() {
        return CREATOR;
    }


    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public String getDisDate() {
        return disDate;
    }

    public void setDisDate(String disDate) {
        this.disDate = disDate;
    }

    public String getDisTime() {
        return disTime;
    }

    public void setDisTime(String disTime) {
        this.disTime = disTime;
    }

    public List<ProductEntity> getGoods() {

        return goods;
    }

    public void setGoods(List<ProductEntity> goods) {
        this.goods = goods;
    }

    public OrderEntity() {
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "goods=" + goods +
                ", address=" + address +
                ", disDate='" + disDate + '\'' +
                ", disTime='" + disTime + '\'' +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", money=" + money +
                ", receiveId='" + receiveId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", disType='" + disType + '\'' +
                ", liftAddress='" + liftAddress + '\'' +
                ", waybillId='" + waybillId + '\'' +
                ", waybillName='" + waybillName + '\'' +
                ", isShopCar=" + isShopCar +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.goods);
        dest.writeParcelable(this.address, flags);
        dest.writeString(this.disDate);
        dest.writeString(this.disTime);
        dest.writeString(this.id);
        dest.writeInt(this.status);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.orderCode);
        dest.writeDouble(this.money);
        dest.writeString(this.receiveId);
        dest.writeString(this.customerId);
        dest.writeString(this.disType);
        dest.writeString(this.liftAddress);
        dest.writeString(this.waybillId);
        dest.writeString(this.waybillName);
        dest.writeByte(this.isShopCar ? (byte) 1 : (byte) 0);
    }

    protected OrderEntity(Parcel in) {
        this.goods = in.createTypedArrayList(ProductEntity.CREATOR);
        this.address = in.readParcelable(AddressEntity.class.getClassLoader());
        this.disDate = in.readString();
        this.disTime = in.readString();
        this.id = in.readString();
        this.status = in.readInt();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.orderCode = in.readString();
        this.money = in.readDouble();
        this.receiveId = in.readString();
        this.customerId = in.readString();
        this.disType = in.readString();
        this.liftAddress = in.readString();
        this.waybillId = in.readString();
        this.waybillName = in.readString();
        this.isShopCar = in.readByte() != 0;
    }

    public static final Creator<OrderEntity> CREATOR = new Creator<OrderEntity>() {
        @Override
        public OrderEntity createFromParcel(Parcel source) {
            return new OrderEntity(source);
        }

        @Override
        public OrderEntity[] newArray(int size) {
            return new OrderEntity[size];
        }
    };
}
