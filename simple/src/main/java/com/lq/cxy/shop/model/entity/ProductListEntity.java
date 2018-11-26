package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品实体类
 *
 * @author: summer
 * @date: 2018/8/15 15:08
 */
public class ProductListEntity implements Parcelable {

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private List<ProductEntity> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ProductEntity> getList() {
        return list;
    }

    public void setList(List<ProductEntity> list) {
        this.list = list;
    }


    public ProductListEntity() {
    }

    @Override
    public String toString() {
        return "ProductListEntity{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", size=" + size +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", total=" + total +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pageNum);
        dest.writeInt(this.pageSize);
        dest.writeInt(this.size);
        dest.writeInt(this.startRow);
        dest.writeInt(this.endRow);
        dest.writeInt(this.total);
        dest.writeInt(this.pages);
        dest.writeList(this.list);
    }

    protected ProductListEntity(Parcel in) {
        this.pageNum = in.readInt();
        this.pageSize = in.readInt();
        this.size = in.readInt();
        this.startRow = in.readInt();
        this.endRow = in.readInt();
        this.total = in.readInt();
        this.pages = in.readInt();
        this.list = new ArrayList<ProductEntity>();
        in.readList(this.list, ProductEntity.class.getClassLoader());
    }

    public static final Creator<ProductListEntity> CREATOR = new Creator<ProductListEntity>() {
        @Override
        public ProductListEntity createFromParcel(Parcel source) {
            return new ProductListEntity(source);
        }

        @Override
        public ProductListEntity[] newArray(int size) {
            return new ProductListEntity[size];
        }
    };
}
