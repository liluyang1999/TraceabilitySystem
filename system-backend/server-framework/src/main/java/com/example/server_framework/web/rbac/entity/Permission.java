package com.example.server_framework.web.rbac.entity;

import com.example.server_framework.web.core.BaseEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;

/**
 * RBAC Model: Permission
 * Table: ss_permission
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Permission extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String path;

    private Integer status;

    @Size(max = 30, message = "The permission name should be within 20 characters")
    @NotBlank(message = "The permission name can't be blank")
    public String getName() {
        return this.name;
    }
}
