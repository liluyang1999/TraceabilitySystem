package com.example.server_framework.web.rbac.entity;

import com.example.server_framework.web.core.BaseEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;

/**
 * RBAC Model: Role
 * Table: ss_role
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Role extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer status;

    @Size(max = 30, message = "The role name should be within 20 characters")
    @NotBlank(message = "The role name can't be blank")
    public String getName() {
        return this.name;
    }
}
