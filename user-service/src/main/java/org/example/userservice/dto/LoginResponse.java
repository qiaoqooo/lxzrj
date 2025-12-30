package org.example.userservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.example.userservice.entity.User;

/**
 * 登录响应 DTO
 * 
 * @author user-service
 */
@Data
@ApiModel("登录响应")
public class LoginResponse {

    @ApiModelProperty("JWT Token")
    private String token;

    @ApiModelProperty("用户信息")
    private User user;

    @ApiModelProperty("是否新用户（首次注册）")
    private Boolean isNewUser;

    public LoginResponse() {
    }

    public LoginResponse(String token, User user, Boolean isNewUser) {
        this.token = token;
        this.user = user;
        this.isNewUser = isNewUser;
    }
}

