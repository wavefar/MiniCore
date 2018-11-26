package com.lq.cxy.shop.model.entity;

import java.util.List;

/**
 * 订单列表
 * @author summer
 * @date 2018/9/9 下午6:24
 */
public class OrderListEntity {


    /**
     * pageNum : 1
     * pageSize : 20
     * size : 7
     * startRow : 1
     * endRow : 7
     * total : 7
     * pages : 1
     * list : [{"id":"488091628838322176","createTime":"2018-09-08 21:01:53","updateTime":"2018-09-08 21:01:53","orderCode":"488091628838322177","money":66666,"receiveId":"486535408398630912","customerId":"481540883053805568","status":0,"disType":"","liftAddress":"","disDate":"09月09日 周日 明天","disTime":"08:00-10:00"}]
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private List<OrderEntity> list;

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

    public List<OrderEntity> getList() {
        return list;
    }

    public void setList(List<OrderEntity> list) {
        this.list = list;
    }

}
