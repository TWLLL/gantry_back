package com.qljt.gantry.common.utils.support;


import com.qljt.gantry.common.constant.BusiConstant;

/**
 * 表格数据处理
 *
 * @author ruoyi
 */
public class TableSupport {
    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(ServletUtils.getParameterToInt(BusiConstant.PAGE_NUM));
        pageDomain.setPageSize(ServletUtils.getParameterToInt(BusiConstant.PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(BusiConstant.ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(BusiConstant.IS_ASC));
        return pageDomain;
    }

    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }
}
