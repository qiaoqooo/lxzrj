package org.example.userservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录请求 DTO
 * 
 * @author user-service
 */
@Data
@ApiModel("登录请求")
public class LoginRequest {

    @ApiModelProperty(value = "微信小程序登录 code", required = true)
    private String code;

    @ApiModelProperty("用户昵称（首次登录时传入）")
    private String nickName;

    @ApiModelProperty("用户头像URL（首次登录时传入）")
    private String avatarUrl;
}

