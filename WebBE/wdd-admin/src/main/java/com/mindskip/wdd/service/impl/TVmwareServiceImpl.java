package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.domain.ueit.VmWare;
import com.mindskip.wdd.repository.TVmwareMapper;
import com.mindskip.wdd.service.TVmwareService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author Ueit
* @description 针对表【t_vmware】的数据库操作Service实现
* @createDate 2025-04-01 08:42:02
*/
@Service
@AllArgsConstructor
public class TVmwareServiceImpl extends ServiceImpl<TVmwareMapper, VmWare>
    implements TVmwareService {

    private final TVmwareMapper tVmwareMapper;

    /**
     * 查询当前学生是否存在考试环境，如果存在返回错误并提示
     * @param userguid
     * @param type
     * @return
     */
    @Override
    public VmWare selectexam(String userguid, String type) {
        return tVmwareMapper.selectexam(userguid,type);
    }

    //根据名称查询分配学生id进行绑定
    @Override
    public String userByName(String userName) {
        return tVmwareMapper.userByName(userName);
    }
}




