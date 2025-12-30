package org.example.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;
import java.util.Scanner;

/**
 * 代码生成器主类
 * 直接运行此类的 main 方法即可生成代码
 * 
 * @author code-generator
 */
public class CodeGeneratorMain {

    // ==================== 配置区域 ====================
    // 数据库配置
    private static final String DB_URL = "jdbc:mysql://localhost:3306/clouddata?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    
    // 代码生成配置
    private static final String AUTHOR = "code-generator";
    private static final String PARENT_PACKAGE = "org.example";
    private static final String OUTPUT_DIR = "src/main/java";
    
    // ==================== 配置区域结束 ====================

    /**
     * 读取控制台输入内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (ipt != null && !ipt.trim().isEmpty()) {
                return ipt;
            }
        }
        throw new RuntimeException("请输入正确的" + tip + "！");
    }

    /**
     * 执行代码生成（重载方法，不继承基类）
     * 
     * @param moduleName 模块名（如：user、order等）
     * @param tableNames 表名，多个用逗号分隔（如：user,role,permission）
     */
    public static void execute(String moduleName, String tableNames) {
        execute(moduleName, tableNames, null);
    }

    /**
     * 执行代码生成
     * 
     * @param moduleName 模块名（如：user、order等）
     * @param tableNames 表名，多个用逗号分隔（如：user,role,permission）
     * @param superEntityClass 继承的基类（可选值：BaseEntity、ComplexEntity，为空则不继承）
     */
    public static void execute(String moduleName, String tableNames, String superEntityClass) {
        // 获取当前类所在路径，定位到 code-generator 模块
        String currentClassPath = CodeGeneratorMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        // 如果是编译后的路径，需要找到源码路径
        String projectPath = System.getProperty("user.dir");
        String codeGeneratorPath = projectPath + "/code-generator";
        
        // 输出目录：code-generator/src/main/java
        String outputDir = codeGeneratorPath + "/src/main/java";
        // Mapper XML 输出目录：code-generator/src/main/resources/mapper
        String mapperXmlPath = codeGeneratorPath + "/src/main/resources/mapper/" + moduleName;
        
        System.out.println("==========================================");
        System.out.println("      开始生成代码");
        System.out.println("==========================================");
        System.out.println("模块名：" + moduleName);
        System.out.println("表名：" + tableNames);
        System.out.println("继承基类：" + (superEntityClass != null && !superEntityClass.isEmpty() ? superEntityClass : "无（不继承）"));
        System.out.println("输出目录：" + outputDir);
        System.out.println("Mapper XML 目录：" + mapperXmlPath);
        System.out.println("==========================================");
        
        // 构建基类完整路径
        // 如果为 null 或空，则不继承任何类（不设置 superClass，让类默认只实现 Serializable）
        String superEntityClassFullPath = null;
        if (superEntityClass != null && !superEntityClass.trim().isEmpty()) {
            if ("BaseEntity".equals(superEntityClass) || "baseEntity".equals(superEntityClass)) {
                superEntityClassFullPath = "org.example.common.entity.BaseEntity";
            } else if ("ComplexEntity".equals(superEntityClass) || "complexEntity".equals(superEntityClass)) {
                superEntityClassFullPath = "org.example.common.entity.ComplexEntity";
            } else if ("Object".equals(superEntityClass) || "object".equals(superEntityClass)) {
                // 显式指定 Object 时，设置为 null（不继承，等同于不写 extends）
                superEntityClassFullPath = null;
            } else {
                // 如果传入的是完整路径，直接使用
                superEntityClassFullPath = superEntityClass;
            }
        }
        
        final String finalSuperEntityClass = superEntityClassFullPath;
        
        FastAutoGenerator.create(DB_URL, DB_USERNAME, DB_PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT_PACKAGE) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlPath)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames.split(",")) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .strategyConfig(builder -> {
                    // 构建基础配置
                    builder.entityBuilder()
                            .enableLombok() // 启用Lombok（使用@Data注解）
                            .enableFileOverride(); // 覆盖已生成文件
                    
                    // 如果指定了基类，设置继承
                    if (finalSuperEntityClass != null) {
                        builder.entityBuilder().superClass(finalSuperEntityClass);
                    }
                    // 如果不设置 superClass，生成的类将不写 extends 语句（默认实现 Serializable）
                    
                    // 根据基类类型设置不同的自动填充字段
                    if (finalSuperEntityClass != null && finalSuperEntityClass.contains("ComplexEntity")) {
                        // ComplexEntity 包含 code 字段，需要自动填充
                        builder.entityBuilder().addTableFills(
                                new Column("create_time", FieldFill.INSERT),
                                new Column("create_by", FieldFill.INSERT),
                                new Column("update_time", FieldFill.INSERT_UPDATE),
                                new Column("update_by", FieldFill.INSERT_UPDATE),
                                new Column("code", FieldFill.INSERT)
                        );
                    } else if (finalSuperEntityClass != null && finalSuperEntityClass.contains("BaseEntity")) {
                        // BaseEntity 不包含 code 字段
                        builder.entityBuilder().addTableFills(
                                new Column("create_time", FieldFill.INSERT),
                                new Column("create_by", FieldFill.INSERT),
                                new Column("update_time", FieldFill.INSERT_UPDATE),
                                new Column("update_by", FieldFill.INSERT_UPDATE)
                        );
                    } else {
                        // 不继承基类时，仍然可以设置自动填充（如果表中有这些字段）
                        builder.entityBuilder().addTableFills(
                                new Column("create_time", FieldFill.INSERT),
                                new Column("create_by", FieldFill.INSERT),
                                new Column("update_time", FieldFill.INSERT_UPDATE),
                                new Column("update_by", FieldFill.INSERT_UPDATE)
                        );
                    }
                })
                .strategyConfig(builder -> {
                    builder.controllerBuilder()
                            .enableHyphenStyle() // 开启驼峰转连字符
                            .enableRestStyle(); // 开启生成@RestController控制器
                })
                .strategyConfig(builder -> {
                    builder.serviceBuilder()
                            .formatServiceFileName("%sService") // 格式化 service 接口文件名称
                            .formatServiceImplFileName("%sServiceImpl"); // 格式化 service 实现类文件名称
                })
                .templateConfig(builder -> {
                    builder.entity("/templates/entity.java.ftl"); // 使用自定义实体类模板（使用@Data注解）
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用 Freemarker 模板引擎
                .execute();
        
        System.out.println("==========================================");
        System.out.println("      代码生成完成！");
        System.out.println("==========================================");
    }

}

