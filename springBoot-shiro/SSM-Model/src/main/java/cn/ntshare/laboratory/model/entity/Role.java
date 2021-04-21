package cn.ntshare.laboratory.model.entity;

import lombok.Data;

@Data
public class Role {
    private static final long serialVersionUID = -1767327914553823741L;

    private Integer id;

    private String role;

    private String desc;
}
