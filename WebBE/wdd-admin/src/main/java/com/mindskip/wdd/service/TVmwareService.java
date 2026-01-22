package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.domain.ueit.VmWare;

/**
* @author Ueit
* @description 针对表【t_vmware】的数据库操作Service
* @createDate 2024-04-01 08:42:02
*/
public interface TVmwareService extends IService<VmWare> {

    VmWare selectexam(String userguid, String type);

    //根据名称查询分配学生id进行绑定
    String userByName(String userName);

}
