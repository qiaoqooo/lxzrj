package org.example.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.userservice.entity.ResumeAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeAttachmentService extends IService<ResumeAttachment> {

    /**
     * 当前用户上传附件简历
     */
    ResumeAttachment uploadAttachment(Long userId, MultipartFile file);

    /**
     * 查询当前用户的所有附件简历
     */
    List<ResumeAttachment> listByUser(Long userId);

    /**
     * 删除指定附件简历（同时尝试删除物理文件）
     */
    boolean deleteByUser(Long userId, Long id);

    /**
     * 重命名附件简历显示名称
     */
    boolean renameByUser(Long userId, Long id, String newName);
}


