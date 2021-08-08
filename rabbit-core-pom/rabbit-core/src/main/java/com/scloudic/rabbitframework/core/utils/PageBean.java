package com.scloudic.rabbitframework.core.utils;

import java.util.List;

/**
 * 分页工具类
 *
 * @author justin.liang
 */
public class PageBean<T> {
    public static final long DEFAULT_LIMIT = 15;
    public static final long DEFAULT_OFFSET = 1;
    private long pageSize; // 每页显示的数据条数。
    private long totalRecord; // 总的记录条数。查询数据库得到的数据
    // 需要计算得来
    private long totalPage; // 总页数，通过totalRecord和pageSize计算可以得来
    // 开始索引，也就是我们在数据库中要从第几行数据开始拿，有了startIndex和pageSize，
    // 就知道了limit语句的两个数据，就能获得每页需要显示的数据了
    private long startPage;
    // 将每页要显示的数据放在list集合中
    private List<T> datas;

    public PageBean(Long pageNum, Long pageSize) {
        long offset = pageNum == null ? DEFAULT_OFFSET : pageNum.longValue();
        this.pageSize = pageSize == null ? DEFAULT_LIMIT : pageSize.longValue();
        if (offset <= 0) {
            offset = 1;
        }
        // 开始索引
        this.startPage = (offset - 1) * this.pageSize;
    }

    // 通过pageNum，pageSize，totalRecord计算得来tatalPage和startIndex
    // 构造方法中将pageNum，pageSize，totalRecord获得
    public PageBean(Long pageNum, Long pageSize, long totalRecord) {
        long offset = pageNum == null ? DEFAULT_OFFSET : pageNum.longValue();
        this.pageSize = pageSize == null ? DEFAULT_LIMIT : pageSize.longValue();
        if (offset <= 0) {
            offset = 1;
        }
        this.totalRecord = totalRecord;
        // totalPage 总页数
        if (totalRecord % this.pageSize == 0) {
            // 说明整除，正好每页显示pageSize条数据，没有多余一页要显示少于pageSize条数据的
            this.totalPage = totalRecord / this.pageSize;
        } else {
            // 不整除，就要在加一页，来显示多余的数据。
            this.totalPage = totalRecord / this.pageSize + 1;
        }
        // 开始索引
        this.startPage = (offset - 1) * this.pageSize;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getStartPage() {
        return startPage;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

}