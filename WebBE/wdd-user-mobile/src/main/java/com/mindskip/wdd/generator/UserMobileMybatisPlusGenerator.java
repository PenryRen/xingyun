package com.mindskip.wdd.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

import java.sql.Types;
import java.util.Collections;

public class UserMobileMybatisPlusGenerator {
    public static void main(String[] args) {
        String outDir = "D://MINDSKIP//ueit//background//ueit//ueit-user-mobile//src//main//";
        FastAutoGenerator.create("jdbc:mysql://192.168.0.96:3306/wdd?useSSL=false&useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowPublicKeyRetrieval=true&allowMultiQueries=true", "root", "123456").globalConfig(builder -> {
            builder.disableOpenDir()
                    .author("麟航团队") // 设置作者
                    .outputDir(outDir + "java"); // 指定输出目录
        }).dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
            if (typeCode == Types.SMALLINT) {
                return DbColumnType.INTEGER;
            } else if (typeCode == Types.TIMESTAMP) {
                return DbColumnType.DATE;
            }
            return typeRegistry.getColumnType(metaInfo);

        })).packageConfig(builder -> {
            builder.parent("com.mindskip") // 设置父包名
                    .moduleName("wdd") // 设置父包模块名
                    .entity("domain")
                    .mapper("repository")
                    .pathInfo(Collections.singletonMap(OutputFile.xml, outDir + "resources//mapper"));
        }).packageConfig(builder -> {
            builder.parent("com.mindskip") // 设置父包名
                    .moduleName("wdd") // 设置父包模块名
                    .entity("domain")
                    .mapper("repository")
                    .pathInfo(Collections.singletonMap(OutputFile.xml, outDir + "resources//mapper"));
        }).strategyConfig(builder -> {
          /*  builder = builder.addInclude("t_user");*/
            builder = builder.addInclude("t_feedback");
            builder.addTablePrefix("t_")
                    .entityBuilder().disableSerialVersionUID().enableLombok().enableFileOverride()
                    .mapperBuilder()
                    .serviceBuilder().formatServiceFileName("%sService")
                    .controllerBuilder().enableRestStyle();

        }).execute();
    }
}
