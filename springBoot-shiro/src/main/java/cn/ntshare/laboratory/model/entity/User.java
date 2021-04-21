package cn.ntshare.laboratory.model.entity;

import lombok.Data;

@Data
public class User {
    private static final long serialVersionUID = -6056125703075132981L;

    private Integer id;

    private String account;

    private String password;

    private String username;
}
