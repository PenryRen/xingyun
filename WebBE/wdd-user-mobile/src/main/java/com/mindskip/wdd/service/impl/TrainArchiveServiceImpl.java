package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.domain.TrainArchive;
import com.mindskip.wdd.repository.TrainArchiveMapper;
import com.mindskip.wdd.service.TrainArchiveService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Service
@AllArgsConstructor
public class TrainArchiveServiceImpl extends ServiceImpl<TrainArchiveMapper, TrainArchive> implements TrainArchiveService {

    private final TrainArchiveMapper trainArchiveMapper;


    @Override
    public List<TrainArchive> selectRootTree() {
        return trainArchiveMapper.selectRootTree();
    }

    @Override
    public TrainArchive getTrainArchiveById(Integer id) {
        return trainArchiveMapper.selectById(id);
    }

    @Override
    public List<TrainArchive> getTrainArchiveByLevel(String level) {
        return trainArchiveMapper.getTrainArchiveByLevel(level);
    }

}
