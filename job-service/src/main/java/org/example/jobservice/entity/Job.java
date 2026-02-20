package org.example.jobservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jobs")
public class Job extends BaseEntity {

    private Long companyId;
    private Long recruiterId;
    private String title;
    private Integer salaryMin;
    private Integer salaryMax;
    private String city;
    private String district;
    private String workPlace;
    private String experienceReq;
    private String degreeReq;
    private String jobType;
    private String description;
    private String status;
}
