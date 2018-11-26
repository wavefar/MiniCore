package com.lq.cxy.shop.model.entity;

/**
 * 版本检查实体类
 * @author summer
 * @date 2018/10/18 16:49
 */
public class VersionInfoEntity {
    /**
     * id : 1
     * createTime : 2018-10-19 00:21:28
     * updateTime : 2018-10-19 00:21:28
     * versionName : 检查到新版本
     * versionCode : 100001
     * versionDesc : 系统优化
     * versionUrl : http://118.126.113.73/images/wanjia.apk
     * osType : 0
     */

    private String id;
    private String createTime;
    private String updateTime;
    private String versionName;
    private String versionCode;
    private String versionDesc;
    private String versionUrl;
    private int osType;

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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public int getOsType() {
        return osType;
    }

    public void setOsType(int osType) {
        this.osType = osType;
    }
}
