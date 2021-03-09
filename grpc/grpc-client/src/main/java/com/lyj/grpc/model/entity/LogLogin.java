package com.lyj.grpc.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lyj
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogLogin {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Long userId;

	private Integer tenantId;

	/**
	 * 登录类型 1 登录2登出
	 */
	private Integer logType;

	private LocalDateTime logDate;

	private Long createBy;

	private Long updateBy;

	private Long deleteBy;

	private LocalDateTime createAt;

	private LocalDateTime updateAt;

	private LocalDateTime deleteAt;

}
