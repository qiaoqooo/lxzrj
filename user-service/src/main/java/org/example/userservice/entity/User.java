package org.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * <p>
 * 用户表：与 boss_employment.users 完全对齐
 * </p>
 *
 * @author 用户
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
@ApiModel(value = "User对象", description = "用户表：求职者/招聘者")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("小程序 openid 或第三方 id")
    @TableField("openid")
    private String openId;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("密码哈希（如有自建登录时使用）")
    @TableField("password_hash")
    private String passwordHash;

    @ApiModelProperty("昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("头像")
    @TableField("avatar_url")
    private String avatarUrl;

    @ApiModelProperty("真实姓名")
    @TableField("real_name")
    private String realName;

    @ApiModelProperty("角色：seeker / recruiter / admin")
    @TableField("role")
    private String role;

    @ApiModelProperty("性别：0-未知 1-男 2-女")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty("城市")
    @TableField("city")
    private String city;
}

