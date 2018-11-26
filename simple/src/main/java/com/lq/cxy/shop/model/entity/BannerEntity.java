package com.lq.cxy.shop.model.entity;

/**
 * 广告实体类
 * @author summer
 */
public class BannerEntity{


	/**
	 * id : 484089972618428416
	 * createTime : 2018-08-28 20:00:43
	 * updateTime : 2018-08-28 20:00:43
	 * name : 一桶水
	 * url : http://www.baidu.com
	 * imgPath : http://118.126.113.73/images/20180828/71a0ab5b57d14a4db36c3592b6636cbd.png
	 * ordNum : 1
	 * status : 0
	 */

	private String id;
	private String createTime;
	private String updateTime;
	private String name;
	private String url;
	private String imgPath;
	private int ordNum;
	private int status;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getOrdNum() {
		return ordNum;
	}

	public void setOrdNum(int ordNum) {
		this.ordNum = ordNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BannerEntity{" +
				"id='" + id + '\'' +
				", createTime='" + createTime + '\'' +
				", updateTime='" + updateTime + '\'' +
				", name='" + name + '\'' +
				", url='" + url + '\'' +
				", imgPath='" + imgPath + '\'' +
				", ordNum=" + ordNum +
				", status=" + status +
				'}';
	}
}
