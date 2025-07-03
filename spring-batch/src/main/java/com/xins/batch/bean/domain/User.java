package com.xins.batch.bean.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private Date birthDate;

    private Date registrationTime;

    private Date lastLoginTime;

    private Integer status;

    private String address;

}