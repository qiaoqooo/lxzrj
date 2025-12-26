package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 小程序用户表
 * </p>
 *
 * @author code-generator
 * @since 2025-12-26
 */
@Getter
@Setter
@ApiModel(value = "User对象", description = "小程序用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("微信小程序openid")
    private String openid;

    @ApiModelProperty("微信开放平台unionid")
    private String unionid;

    @ApiModelProperty("用户昵称")
    private String nickname;

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

    @ApiModelProperty("删除标记：0-正常，1-删除（逻辑删除）")
    private Boolean delFlag;

    @ApiModelProperty("版本号（乐观锁）")
    private Long version;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

