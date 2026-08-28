package com.mindskip.wdd.viewmodel.ueit;

import lombok.Data;

@Data
public class VmWareVM {

    /**
     * UUID
     */
    private String guid;

    /**
     * 虚拟机名称
     */
    private String vmName;

    /**
     * 虚拟机父ID
     */
    private String vmParentId;

    /**
     * 分类   00：实训   01：考试
     */
    private String classes;

    /**
     * 试卷id
     */
    private Long examPaperId;
}
