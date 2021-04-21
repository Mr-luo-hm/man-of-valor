package cn.ntshare.laboratory.entity;

import lombok.Data;

@Data
public class user {
    private static final long serialVersionUID = -6056125703075132981L;

    private Integer id;

    private String account;

    private String password;

    private String username;
}
