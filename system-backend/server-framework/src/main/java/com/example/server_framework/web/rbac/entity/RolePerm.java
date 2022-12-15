package com.example.server_framework.web.rbac.entity;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Relationship between Role and Permission
 * Table: ss_role_perm
 */
@Data
public class RolePerm {

    private Long roleId;

    private Long permId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getRoleId())
                .append("roleId", getPermId())
                .toString();
    }
}
