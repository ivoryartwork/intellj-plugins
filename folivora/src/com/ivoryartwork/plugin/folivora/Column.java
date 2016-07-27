package com.ivoryartwork.plugin.folivora;

/**
 * Created by Yaochao on 2016/7/25.
 */
public class Column {

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段数据类型
     */
    private String dataType;

    /**
     * 字段数据类型对应的java数据类型
     */
    private Class javaDataType;

    /**
     * 是否为null
     */
    private boolean nullable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Class getJavaDataType() {
        return javaDataType;
    }

    public void setJavaDataType(Class javaDataType) {
        this.javaDataType = javaDataType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
