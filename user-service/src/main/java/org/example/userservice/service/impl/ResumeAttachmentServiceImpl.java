package org.example.userservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.userservice.entity.ResumeAttachment;
import org.example.userservice.mapper.ResumeAttachmentMapper;
import org.example.userservice.service.ResumeAttachmentService;
import org.example.userservice.util.PdfPreviewUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResumeAttachmentServiceImpl extends ServiceImpl<ResumeAttachmentMapper, ResumeAttachment> implements ResumeAttachmentService {

    /**
     * 附件简历保存目录（相对或绝对路径均可）
     */
    @Value("${upload.resume-dir:uploads/resume}")
    private String resumeDir;

    /**
     * PDF 预览图保存目录（相对或绝对路径），默认在简历目录下的 preview 子目录
     */
    @Value("${upload.resume-preview-dir:uploads/resume/preview}")
    private String resumePreviewDir;

    @Override
    public ResumeAttachment uploadAttachment(Long userId, MultipartFile file) {
        if (userId == null) {
            throw new IllegalArgumentException("未登录或用户ID为空");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 原始文件名和后缀
        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (StringUtils.hasText(originalFilename) && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成唯一文件名
        String newFileName = userId + "_" + UUID.randomUUID().toString().replace("-", "") + ext;

        // 解析上传目录：
        // 1. 如果配置的是绝对路径，直接使用；
        // 2. 如果是相对路径（默认 uploads/resume），则相对于项目工作目录 user.dir
        File configuredDir = new File(resumeDir);
        File dir;
        if (configuredDir.isAbsolute()) {
            dir = configuredDir;
        } else {
            String baseDir = System.getProperty("user.dir");
            dir = new File(baseDir, resumeDir);
        }

        // 确保目录存在（如果不存在则自动创建）
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("创建上传目录失败: " + resumeDir);
        }

        File dest = new File(dir, newFileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("保存附件简历失败: " + e.getMessage(), e);
        }

        // 如果是 PDF，尝试生成预览图（第一页 PNG）
        String previewUrl = null;
        if (".pdf".equalsIgnoreCase(ext)) {
            File previewBaseDir;
            File configuredPreviewDir = new File(resumePreviewDir);
            if (configuredPreviewDir.isAbsolute()) {
                previewBaseDir = configuredPreviewDir;
            } else {
                String baseDir = System.getProperty("user.dir");
                previewBaseDir = new File(baseDir, resumePreviewDir);
            }
            // 预览文件名：与原文件同前缀，增加 _preview
            String baseName = newFileName;
            int dotIndex = newFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                baseName = newFileName.substring(0, dotIndex);
            }
            File previewFile = PdfPreviewUtil.generateFirstPagePreview(dest, previewBaseDir, baseName + "_preview");
            if (previewFile != null) {
                // 与 WebConfig 中 /uploads/** 映射保持一致
                String relativePath = previewFile.getPath().replace("\\", "/");
                int idx = relativePath.indexOf("uploads/");
                if (idx >= 0) {
                    previewUrl = "/" + relativePath.substring(idx);
                }
            }
        }

        // 构造数据库记录
        ResumeAttachment attachment = new ResumeAttachment();
        attachment.setUserId(userId);
        attachment.setFileName(StringUtils.hasText(originalFilename) ? originalFilename : newFileName);
        attachment.setFileUrl("/uploads/resume/" + newFileName);
        attachment.setFileSize((int) file.getSize());
        attachment.setCreatedAt(LocalDateTime.now());
        attachment.setPreviewImageUrl(previewUrl);

        this.save(attachment);
        return attachment;
    }

    @Override
    public List<ResumeAttachment> listByUser(Long userId) {
        return this.lambdaQuery()
                .eq(ResumeAttachment::getUserId, userId)
                .orderByDesc(ResumeAttachment::getCreatedAt)
                .list();
    }

    @Override
    public boolean deleteByUser(Long userId, Long id) {
        ResumeAttachment attachment = this.getById(id);
        if (attachment == null || !userId.equals(attachment.getUserId())) {
            return false;
        }

        // 先删物理文件（忽略失败）
        try {
            String url = attachment.getFileUrl(); // 形式：/uploads/resume/xxx.ext
            if (StringUtils.hasText(url) && url.contains("/")) {
                String fileName = url.substring(url.lastIndexOf('/') + 1);
                File file = new File(resumeDir, fileName);
                if (file.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        } catch (Exception ignored) {
        }

        return this.removeById(id);
    }

    @Override
    public boolean renameByUser(Long userId, Long id, String newName) {
        if (!StringUtils.hasText(newName)) {
            return false;
        }
        ResumeAttachment attachment = this.getById(id);
        if (attachment == null || !userId.equals(attachment.getUserId())) {
            return false;
        }
        attachment.setFileName(newName);
        return this.updateById(attachment);
    }
}


