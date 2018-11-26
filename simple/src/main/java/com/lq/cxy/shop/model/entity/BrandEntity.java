package com.lq.cxy.shop.model.entity;

/**
 * 品牌分类
 * @author summer
 * @date 2018/8/29 9:35
 */
public class BrandEntity {


    /**
     * id : 1
     * createTime : 2018-08-25 01:43:49
     * updateTime : 2018-08-25 01:43:49
     * categoryName : 怡宝
     * categoryCode : YB00001
     * parentId :
     * remark :
     */

    /**
     * 是否被选中，界面处理
     */
    private boolean checked;

    private String id;
    private String createTime;
    private String updateTime;
    private String categoryName;
    private String categoryCode;
    private String parentId;
    private String remark;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "BrandEntity{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                ", parentId='" + parentId + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
