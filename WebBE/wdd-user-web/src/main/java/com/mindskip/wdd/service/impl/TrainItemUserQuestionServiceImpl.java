package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.domain.ueit.TrainItemUserQuestion;
import com.mindskip.wdd.repository.TrainItemUserQuestionMapper;
import com.mindskip.wdd.service.TrainItemUserQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户课件功能点Service业务层处理
 * 
 * @author libl
 * @date 2024-04-09
 */
@Service
public class TrainItemUserQuestionServiceImpl extends ServiceImpl<TrainItemUserQuestionMapper, TrainItemUserQuestion> implements TrainItemUserQuestionService
{
    @Autowired
    private TrainItemUserQuestionMapper trainItemUserQuestionMapper;

    /**
     * 查询用户课件功能点
     * 
     * @param id 用户课件功能点主键
     * @return 用户课件功能点
     */
    @Override
    public TrainItemUserQuestion selectTrainItemUserQuestionById(Integer id)
    {
        return trainItemUserQuestionMapper.selectTrainItemUserQuestionById(id);
    }

    /**
     * 查询用户课件功能点列表
     * 
     * @param trainItemUserQuestion 用户课件功能点
     * @return 用户课件功能点
     */
    @Override
    public List<TrainItemUserQuestion> selectTrainItemUserQuestionList(TrainItemUserQuestion trainItemUserQuestion)
    {
        return trainItemUserQuestionMapper.selectTrainItemUserQuestionList(trainItemUserQuestion);
    }

    /**
     * 新增用户课件功能点
     * 
     * @param trainItemUserQuestion 用户课件功能点
     * @return 结果
     */
    @Override
    public int insertTrainItemUserQuestion(TrainItemUserQuestion trainItemUserQuestion)
    {
        return trainItemUserQuestionMapper.insertTrainItemUserQuestion(trainItemUserQuestion);
    }

    /**
     * 修改用户课件功能点
     * 
     * @param trainItemUserQuestion 用户课件功能点
     * @return 结果
     */
    @Override
    public int updateTrainItemUserQuestion(TrainItemUserQuestion trainItemUserQuestion)
    {
        return trainItemUserQuestionMapper.updateTrainItemUserQuestion(trainItemUserQuestion);
    }

    /**
     * 批量删除用户课件功能点
     * 
     * @param ids 需要删除的用户课件功能点主键
     * @return 结果
     */
    @Override
    public int deleteTrainItemUserQuestionByIds(Integer[] ids)
    {
        return trainItemUserQuestionMapper.deleteTrainItemUserQuestionByIds(ids);
    }

    /**
     * 删除用户课件功能点信息
     * 
     * @param id 用户课件功能点主键
     * @return 结果
     */
    @Override
    public int deleteTrainItemUserQuestionById(Integer id)
    {
        return trainItemUserQuestionMapper.deleteTrainItemUserQuestionById(id);
    }
}
