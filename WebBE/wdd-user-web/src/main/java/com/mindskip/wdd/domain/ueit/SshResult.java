package com.mindskip.wdd.domain.ueit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ssh执行命令返回值
 *
 * @author libl
 * @date 2024-04-07
 */
@Getter
@Setter
@AllArgsConstructor
public class SshResult {

    /**
     * 错误码，0成功，500错误，其他为JSch返回状态
     */
    private Integer errCode = 0;

    /**
     * 执行命令响应结果
     */
    private StringBuffer result;

    /**
     * 错误信息
     */
    private String errmsg;

}
