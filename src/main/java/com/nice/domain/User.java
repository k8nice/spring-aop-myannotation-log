package com.nice.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = -8540219965013904737L;
    private Long id;
    private String name;
}
