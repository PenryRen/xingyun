package com.mindskip.wdd.configuration.utility;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: YML数组
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public class YmlUtil {
    /**
     * yml字符串数组转对象
     *
     * @param source the source
     * @return the list
     */
    public static List<String> ymlArrayConvert(List<String> source) {
        return source.stream().filter(value -> StringUtils.isNotBlank(value))
                .map(value -> value.
                        replaceAll("-", "")
                        .replaceAll("\n", "")
                        .replaceAll(" ", "")
                )
                .collect(Collectors.toList());
    }
}
