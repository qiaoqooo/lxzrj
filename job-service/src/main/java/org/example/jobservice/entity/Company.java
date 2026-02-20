package org.example.jobservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("companies")
public class Company extends BaseEntity {

    private String name;
    private String logoUrl;
    private String city;
    private String district;
    private String address;
    private String financingStage;
    private String staffSize;
    private String industry;
    private String homepageUrl;
    private String description;

    /**
     * 公司归属的企业用户（招聘者）ID
     */
    @TableField("owner_user_id")
    private Long ownerUserId;
}
