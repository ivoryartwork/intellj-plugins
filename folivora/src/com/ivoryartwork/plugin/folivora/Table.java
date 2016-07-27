package com.ivoryartwork.plugin.folivora;

import java.util.List;

/**
 * Created by Yaochao on 2016/7/25.
 */
public class Table {

    private List<Column> columns;

    /**
     * 表名字
     */
    private String name;

    /**
     * 主键名
     */
    private String primaryKey;

    /**
     * 主键是否自增
     */
    private boolean autoIncrement;

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
