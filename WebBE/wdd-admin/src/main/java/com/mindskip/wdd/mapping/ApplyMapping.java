package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.apply.ApplyEditRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyInfoVM;
import com.mindskip.wdd.viewmodel.apply.ApplyPageResponseVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: ApplyMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface ApplyMapping {


    /**
     * To apply response vm apply page response vm.
     *
     * @param apply the apply
     * @return the apply page response vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getCreateTime()))"),
            @Mapping(target = "applyEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getApplyEndTime()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getLimitEndTime()))")})
    ApplyPageResponseVM toApplyResponseVM(Apply apply);

    /**
     * To apply info vm apply info vm.
     *
     * @param apply the apply
     * @return the apply info vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getCreateTime()))"),
            @Mapping(target = "applyEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getApplyEndTime()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getLimitEndTime()))")})
    ApplyInfoVM toApplyInfoVM(Apply apply);


    /**
     * To apply apply.
     *
     * @param applyEditRequestVM the apply edit request vm
     * @return the apply
     */
    @Mappings({
            @Mapping(target = "applyEndTime", expression = "java(DateTimeUtil.parse(applyEditRequestVM.getApplyEndTime()))"),
            @Mapping(target = "limitStartTime", ignore = true),
            @Mapping(target = "limitEndTime", ignore = true)}
    )
    Apply toApply(ApplyEditRequestVM applyEditRequestVM);

    /**
     * Map apply.
     *
     * @param applyEditRequestVM the apply edit request vm
     * @param apply              the apply
     */
    @InheritConfiguration
    void mapApply(ApplyEditRequestVM applyEditRequestVM, @MappingTarget Apply apply);

    /**
     * To apply edit request vm apply edit request vm.
     *
     * @param apply the apply
     * @return the apply edit request vm
     */
    @Mappings({@Mapping(target = "applyEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(apply.getApplyEndTime()))")})
    ApplyEditRequestVM toApplyEditRequestVM(Apply apply);

}
