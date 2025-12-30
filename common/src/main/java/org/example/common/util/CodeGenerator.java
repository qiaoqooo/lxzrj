package org.example.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 编码生成器工具类
 * 用于生成业务编码，规则：表名首字母 + 日期(yyyyMMdd) + 序号(3位)
 * 例如：user -> U20251230001, shop_mark -> SM20251230001
 * 
 * @author common
 */
public class CodeGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 根据表名生成编码前缀
     * 规则：提取表名中每个单词的首字母（大写）
     * 例如：user -> U, shop_mark -> SM, user_order -> UO
     * 
     * @param tableName 表名
     * @return 编码前缀
     */
    public static String generatePrefix(String tableName) {
        if (tableName == null || tableName.trim().isEmpty()) {
            return "";
        }
        
        // 移除表前缀（如 t_, c_ 等）
        String name = tableName;
        if (name.startsWith("t_")) {
            name = name.substring(2);
        } else if (name.startsWith("c_")) {
            name = name.substring(2);
        }
        
        // 按 _ 分割，取每个单词的首字母
        String[] parts = name.split("_");
        StringBuilder prefix = new StringBuilder();
        
        for (String part : parts) {
            if (!part.isEmpty()) {
                prefix.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        
        return prefix.toString();
    }

    /**
     * 生成完整的编码
     * 格式：前缀 + 日期(yyyyMMdd) + 序号(3位，需要外部传入)
     * 
     * @param tableName 表名
     * @param sequence 序号（当天的序号，从1开始）
     * @return 完整编码
     */
    public static String generateCode(String tableName, int sequence) {
        String prefix = generatePrefix(tableName);
        String date = LocalDate.now().format(DATE_FORMATTER);
        String sequenceStr = String.format("%03d", sequence);
        return prefix + date + sequenceStr;
    }

    /**
     * 生成完整的编码（使用当前日期）
     * 
     * @param tableName 表名
     * @param sequence 序号
     * @return 完整编码
     */
    public static String generateCodeWithDate(String tableName, int sequence, LocalDate date) {
        String prefix = generatePrefix(tableName);
        String dateStr = date.format(DATE_FORMATTER);
        String sequenceStr = String.format("%03d", sequence);
        return prefix + dateStr + sequenceStr;
    }
}

