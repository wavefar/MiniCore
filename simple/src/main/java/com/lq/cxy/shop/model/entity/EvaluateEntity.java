package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 评论实体
 * @author summer
 */
public class EvaluateEntity implements Parcelable {

    public ItemBinding<ReplyEntity> replyItem = ItemBinding.of(BR.replyEntity, R.layout.item_evaluate_reply);

    private String id;
    private String createTime;
    private String updateTime;
    private String customerId;
    private String evaluate;
    private int star;
    private String goodsId;
    private String orderId;
    private String loginName;
    private String name;
    private String phoneNum;
    private String avatar;


    /**
     * 以下为商家回复的内容
     */

    private List<ReplyEntity> reply;

    public List<ReplyEntity> getReply() {
        return reply;
    }

    public void setReply(List<ReplyEntity> reply) {
        this.reply = reply;
    }


    public static class ReplyEntity {

        private String id;
        private String createTime;
        private String updateTime;
        private String evaluateId;
        private String fromCustomerId;
        private String replyMsg;
        private String fromCustomerLoginName;
        private String fromCustomerName;
        private String fromCustomerPhoneNum;
        private String fromCustomerAvatar;

        public String getEvaluateId() {
            return evaluateId;
        }

        public void setEvaluateId(String evaluateId) {
            this.evaluateId = evaluateId;
        }

        public String getFromCustomerId() {
            return fromCustomerId;
        }

        public void setFromCustomerId(String fromCustomerId) {
            this.fromCustomerId = fromCustomerId;
        }

        public String getReplyMsg() {
            return replyMsg;
        }

        public void setReplyMsg(String replyMsg) {
            this.replyMsg = replyMsg;
        }

        public String getFromCustomerLoginName() {
            return fromCustomerLoginName;
        }

        public void setFromCustomerLoginName(String fromCustomerLoginName) {
            this.fromCustomerLoginName = fromCustomerLoginName;
        }

        public String getFromCustomerName() {
            return fromCustomerName;
        }

        public void setFromCustomerName(String fromCustomerName) {
            this.fromCustomerName = fromCustomerName;
        }

        public String getFromCustomerPhoneNum() {
            return fromCustomerPhoneNum;
        }

        public void setFromCustomerPhoneNum(String fromCustomerPhoneNum) {
            this.fromCustomerPhoneNum = fromCustomerPhoneNum;
        }

        public String getFromCustomerAvatar() {
            return fromCustomerAvatar;
        }

        public void setFromCustomerAvatar(String fromCustomerAvatar) {
            this.fromCustomerAvatar = fromCustomerAvatar;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.customerId);
        dest.writeString(this.evaluate);
        dest.writeInt(this.star);
        dest.writeString(this.goodsId);
        dest.writeString(this.orderId);
        dest.writeString(this.loginName);
        dest.writeString(this.name);
        dest.writeString(this.phoneNum);
        dest.writeString(this.avatar);
        dest.writeList(this.reply);
    }

    public EvaluateEntity() {
    }

    protected EvaluateEntity(Parcel in) {
        this.id = in.readString();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.customerId = in.readString();
        this.evaluate = in.readString();
        this.star = in.readInt();
        this.goodsId = in.readString();
        this.orderId = in.readString();
        this.loginName = in.readString();
        this.name = in.readString();
        this.phoneNum = in.readString();
        this.avatar = in.readString();
        this.reply = new ArrayList<ReplyEntity>();
        in.readList(this.reply, ReplyEntity.class.getClassLoader());
    }

    public static final Creator<EvaluateEntity> CREATOR = new Creator<EvaluateEntity>() {
        @Override
        public EvaluateEntity createFromParcel(Parcel source) {
            return new EvaluateEntity(source);
        }

        @Override
        public EvaluateEntity[] newArray(int size) {
            return new EvaluateEntity[size];
        }
    };
}