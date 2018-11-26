package com.lq.cxy.shop.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 收藏列表
 * @author summer
 */
public class FavGoodsListEntity implements Parcelable {

    /**
     * pageNum : 1
     * pageSize : 20
     * size : 2
     * startRow : 1
     * endRow : 2
     * total : 2
     * pages : 1
     * navigateFirstPage : 1
     * navigateLastPage : 1
     * firstPage : 1
     * lastPage : 1
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int firstPage;
    private int lastPage;

    public FavGoodsListEntity() {

    }

    public List<ProductEntity> getList() {
        return list;
    }

    public void setList(List<ProductEntity> list) {
        this.list = list;
    }

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

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
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
        dest.writeInt(this.navigateFirstPage);
        dest.writeInt(this.navigateLastPage);
        dest.writeInt(this.firstPage);
        dest.writeInt(this.lastPage);
        dest.writeTypedList(this.list);
    }

    protected FavGoodsListEntity(Parcel in) {
        this.pageNum = in.readInt();
        this.pageSize = in.readInt();
        this.size = in.readInt();
        this.startRow = in.readInt();
        this.endRow = in.readInt();
        this.total = in.readInt();
        this.pages = in.readInt();
        this.navigateFirstPage = in.readInt();
        this.navigateLastPage = in.readInt();
        this.firstPage = in.readInt();
        this.lastPage = in.readInt();
        this.list = in.createTypedArrayList(ProductEntity.CREATOR);
    }

    public static final Creator<FavGoodsListEntity> CREATOR = new Creator<FavGoodsListEntity>() {
        @Override
        public FavGoodsListEntity createFromParcel(Parcel source) {
            return new FavGoodsListEntity(source);
        }

        @Override
        public FavGoodsListEntity[] newArray(int size) {
            return new FavGoodsListEntity[size];
        }
    };
}
