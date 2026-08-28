package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ueit.VmWare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Ueit
* @description 针对表【t_vmware】的数据库操作Mapper
* @createDate 2025-04-01 08:42:02
* @Entity generator.domain.TVmware
*/
@Mapper
public interface TVmwareMapper extends BaseMapper<VmWare> {

    List<VmWare> vmwarList(VmWare tVmware);

    void updatewar(VmWare tVmware);

    void deleteware(VmWare tVmware);

    VmWare selectexam(@Param("guid") String userguid, @Param("type") String type);

    String userByName(String userName);

}




