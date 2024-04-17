package com.mall.api;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据封装类
 * Created by macro on 2019/4/19.
 */
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(List<T> list) {
        PageInfo<T> pageResult = new PageInfo<T>(list);
        CommonPage<T> result = new CommonPage<>();
        // 当前页
        result.setPageNum(Convert.toInt(pageResult.getPageNum()));
        // 一页多少条数据
        result.setPageSize(Convert.toInt(pageResult.getPageSize()));
        // 总数据数量
        result.setTotal(pageResult.getTotal());
        // 总页数
//        result.setTotalPage(Convert.toInt(pageResult.getPages()/pageResult.getSize()+1));
        result.setTotalPage(Convert.toInt(pageResult.getPages()));
        // 当前页数据
        result.setList(pageResult.getList());
        System.out.println("//第几页:"+pageResult.getPageNum());
        System.out.println("//没页多少数据："+ pageResult.getPageSize());
        System.out.println("//没页实际多少数据："+ pageResult.getSize());
        System.out.println("//总共几页:"+pageResult.getPages());
        System.out.println("//总共多少条数据:"+pageResult.getTotal());
        System.out.println("//结果集:"+pageResult.getList().get(0).toString());

        return result;
    }
    public static <T> CommonPage<T> restPage(PageInfo<T> pageResult) {
        CommonPage<T> result = new CommonPage<>();
        // 当前页
        result.setPageNum(Convert.toInt(pageResult.getPageNum()));
        // 一页多少条数据
        result.setPageSize(Convert.toInt(pageResult.getPageSize()));
        // 总数据数量
        result.setTotal(pageResult.getTotal());
        // 总页数
//        result.setTotalPage(Convert.toInt(pageResult.getPages()/pageResult.getSize()+1));
        result.setTotalPage(Convert.toInt(pageResult.getPages()));
        // 当前页数据
        result.setList(pageResult.getList());
        return result;
    }
    public static <T> PageInfo<T> getPageInfo(List<T> list, Integer pageNo, Integer pageSize) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setTotal(list.size());
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNo);
        pageInfo.setList((pageNo - 1) * pageSize >= list.size() ?
                new ArrayList<>() : list.subList((pageNo - 1) * pageSize, Math.min(pageNo * pageSize, list.size())));
        return pageInfo;
    }
    /**
     * 将SpringData分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}