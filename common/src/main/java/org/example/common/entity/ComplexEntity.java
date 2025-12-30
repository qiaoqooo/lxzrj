package org.example.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 复杂实体类
 * 继承 BaseEntity，包含业务字段：code、version、remark、delFlag
 * 
 * @author common
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComplexEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编码（自动生成）")
    @TableField(fill = FieldFill.INSERT)
    private String code;

    @ApiModelProperty("版本号（乐观锁）")
    @Version
    private Long version;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("删除标记：0-正常，1-删除（逻辑删除）")
    private Boolean delFlag;
}

