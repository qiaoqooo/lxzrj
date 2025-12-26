package org.example.generator;

/**
 * 代码生成器运行类
 * 直接运行此类的 main 方法即可生成代码
 * 
 * 使用方式：
 * 1. 修改下面的 MODULE_NAME 和 TABLE_NAMES 变量
 * 2. 运行 main 方法
 * 
 * @author code-generator
 */
public class GeneratorRunner {

    public static void main(String[] args) {
        // ==================== 方式一：直接配置（推荐） ====================
        // 修改这里的配置即可
        String MODULE_NAME = "userservice";  // 模块名，如：user、order、product等
        String TABLE_NAMES = "user";    // 要生成的表名，多个用逗号分隔
        
        // 执行生成
        CodeGeneratorMain.execute(MODULE_NAME, TABLE_NAMES);
        
        // ==================== 方式二：交互式输入（取消下面的注释使用） ====================
        // String moduleName = CodeGeneratorMain.scanner("模块名");
        // String tableNames = CodeGeneratorMain.scanner("表名，多个英文逗号分割");
        // CodeGeneratorMain.execute(moduleName, tableNames);
    }
}

