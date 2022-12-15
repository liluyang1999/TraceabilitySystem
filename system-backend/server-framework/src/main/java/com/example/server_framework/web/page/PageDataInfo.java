package com.example.server_framework.web.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Page Data
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageDataInfo implements Serializable {

    // Start Index of current page
    private Integer pageNum;

    // Size of every page
    private Integer pageSize;

    private String orderByColumn;

    // asc or desc, default: asc
    private String isAsc = "asc";

    public String getOrderBy() {
        if (!StringUtils.hasText(orderByColumn)) {
            return "";
        }
        return orderByColumn + " " + isAsc;
    }

}
