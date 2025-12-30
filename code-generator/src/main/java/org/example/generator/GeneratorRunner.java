package org.example.generator;

/**
 * 代码生成器运行类
 * 直接运行此类的 main 方法即可生成代码
 * 
 * 使用方式：
 * 1. 修改下面的 MODULE_NAME、TABLE_NAMES 和 SUPER_ENTITY_CLASS 变量
 * 2. 运行 main 方法
 * 
 * SUPER_ENTITY_CLASS 可选值：
 * - null 或 ""：不继承基类
 * - "BaseEntity"：继承 BaseEntity（包含 id、createTime、createBy、updateTime、updateBy）
 * - "ComplexEntity"：继承 ComplexEntity（继承 BaseEntity，额外包含 code、version、remark、delFlag）
 * 
 * @author 小熊敲敲
 */
public class GeneratorRunner {

    public static void main(String[] args) {
        // ==================== 方式一：直接配置（推荐） ====================
        // 修改这里的配置即可
        String MODULE_NAME = "userservice";  // 模块名，如：user、order、product等
        String TABLE_NAMES = "user";    // 要生成的表名，多个用逗号分隔
        
        // 继承的基类类型（可选值：null、""、"BaseEntity"、"ComplexEntity"）
        // null 或 ""：不继承任何类（生成的类不写 extends 语句，只实现 Serializable）
        // "BaseEntity"：继承 BaseEntity
        // "ComplexEntity"：继承 ComplexEntity（推荐，包含 code 自动生成等功能）
        String SUPER_ENTITY_CLASS = "ComplexEntity";
        
        // 执行生成
        CodeGeneratorMain.execute(MODULE_NAME, TABLE_NAMES, SUPER_ENTITY_CLASS);
        
        // ==================== 方式二：交互式输入（取消下面的注释使用） ====================
        // String moduleName = CodeGeneratorMain.scanner("模块名");
        // String tableNames = CodeGeneratorMain.scanner("表名，多个英文逗号分割");
        // System.out.println("请选择继承的基类：");
        // System.out.println("1. 不继承（直接回车）");
        // System.out.println("2. BaseEntity");
        // System.out.println("3. ComplexEntity");
        // String superEntityChoice = CodeGeneratorMain.scanner("请输入选项（1/2/3）或直接回车");
        // String superEntityClass = null;
        // if ("2".equals(superEntityChoice)) {
        //     superEntityClass = "BaseEntity";
        // } else if ("3".equals(superEntityChoice)) {
        //     superEntityClass = "ComplexEntity";
        // }
        // // 如果选择1或直接回车，superEntityClass 为 null，不继承任何类
        // CodeGeneratorMain.execute(moduleName, tableNames, superEntityClass);
    }
}

