package com.nice.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SystemLog implements Serializable {
    private static final long serialVersionUID = -5533371291680836835L;
    private Long id;
    private String description;
    private String method;
    private Long logType;
    private String requestId;
    private String exceptioncode;
    private String exceptionDetail;
    private String params;
    private String createBy;
    private Date createDate;
}
