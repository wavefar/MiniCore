package com.lq.cxy.shop.model.entity;

import java.util.List;

/**
 * 评论相关列表
 * @author summer
 * @date 2018/9/19 9:19
 */
public class EvaluateListEntity {

    /**
     * pageNum : 1
     * pageSize : 20
     * size : 1
     * startRow : 1
     * endRow : 1
     * total : 1
     * pages : 1
     * list : [{"id":"1","createTime":"2018-09-01 01:19:25","updateTime":"2018-09-01 01:19:25","customerId":"1","evaluate":"送货很快商品质量很好","star":5,"goodsId":"479627039511412736","orderId":"","loginName":"admin","name":"zgy","phoneNum":"15888888888","avatar":"http://118.126.113.73/images/20180820/5e35b4fc6c51f668836b78b54f6802f3.jpg"}]
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private List<EvaluateEntity> list;

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

    public List<EvaluateEntity> getList() {
        return list;
    }

    public void setList(List<EvaluateEntity> list) {
        this.list = list;
    }
}
