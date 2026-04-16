package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ueit.VmWare;
import com.mindskip.wdd.domain.ueit.VmWareClone;
import com.mindskip.wdd.viewmodel.ueit.VmWareVM;
import org.mapstruct.Mapper;

/**
 * 虚拟机实体映射
 *
 * @author libl
 * @date 2025-04-07
 */
@Mapper(componentModel = "spring")
public interface VmWareMapping {

    VmWareVM toVmWareVM(VmWare vmWare);

    VmWareClone toVmWareClone(VmWare VmWare);

    VmWare toVmWare(VmWareClone vmWareClone);
}
