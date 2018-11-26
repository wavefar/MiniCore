package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;

public class LicenseInfoEntity implements Parcelable {

    private String id;
    /**
     * 营业执照类型 0多证合一营业执照（统一社会信用代码）
     * 1普通执照 2多证合一营业执照（非统一社会信用代码）
     */
    private int licenseType;
    private int capital;
    /**
     * 证件类型0大陆身份证1护照
     */
    private int idType;
    private String customerId;
    /**
     * 状态0待审核1审核通过2未通过
     */
    private int status;
    private String companyName;
    private String licenseNum;
    private String licenseImg;
    private String licenseAddress;
    private String establishTime;
    private String beginTime;
    private String endTime;
    private String scope;
    private String legalName;
    private String legalNum;
    private String companyAddress;
    private String emContact;
    private String emContactPhone;
    private File file;

    public static final ArrayList<String> ID_TYPE_STR = new ArrayList<>();
    public static final ArrayList<String> LISENCE_TYPE_STR = new ArrayList<>();

    static {
        ID_TYPE_STR.add("身份证");
        ID_TYPE_STR.add("护照");

        LISENCE_TYPE_STR.add("多证合一( 统一社会信用代码 )");
        LISENCE_TYPE_STR.add("普通执照");
        LISENCE_TYPE_STR.add("多证合一( 非统一社会信用代码 )");
    }

    public LicenseInfoEntity() {
    }

    protected LicenseInfoEntity(Parcel in) {
        licenseType = in.readInt();
        capital = in.readInt();
        idType = in.readInt();
        status = in.readInt();
        customerId = in.readString();
        companyName = in.readString();
        licenseNum = in.readString();
        licenseImg = in.readString();
        licenseAddress = in.readString();
        establishTime = in.readString();
        beginTime = in.readString();
        endTime = in.readString();
        scope = in.readString();
        legalName = in.readString();
        legalNum = in.readString();
        companyAddress = in.readString();
        emContact = in.readString();
        emContactPhone = in.readString();
    }

    public static final Creator<LicenseInfoEntity> CREATOR = new Creator<LicenseInfoEntity>() {
        @Override
        public LicenseInfoEntity createFromParcel(Parcel in) {
            return new LicenseInfoEntity(in);
        }

        @Override
        public LicenseInfoEntity[] newArray(int size) {
            return new LicenseInfoEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(int licenseType) {
        this.licenseType = licenseType;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }

    public String getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg;
    }

    public String getLicenseAddress() {
        return licenseAddress;
    }

    public void setLicenseAddress(String licenseAddress) {
        this.licenseAddress = licenseAddress;
    }

    public String getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(String establishTime) {
        this.establishTime = establishTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalNum() {
        return legalNum;
    }

    public void setLegalNum(String legalNum) {
        this.legalNum = legalNum;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getEmContact() {
        return emContact;
    }

    public void setEmContact(String emContact) {
        this.emContact = emContact;
    }

    public String getEmContactPhone() {
        return emContactPhone;
    }

    public void setEmContactPhone(String emContactPhone) {
        this.emContactPhone = emContactPhone;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(licenseType);
        parcel.writeInt(capital);
        parcel.writeInt(idType);
        parcel.writeInt(status);
        parcel.writeString(customerId);
        parcel.writeString(companyName);
        parcel.writeString(licenseNum);
        parcel.writeString(licenseImg);
        parcel.writeString(licenseAddress);
        parcel.writeString(establishTime);
        parcel.writeString(beginTime);
        parcel.writeString(endTime);
        parcel.writeString(scope);
        parcel.writeString(legalName);
        parcel.writeString(legalNum);
        parcel.writeString(companyAddress);
        parcel.writeString(emContact);
        parcel.writeString(emContactPhone);
    }

    public String getApplyStateString() {
        if (status == 2) {
            return "未通过";
        } else if (status == 1) {
            return "审核通过";
        } else {
            return "待审核";
        }
    }
}
