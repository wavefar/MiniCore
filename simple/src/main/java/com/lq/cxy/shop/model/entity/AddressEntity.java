package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class AddressEntity implements Parcelable {

    private static final String divider_char = "|";
    /**
     * id : 479309303983898624
     * createTime : 2018-08-15 15:24:03
     * updateTime : 2018-08-15 15:25:32
     * userName : 李四
     * sex : 0
     * phoneNum : 15888888888
     * address : 四川省成都市双流区华府大道三段保利叶语
     * customerId : 1
     */

    private String id;
    private String createTime;
    private String updateTime;
    private String userName;
    private String sex;
    private String phoneNum;
    private String address;
    private String customerId;

    public AddressEntity() {

    }

    public AddressEntity(Parcel in) {
        id = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
        userName = in.readString();
        sex = in.readString();
        phoneNum = in.readString();
        address = in.readString();
        customerId = in.readString();
    }

    public static final Creator<AddressEntity> CREATOR = new Creator<AddressEntity>() {
        @Override
        public AddressEntity createFromParcel(Parcel in) {
            return new AddressEntity(in);
        }

        @Override
        public AddressEntity[] newArray(int size) {
            return new AddressEntity[size];
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return getDecodedFullAddr();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
        parcel.writeString(userName);
        parcel.writeString(sex);
        parcel.writeString(phoneNum);
        parcel.writeString(address);
        parcel.writeString(customerId);
    }

    public static String encodeString(String province, String city, String area, String detail) {
        StringBuilder sb = new StringBuilder();
        sb.append(province);
        sb.append(divider_char);
        sb.append(city);
        sb.append(divider_char);
        sb.append(area);
        sb.append(divider_char);
        sb.append(detail);
        return sb.toString();
    }

    public String getDecodedFullAddr() {
        return new StringBuilder().append(getAddrProvince())
                .append(" ")
                .append(getAddrCity())
                .append(" ")
                .append(getAddrArea())
                .append(" ")
                .append(getAddrDetail())
                .toString();
    }

    private String getAddrProvince() {
        try {
            if (!TextUtils.isEmpty(address)) {
                int pos = address.indexOf(divider_char);
                if (pos != -1)
                    return address.substring(0, pos);
            }
        } catch (Exception e) {

        }
        return "";
    }

    private String getAddrCity() {
        try {
            if (!TextUtils.isEmpty(address)) {
                int p1 = address.indexOf(divider_char);
                int p2 = address.indexOf(divider_char, p1 + 1);
                if (p1 != -1 && p2 != -1) {
                    return address.substring(p1 + 1, p2);
                }
            }
        } catch (Exception e) {

        }
        return "";
    }

    private String getAddrArea() {
        try {
            if (!TextUtils.isEmpty(address)) {
                int p1 = address.indexOf(divider_char);
                int p2 = address.indexOf(divider_char, p1 + 1);
                p1 = address.indexOf(divider_char, p2 + 1);
                if (p1 != -1 && p2 != -1) {
                    return address.substring(p2 + 1, p1);
                }
            }
        } catch (Exception e) {

        }
        return "";
    }

    private String getAddrDetail() {
        try {
            if (!TextUtils.isEmpty(address)) {
                int p1 = address.indexOf(divider_char);
                int p2 = address.indexOf(divider_char, p1 + 1);
                p1 = address.indexOf(divider_char, p2 + 1);
                if (p1 != -1 && p2 != -1) {
                    return address.substring(p1 + 1);
                }
            }
        } catch (Exception e) {

        }

        return address;
    }
}
