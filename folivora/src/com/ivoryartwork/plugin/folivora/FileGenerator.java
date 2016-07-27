package com.ivoryartwork.plugin.folivora;

import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * Created by Yaochao on 2016/7/26.
 */
public class FileGenerator {

    private static Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    String classStruct = "PACKAGE_NAME\n" +
            "IMPORT" +
            "/**\n" +
            " * CLASS_DESCRIPTION\n" +
            " */\n" +
            "public CLASS_TYPE CLASS_NAME {\n" +
            "\n" +
            "CLASS_CONTENT\n" +
            "}";
    String modelClassContentFields = "    /**\n" +
            "     * COLUMN_DESCRIPTION\n" +
            "     */\n" +
            "    private COLUMN_TYPE COLUMN_NAME;\n\n";

    String modelClassContentGetterSetter = "    public COLUMN_TYPE getCOLUMN_U() {\n" +
            "        return COLUMN_NAME;\n" +
            "    }\n" +
            "\n" +
            "    public void setCOLUMN_U(COLUMN_TYPE COLUMN_NAME) {\n" +
            "        this.COLUMN_NAME = COLUMN_NAME;\n" +
            "    }\n\n";

    String mapperContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<!DOCTYPE mapper\n" +
            "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
            "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
            "<mapper namespace=\"\">\n" +
            "    MAPPER_CONTENT\n" +
            "</mapper>";

    /**
     * 生成model类文件
     *
     * @param table
     * @param parent
     * @param className
     */
    public void generateModelClass(Table table, VirtualFile parent, String className) throws IOException {
        VirtualFile modelFile = parent.createChildData(this, className + ".java");
        modelFile.setCharset(CHARSET_UTF8);
        //获取包名
        String packageName = getClassPackageName(parent.getCanonicalPath());
        //获取import的包
        String importLib = getModelClassImport(table);
        String classDes = "数据库表" + table.getName() + "对应的实体类";
        String cs = getClassStruct(packageName, importLib, classDes, "class", className);
        //获取实体类的内容
        String classContent = getModelClassContent(table);
        cs = cs.replace("CLASS_CONTENT", classContent);
        modelFile.setBinaryContent(cs.getBytes(CHARSET_UTF8));
    }

    /**
     * 生成mybatis的mapper配置文件
     *
     * @param table
     * @param parent
     * @param className
     * @throws IOException
     */
    public void generateMapperXml(Table table, VirtualFile parent, String className) throws IOException {
        VirtualFile xmlFile = parent.createChildData(this, className.toLowerCase() + "-mapper.xml");
        xmlFile.setCharset(CHARSET_UTF8);
        //
        String content = getMapperXmlContent(table, className);
        xmlFile.setBinaryContent(content.getBytes(CHARSET_UTF8));
    }

    /**
     * 获取mybatis的mapper内容
     *
     * @param table
     * @param className
     * @return
     */
    private String getMapperXmlContent(Table table, String className) {
        String lowClassName = className.toLowerCase();
        String columnList = "";
        String valuesList = "";
        List<Column> columns = table.getColumns();
        for (Column column : columns) {
            columnList += "," + column.getName();
            valuesList += ",#{" + column.getName() + "}";
        }
        columnList = columnList.substring(1);
        valuesList = valuesList.substring(1);
        String sqlDefine = "<sql id=\"" + lowClassName + "_base_column_list\">\n" +
                "      " + columnList + "\n" +
                "    </sql>\n" +
                "    <sql id=\"" + lowClassName + "_table_name\">\n" +
                "      " + table.getName() + "\n" +
                "    </sql>\n\n";
        String keyGen = "";
        if (table.isAutoIncrement() && table.getPrimaryKey() != null) {
            keyGen = " useGeneratedKeys=\"true\" keyProperty=\"" + table.getPrimaryKey() + "\"";
        }
        String saveSqlDefine = "    <insert id=\"save\" parameterType=\"" + className + "\"" + keyGen + ">\n" +
                "        INSERT INTO<include refid=\"" + lowClassName + "_table_name\"/>(<include refid=\"" + lowClassName + "_base_column_list\"/>)\n" +
                "        VALUES(" + valuesList + ")\n" +
                "    </insert>\n\n";

        return mapperContent.replace("MAPPER_CONTENT", sqlDefine + saveSqlDefine);
    }

    /**
     * 获取实体类内容
     *
     * @param table
     * @return
     */
    private String getModelClassContent(Table table) {
        String contentFields = "";
        String contentGetSetMethod = "";
        List<Column> columns = table.getColumns();
        for (Column column : columns) {
            String columnName = column.getName();
            String columnNameU = getGetterSetterName(columnName);
            String columnDes = "对应数据库字段" + column.getName();
            if (column.isNullable()) {
                columnDes += ",可以为空";
            } else {
                columnDes += ",不能为空";
            }
            String columnType = column.getJavaDataType().getSimpleName();
            contentFields += modelClassContentFields.replace("COLUMN_DESCRIPTION", columnDes).replace("COLUMN_TYPE", columnType).
                    replace("COLUMN_NAME", columnName);
            contentGetSetMethod += modelClassContentGetterSetter.replace("COLUMN_TYPE", columnType).
                    replace("COLUMN_NAME", columnName).replace("COLUMN_U", columnNameU);
        }
        return contentFields + contentGetSetMethod;
    }

    /**
     * 根据字段名获取get set方法的名称
     *
     * @param name
     * @return
     */
    private String getGetterSetterName(String name) {
        if (name.length() > 1) {
            int c0 = name.charAt(0);
            int c1 = name.charAt(1);
            if (c0 >= 97 && c0 <= 122 && c1 >= 65 && c1 <= 90) {
                return name;
            }
        }
        return name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
    }

    /**
     * 获取类的结构
     *
     * @param packageName 包
     * @param importLib   导入类
     * @param classDes    类描述
     * @param classType   类类型
     * @param className   类名称
     * @return
     */
    private String getClassStruct(String packageName, String importLib, String classDes, String classType, String className) {
        return classStruct.replace("PACKAGE_NAME", packageName).replace("IMPORT", importLib).replace("CLASS_DESCRIPTION", classDes).
                replace("CLASS_TYPE", classType).replace("CLASS_NAME", className);
    }

    /**
     * 获取实体类需要导入的包
     *
     * @param table
     * @return
     */
    private String getModelClassImport(Table table) {
        String improtLib = "\n";
        List<Column> columns = table.getColumns();
        for (Column column : columns) {
            if (column.getJavaDataType().equals(BigDecimal.class)) {
                improtLib += "import java.math.BigDecimal;\n";
            }
            if (column.getJavaDataType().equals(Date.class)) {
                improtLib += "import java.util.Date;\n";
            }
        }
        return improtLib;
    }

    /**
     * 获取class的包名
     *
     * @param path 文件路径
     * @return
     */
    private String getClassPackageName(String path) {
        String packageName = "";
        path = path.substring(path.indexOf("src") + 3, path.length());
        if (path.startsWith("/main/java")) {
            path = path.substring(10, path.length());
        }
        if (path.length() > 0) {
            String[] subPaths = path.split("/");
            for (int i = 1; i < subPaths.length; i++) {
                packageName += "." + subPaths[i];
            }
            packageName = "package " + packageName.substring(1) + ";";
        }
        return packageName;
    }

}
