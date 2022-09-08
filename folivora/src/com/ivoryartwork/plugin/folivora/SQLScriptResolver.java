package com.ivoryartwork.plugin.folivora;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yaochao on 2016/7/25.
 */
public class SQLScriptResolver {

    private static HashMap<String, Class> mysqlDataTypeToJavaDataType;

    static {
        mysqlDataTypeToJavaDataType = new HashMap<>();
        mysqlDataTypeToJavaDataType.put("tinyint", int.class);
        mysqlDataTypeToJavaDataType.put("smallint", int.class);
        mysqlDataTypeToJavaDataType.put("int", int.class);
        mysqlDataTypeToJavaDataType.put("bigint", long.class);
        mysqlDataTypeToJavaDataType.put("float", float.class);
        mysqlDataTypeToJavaDataType.put("double", double.class);
        mysqlDataTypeToJavaDataType.put("decimal", BigDecimal.class);
        mysqlDataTypeToJavaDataType.put("varchar", String.class);
        mysqlDataTypeToJavaDataType.put("char", String.class);
        mysqlDataTypeToJavaDataType.put("date", Date.class);
        mysqlDataTypeToJavaDataType.put("time", Date.class);
        mysqlDataTypeToJavaDataType.put("datetime", Date.class);
        mysqlDataTypeToJavaDataType.put("tinytext", String.class);
        mysqlDataTypeToJavaDataType.put("text", String.class);
        mysqlDataTypeToJavaDataType.put("mediumtext", String.class);
        mysqlDataTypeToJavaDataType.put("longtext", String.class);
        mysqlDataTypeToJavaDataType.put("tinyblob", byte[].class);
        mysqlDataTypeToJavaDataType.put("blob", byte[].class);
        mysqlDataTypeToJavaDataType.put("mediumblob", byte[].class);
        mysqlDataTypeToJavaDataType.put("longblob", byte[].class);
    }

    String getTableName(String createSQLScript) {
        String reg = "CREATE TABLE `([a-zA-Z0-9_]{1,})`";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(createSQLScript);
        if (matcher.find()) {
            String[] tp = matcher.group().split(" ");
            if (tp.length == 3) {
                return tp[2].substring(1, tp[2].length() - 1);
            }
        }
        return null;
    }

    /**
     * 根据数据库表创建语句解析出列属性
     *
     * @param createSQLScript
     * @return
     */
    Table getTable(String createSQLScript) {
        String tableName = getTableName(createSQLScript);
        String reg = "`[a-zA-Z0-9_]+` [a-zA-Z0-9)\\(\\)]+";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(createSQLScript);
        Table table = new Table();
        table.setName(tableName);
        List<Column> columns = new ArrayList<>();
        while (matcher.find()) {
            String[] tp = matcher.group().split(" ");
            if (tp.length == 2) {
                Column column = new Column();
                column.setName(tp[0].substring(1, tp[0].length() - 1));
                int m = tp[1].indexOf("(");
                if (m > 0) {
                    tp[1] = tp[1].substring(0, m);
                }
                column.setDataType(tp[1]);
                if (column.getDataType() != null) {
                    column.setJavaDataType(mysqlDataTypeToJavaDataType.get(column.getDataType()));
                }
                columns.add(column);
            }
        }
        table.setColumns(columns);
        reg = "PRIMARY KEY \\(`([a-zA-Z0-9_]{1,})`\\)";
        pattern = Pattern.compile(reg);
        matcher = pattern.matcher(createSQLScript);
        if (matcher.find()) {
            table.setPrimaryKey(matcher.group(1));
        }
        reg = "AUTO_INCREMENT";
        pattern = Pattern.compile(reg);
        matcher = pattern.matcher(createSQLScript);
        if (matcher.find()) {
            table.setAutoIncrement(true);
        }
        return table;
    }

    public static void main(String[] args) {
        String sql = "CREATE TABLE `wxw_room_livecode`  (\n" +
                "  `codeId` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',\n" +
                "  `codeName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群活码名称',\n" +
                "  `showOrder` tinyint(4) NOT NULL COMMENT '活码展示顺序。1：到上限后切换，2：顺序轮换',\n" +
                "  `preventRepeatIn` tinyint(4) NOT NULL COMMENT '防重复入群。0：关闭，1：开启',\n" +
                "  `codeLogoUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业Logo图片url',\n" +
                "  `codeGrouping` tinyint(4) NOT NULL COMMENT '群分组。0：无分组，1：有分组',\n" +
                "  `groupPageConf` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '分组页面配置',\n" +
                "  `middlePageType` tinyint(4) NOT NULL COMMENT '中间页页面模式。1：标准模式，2：海报模式',\n" +
                "  `middlePageTitle` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '中间页页面标题',\n" +
                "  `middlePageConf` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '中间页页面配置',\n" +
                "  `userId` bigint(20) NOT NULL COMMENT '账户id',\n" +
                "  `active` tinyint(4) NOT NULL COMMENT '0：不可用，1：可用',\n" +
                "  `status` tinyint(4) NOT NULL COMMENT '活码状态，0：暂停，1：开启',\n" +
                "  `createTime` datetime(0) NOT NULL COMMENT '活码创建时间',\n" +
                "  `closeTime` datetime(0) NULL DEFAULT NULL COMMENT '关闭时间，每次更新',\n" +
                "  PRIMARY KEY (`codeId`) USING BTREE,\n" +
                "  INDEX `lcode_u`(`userId`, `status`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '群活码' ROW_FORMAT = Dynamic;";
        String reg = "PRIMARY KEY \\(`([a-zA-Z0-9_]{1,})`\\)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
        SQLScriptResolver sqlScriptResolver = new SQLScriptResolver();
        sqlScriptResolver.getTable(sql);
    }
}
