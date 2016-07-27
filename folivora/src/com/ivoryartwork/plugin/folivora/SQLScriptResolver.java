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
        mysqlDataTypeToJavaDataType.put("tinyint", byte.class);
        mysqlDataTypeToJavaDataType.put("smallint", short.class);
        mysqlDataTypeToJavaDataType.put("int", int.class);
        mysqlDataTypeToJavaDataType.put("bigint", long.class);
        mysqlDataTypeToJavaDataType.put("float", float.class);
        mysqlDataTypeToJavaDataType.put("double", double.class);
        mysqlDataTypeToJavaDataType.put("decimal", BigDecimal.class);
        mysqlDataTypeToJavaDataType.put("varchar", String.class);
        mysqlDataTypeToJavaDataType.put("char", char.class);
        mysqlDataTypeToJavaDataType.put("date", Date.class);
        mysqlDataTypeToJavaDataType.put("time", Date.class);
        mysqlDataTypeToJavaDataType.put("datetime", Date.class);
        mysqlDataTypeToJavaDataType.put("tinytext", String.class);
        mysqlDataTypeToJavaDataType.put("text", String.class);
        mysqlDataTypeToJavaDataType.put("mediumtext", String.class);
        mysqlDataTypeToJavaDataType.put("longtext", String.class);
        mysqlDataTypeToJavaDataType.put("longtext", String.class);
        mysqlDataTypeToJavaDataType.put("tinyblob", byte[].class);
        mysqlDataTypeToJavaDataType.put("blob", byte[].class);
        mysqlDataTypeToJavaDataType.put("mediumblob", byte[].class);
        mysqlDataTypeToJavaDataType.put("longblob", byte[].class);
    }

    /**
     * 根据数据库表创建语句解析出列属性
     *
     * @param createSQLScript
     * @return
     */
    Table getTable(String createSQLScript) {
        String reg = "`[a-zA-Z0-9_]+` [a-zA-Z0-9)\\(\\)]+ ([a-zA-Z]+ NULL)|CREATE TABLE `([a-zA-Z0-9_]{1,})`";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(createSQLScript);
        Table table = new Table();
        List<Column> columns = new ArrayList<>();
        while (matcher.find()) {
            String[] tp = matcher.group().split(" ");
            if (tp.length == 3) {
                table.setName(tp[2].substring(1, tp[2].length() - 1));
            } else if (tp.length == 4) {
                Column column = new Column();
                column.setName(tp[0].substring(1, tp[0].length() - 1));
                int m = tp[1].indexOf("(");
                if (m > 0) {
                    tp[1] = tp[1].substring(0, m);
                }
                column.setDataType(tp[1]);
                tp[2] = tp[2].toLowerCase();
                if (tp[2].equals("not")) {
                    column.setNullable(false);
                } else {
                    column.setNullable(true);
                }
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
        String sql = "CREATE TABLE `t_health_exam` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `number` bigint(20) NOT NULL,\n" +
                "  `userid` bigint(20) NOT NULL,\n" +
                "  `name` varchar(20) NOT NULL,\n" +
                "  `examinDate` datetime NOT NULL,\n" +
                "  `doctor` varchar(20) DEFAULT NULL,\n" +
                "  `symptom` varchar(100) DEFAULT NULL,\n" +
                "  `temperature` float DEFAULT NULL,\n" +
                "  `pulseRate` int(11) DEFAULT NULL,\n" +
                "  `breathRate` int(11) DEFAULT NULL,\n" +
                "  `bloodPressueLeftUp` float DEFAULT NULL,\n" +
                "  `bloodPressueLeftDown` float DEFAULT NULL,\n" +
                "  `bloodPressueRightUp` float DEFAULT NULL,\n" +
                "  `bloodPressueRightDown` float DEFAULT NULL,\n" +
                "  `hight` float DEFAULT NULL,\n" +
                "  `weight` float DEFAULT NULL,\n" +
                "  `waistLine` float DEFAULT NULL,\n" +
                "  `bmi` float DEFAULT NULL,\n" +
                "  `selfPhase` tinyint(4) DEFAULT NULL,\n" +
                "  `selfCater` tinyint(4) DEFAULT NULL,\n" +
                "  `cognition` tinyint(4) DEFAULT NULL,\n" +
                "  `MentalScore` float DEFAULT NULL,\n" +
                "  `emotion` tinyint(4) DEFAULT NULL,\n" +
                "  `depressionScore` float DEFAULT NULL,\n" +
                "  `exerciseFrequency` tinyint(4) DEFAULT NULL,\n" +
                "  `exerciseTime` float DEFAULT NULL,\n" +
                "  `exerciseYear` float DEFAULT NULL,\n" +
                "  `exerciseWay` varchar(20) DEFAULT NULL,\n" +
                "  `eat` varchar(20) DEFAULT NULL,\n" +
                "  `smoke` tinyint(4) DEFAULT NULL,\n" +
                "  `daySmokeNum` int(11) DEFAULT NULL,\n" +
                "  `startSmoke` int(11) DEFAULT NULL,\n" +
                "  `quitSmoke` int(11) DEFAULT NULL,\n" +
                "  `drinkFrequency` tinyint(4) DEFAULT NULL,\n" +
                "  `dayDrinkNum` float DEFAULT NULL,\n" +
                "  `quitDrink` tinyint(1) DEFAULT NULL,\n" +
                "  `quitDrinkAge` int(11) DEFAULT NULL,\n" +
                "  `startDrinkAge` int(11) DEFAULT NULL,\n" +
                "  `drunk` tinyint(1) DEFAULT NULL,\n" +
                "  `wineType` varchar(20) DEFAULT NULL,\n" +
                "  `occuDisease` tinyint(1) DEFAULT NULL,\n" +
                "  `lip` tinyint(4) DEFAULT NULL,\n" +
                "  `teeth` tinyint(4) DEFAULT NULL,\n" +
                "  `throat` tinyint(4) DEFAULT NULL,\n" +
                "  `leftEye` float DEFAULT NULL,\n" +
                "  `rightEye` float DEFAULT NULL,\n" +
                "  `adjustLeftEye` float DEFAULT NULL,\n" +
                "  `adjustRightEye` float DEFAULT NULL,\n" +
                "  `hear` tinyint(4) DEFAULT NULL,\n" +
                "  `sport` tinyint(4) DEFAULT NULL,\n" +
                "  `eyeGround` varchar(20) DEFAULT NULL,\n" +
                "  `skin` varchar(50) DEFAULT NULL,\n" +
                "  `sclera` varchar(30) DEFAULT NULL,\n" +
                "  `lymphaden` varchar(30) DEFAULT NULL,\n" +
                "  `lung` tinyint(1) DEFAULT NULL,\n" +
                "  `breathSound` varchar(20) DEFAULT NULL,\n" +
                "  `rales` varchar(20) DEFAULT NULL,\n" +
                "  `heartRate` int(11) DEFAULT NULL,\n" +
                "  `rhythm` tinyint(4) DEFAULT NULL,\n" +
                "  `noise` varchar(20) DEFAULT NULL,\n" +
                "  `bellyTender` varchar(20) DEFAULT NULL,\n" +
                "  `bellyMass` varchar(20) DEFAULT NULL,\n" +
                "  `bellyLiver` varchar(20) DEFAULT NULL,\n" +
                "  `bellySpleen` varchar(20) DEFAULT NULL,\n" +
                "  `bellyNoise` varchar(20) DEFAULT NULL,\n" +
                "  `legEdema` tinyint(4) DEFAULT NULL,\n" +
                "  `foot` tinyint(4) DEFAULT NULL,\n" +
                "  `anus` varchar(30) DEFAULT NULL,\n" +
                "  `breast` varchar(50) DEFAULT NULL,\n" +
                "  `vulva` varchar(30) DEFAULT NULL,\n" +
                "  `vagina` varchar(30) DEFAULT NULL,\n" +
                "  `cervical` varchar(30) DEFAULT NULL,\n" +
                "  `corpus` varchar(30) DEFAULT NULL,\n" +
                "  `appendix` varchar(30) DEFAULT NULL,\n" +
                "  `check_others` varchar(30) DEFAULT NULL,\n" +
                "  `hemoglobin` float DEFAULT NULL,\n" +
                "  `leukocyte` float DEFAULT NULL,\n" +
                "  `plateletplatelet` float DEFAULT NULL,\n" +
                "  `platelet` varchar(100) DEFAULT NULL,\n" +
                "  `bloodOthers` varchar(20) DEFAULT NULL,\n" +
                "  `urineProtein` varchar(20) DEFAULT NULL,\n" +
                "  `urineSugar` varchar(20) DEFAULT NULL,\n" +
                "  `urineKetone` varchar(20) DEFAULT NULL,\n" +
                "  `urineBld` varchar(20) DEFAULT NULL,\n" +
                "  `urineOthers` varchar(100) DEFAULT NULL,\n" +
                "  `fastingGlucose1` float DEFAULT NULL,\n" +
                "  `fastingGlucose2` float DEFAULT NULL,\n" +
                "  `cardiogram` varchar(30) DEFAULT NULL,\n" +
                "  `microalbumin` float DEFAULT NULL,\n" +
                "  `FOB` tinyint(4) DEFAULT NULL,\n" +
                "  `glyHemoglobin` float DEFAULT NULL,\n" +
                "  `hbsAntigen` tinyint(4) DEFAULT NULL,\n" +
                "  `SGPT` float DEFAULT NULL,\n" +
                "  `SGOT` float DEFAULT NULL,\n" +
                "  `albumin` float DEFAULT NULL,\n" +
                "  `bilirubin` float DEFAULT NULL,\n" +
                "  `conjugatedBilirubin` float DEFAULT NULL,\n" +
                "  `ScR` float DEFAULT NULL,\n" +
                "  `BuN` float DEFAULT NULL,\n" +
                "  `SpC` float DEFAULT NULL,\n" +
                "  `SsC` float DEFAULT NULL,\n" +
                "  `cholesterol` float DEFAULT NULL,\n" +
                "  `triglyceride` float DEFAULT NULL,\n" +
                "  `LdlC` float DEFAULT NULL,\n" +
                "  `HdlC` float DEFAULT NULL,\n" +
                "  `CXR` varchar(20) DEFAULT NULL,\n" +
                "  `bScan` varchar(20) DEFAULT NULL,\n" +
                "  `cervicalSmear` varchar(20) DEFAULT NULL,\n" +
                "  `assistCheckOther` varchar(20) DEFAULT NULL,\n" +
                "  `gentleQuality` tinyint(4) DEFAULT NULL,\n" +
                "  `qiDeficiency` tinyint(4) DEFAULT NULL,\n" +
                "  `yangDeficiency` tinyint(4) DEFAULT NULL,\n" +
                "  `yinDeficiency` tinyint(4) DEFAULT NULL,\n" +
                "  `phlegmWet` tinyint(4) DEFAULT NULL,\n" +
                "  `dampHeat` tinyint(4) DEFAULT NULL,\n" +
                "  `bloodStasis` tinyint(4) DEFAULT NULL,\n" +
                "  `qiDepression` tinyint(4) DEFAULT NULL,\n" +
                "  `specialDiathesis` tinyint(4) DEFAULT NULL,\n" +
                "  `CVD` varchar(50) DEFAULT NULL,\n" +
                "  `KD` varchar(50) DEFAULT NULL,\n" +
                "  `HD` varchar(50) DEFAULT NULL,\n" +
                "  `VDHD` varchar(50) DEFAULT NULL,\n" +
                "  `ED` varchar(50) DEFAULT NULL,\n" +
                "  `ND` varchar(50) DEFAULT NULL,\n" +
                "  `OD` varchar(50) DEFAULT NULL,\n" +
                "  `hospitalization` tinyint(1) DEFAULT NULL,\n" +
                "  `familyHisroty` tinyint(1) DEFAULT NULL,\n" +
                "  `mainDrugUse` tinyint(1) DEFAULT NULL,\n" +
                "  `vaccinationHistory` tinyint(1) DEFAULT NULL,\n" +
                "  `healthComment` varchar(10) DEFAULT NULL,\n" +
                "  `healthSuggest` varchar(10) DEFAULT NULL,\n" +
                "  `dangerControl` varchar(100) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;\n";
        String reg = "PRIMARY KEY \\(`([a-zA-Z0-9_]{1,})`\\)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
//        SQLScriptResolver sqlScriptResolver = new SQLScriptResolver();
//        sqlScriptResolver.getTable(sql);
    }
}
