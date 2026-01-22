package com.mindskip.wdd.utility;

import com.github.pagehelper.PageInfo;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 分页工具类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public class PageInfoHelper {

    /**
     * 复制分页对象
     *
     * @param <T>    the type parameter
     * @param <J>    the type parameter
     * @param source the source
     * @param mapper the mapper
     * @return the page info
     */
    public static <T, J> PageInfo<J> copyMap(PageInfo<T> source, Function<? super T, ? extends J> mapper) {
        PageInfo<J> newPage = new PageInfo<>();
        newPage.setPageNum(source.getPageNum());
        newPage.setPageSize(source.getPageSize());
        newPage.setSize(source.getSize());
        newPage.setStartRow(source.getStartRow());
        newPage.setEndRow(source.getEndRow());
        newPage.setTotal(source.getTotal());
        newPage.setPages(source.getPages());
        newPage.setList(source.getList().stream().map(mapper).collect(Collectors.toList()));
        newPage.setPrePage(source.getPrePage());
        newPage.setNextPage(source.getNextPage());
        newPage.setIsFirstPage(source.isIsFirstPage());
        newPage.setIsLastPage(source.isIsLastPage());
        newPage.setHasPreviousPage(source.isHasPreviousPage());
        newPage.setHasNextPage(source.isHasNextPage());
        newPage.setNavigatePages(source.getNavigatePages());
        newPage.setNavigatepageNums(source.getNavigatepageNums());
        newPage.setNavigateFirstPage(source.getNavigateFirstPage());
        newPage.setNavigateLastPage(source.getNavigateLastPage());
        return newPage;
    }
}
