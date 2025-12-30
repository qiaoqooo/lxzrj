package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.ComplexEntity;

import java.time.LocalDateTime;

/**
 * <p>
 * 小程序用户表
 * </p>
 *
 * @author 小熊敲敲
 * @since 2025-12-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
@ApiModel(value = "User对象", description = "小程序用户表")
public class User extends ComplexEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("微信小程序openid")
    private String openId;

    @ApiModelProperty("微信开放平台unionid")
    private String unionId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户头像URL")
    private String avatarUrl;

    @ApiModelProperty("性别：0-未知，1-男，2-女")
    private Boolean gender;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("语言")
    private String language;

    @ApiModelProperty("微信会话密钥（加密存储）")
    private String sessionKey;

    @ApiModelProperty("手机号（用户授权后获取）")
    private String phoneNumber;

    @ApiModelProperty("是否禁用：0-正常，1-禁用")
    private Boolean isDisabled;

    @ApiModelProperty("最后登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("白名单")
    private Boolean isIgnore;
}

