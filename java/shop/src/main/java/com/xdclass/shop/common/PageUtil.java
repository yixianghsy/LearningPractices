package com.xdclass.shop.common;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author daniel
 * @version 2020/03/14
 */
public class PageUtil {
    public static int PAGE_SIZE = 10;

    public static int[] init(Page<?> page, HttpServletRequest request) {
        int pageNumber = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("p"), "1"));
        page.setPageNo(pageNumber);
        int pageSize = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("ps"), String.valueOf(PAGE_SIZE)));
        page.setPageSize(pageSize);
        return new int[]{pageNumber, pageSize};
    }
}
