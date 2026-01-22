package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveVM;
import com.mindskip.wdd.viewmodel.apply.ApplyPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @version 1.7.0
 * @description: The interface Apply mapping.
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface ApplyMapping {

    /**
     * To apply page response vm apply page response vm.
     *
     * @param apply the apply
     * @return the apply page response vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getCreateTime()))"),
            @Mapping(target = "applyEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getApplyEndTime()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getLimitEndTime()))")})
    ApplyPageResponseVM toApplyPageResponseVM(Apply apply);


    ApplyArchiveVM toApplyArchiveVM(ApplyArchive applyArchive);

    List<ApplyArchiveVM> toApplyArchiveVMList(List<ApplyArchive> applyArchiveList);

}
