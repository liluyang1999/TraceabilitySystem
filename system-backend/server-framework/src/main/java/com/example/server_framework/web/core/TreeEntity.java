package com.example.server_framework.web.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TreeEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long parentID;

    private String parentName;

    private Integer serialNumber;

    private String ancestor;

    private List<?> children = new ArrayList<>();
}
