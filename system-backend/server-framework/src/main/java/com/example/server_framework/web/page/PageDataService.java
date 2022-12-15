package com.example.server_framework.web.page;

import com.example.common_resource.utils.ServletUtils;
import org.springframework.stereotype.Service;

@Service
public class PageDataService {

    public static final String PAGE_NUM = "pageNum";
    public static final String PAGE_SIZE = "pageSize";
    public static final String ORDER_BY_COLUMN = "orderByColumn";
    public static final String IS_ASC = "isAsc";

    public static PageDataInfo buildPageRequest() {
        return createPageDataInfo();
    }

    public static PageDataInfo createPageDataInfo() {
        PageDataInfo pageDataInfo = new PageDataInfo();
        pageDataInfo.setPageNum(ServletUtils.conParamToInt(PAGE_NUM));
        pageDataInfo.setPageSize(ServletUtils.conParamToInt(PAGE_SIZE));
        pageDataInfo.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageDataInfo.setIsAsc(ServletUtils.getParameter(IS_ASC));
        return pageDataInfo;
    }
}
