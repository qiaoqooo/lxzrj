package org.example.common.handler;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.example.common.util.CodeGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * MyBatis-Plus 自动填充处理器
 * 用于自动填充创建时间、更新时间、创建人、更新人、code等字段
 * 
 * @author common
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    /**
     * 插入时的填充策略
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间
        this.strictInsertFill(metaObject, "createTime", java.time.LocalDateTime.class, java.time.LocalDateTime.now());
        
        // 自动填充创建人
        String currentUser = getCurrentUser();
        this.strictInsertFill(metaObject, "createBy", String.class, currentUser);
        
        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updateTime", java.time.LocalDateTime.class, java.time.LocalDateTime.now());
        
        // 自动填充更新人
        this.strictInsertFill(metaObject, "updateBy", String.class, currentUser);

        // 自动生成 code（如果是 ComplexEntity 或其子类）
        if (hasField(metaObject, "code")) {
            String code = generateCode(metaObject);
            if (code != null) {
                this.strictInsertFill(metaObject, "code", String.class, code);
            }
        }
    }

    /**
     * 更新时的填充策略
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", java.time.LocalDateTime.class, java.time.LocalDateTime.now());
        
        // 自动填充更新人
        String currentUser = getCurrentUser();
        this.strictUpdateFill(metaObject, "updateBy", String.class, currentUser);
    }

    /**
     * 生成 code
     */
    private String generateCode(MetaObject metaObject) {
        try {
            // 获取表名
            String tableName = getTableName(metaObject);
            if (tableName == null) {
                return null;
            }

            // 获取当天日期
            String dateStr = LocalDate.now().format(DATE_FORMATTER);
            
            // 查询当天最大序号
            int maxSequence = getMaxSequence(tableName, dateStr);
            
            // 生成下一个序号
            int nextSequence = maxSequence + 1;
            
            // 生成完整 code
            return CodeGenerator.generateCode(tableName, nextSequence);
        } catch (Exception e) {
            // 如果生成失败，返回 null（不自动填充）
            return null;
        }
    }

    /**
     * 获取表名
     */
    private String getTableName(MetaObject metaObject) {
        try {
            Object originalObject = metaObject.getOriginalObject();
            if (originalObject == null) {
                return null;
            }

            Class<?> clazz = originalObject.getClass();
            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            if (tableNameAnnotation != null && !tableNameAnnotation.value().isEmpty()) {
                return tableNameAnnotation.value();
            }

            // 如果没有 @TableName 注解，从类名推断
            String className = clazz.getSimpleName();
            return camelToUnderscore(className);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询当天最大序号
     */
    private int getMaxSequence(String tableName, String dateStr) {
        if (jdbcTemplate == null) {
            return 0; // 如果没有 JdbcTemplate，返回0
        }

        try {
            // 验证表名安全性（只允许字母、数字、下划线）
            if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
                return 0; // 表名不合法，返回0
            }

            // 生成 code 前缀（用于查询）
            String prefix = CodeGenerator.generatePrefix(tableName);
            String codePrefix = prefix + dateStr;

            // 查询当天最大的 code
            // 注意：表名不能作为参数，但已经通过正则验证，相对安全
            String sql = "SELECT MAX(CAST(SUBSTRING(code, " + (codePrefix.length() + 1) + ") AS UNSIGNED)) " +
                        "FROM `" + tableName + "` " +
                        "WHERE code LIKE ?";
            
            Integer maxSeq = jdbcTemplate.queryForObject(
                sql, 
                Integer.class, 
                codePrefix + "%"  // 匹配当天所有 code
            );

            return maxSeq != null ? maxSeq : 0;
        } catch (Exception e) {
            // 查询失败，返回0（从1开始）
            return 0;
        }
    }

    /**
     * 检查字段是否存在
     */
    private boolean hasField(MetaObject metaObject, String fieldName) {
        try {
            return metaObject.hasGetter(fieldName);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 驼峰转下划线
     */
    private String camelToUnderscore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                result.append('_');
            }
            result.append(Character.toLowerCase(c));
        }
        return result.toString();
    }

    /**
     * 获取当前用户
     * 可以从 JWT Token、Session 或 ThreadLocal 中获取
     * 
     * @return 当前用户标识（可以是用户ID、openid等）
     */
    private String getCurrentUser() {
        // TODO: 从请求上下文获取当前用户信息
        // 例如：从 JWT Token 中解析用户ID或openid
        // 或者从 ThreadLocal 中获取（需要在拦截器中设置）
        return "system"; // 默认值，后续可以改为从请求中获取
    }
}

