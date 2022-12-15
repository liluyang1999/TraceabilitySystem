package com.example.blockchain_gateway.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT")
    protected Date createTime;
    protected String creator;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT")
    protected Date updateTime;
    protected String updater;

}
