package com.mindskip.wdd.service;

import com.mindskip.wdd.domain.ueit.TrainItemUserQuestion;
import java.util.List;

/**
 * 用户课件功能点Service接口
 * 
 * @author libl
 * @date 2025-04-09
 */
public interface TrainItemUserQuestionService
{
    /**
     * 查询用户课件功能点
     * 
     * @param id 用户课件功能点主键
     * @return 用户课件功能点
     */
    public TrainItemUserQuestion selectTrainItemUserQuestionById(Integer id);

    /**
     * 查询用户课件功能点列表
     * 
     * @param trainItemUserQuestion 用户课件功能点
     * @return 用户课件功能点集合
     */
    public List<TrainItemUserQuestion> selectTrainItemUserQuestionList(TrainItemUserQuestion trainItemUserQuestion);

    /**
     * 新增用户课件功能点
     * 
     * @param trainItemUserQuestion 用户课件功能点
     * @return 结果
     */
    public int insertTrainItemUserQuestion(TrainItemUserQuestion trainItemUserQuestion);

    /**
     * 修改用户课件功能点
     * 
     * @param trainItemUserQuestion 用户课件功能点
     * @return 结果
     */
    public int updateTrainItemUserQuestion(TrainItemUserQuestion trainItemUserQuestion);

    /**
     * 批量删除用户课件功能点
     * 
     * @param ids 需要删除的用户课件功能点主键集合
     * @return 结果
     */
    public int deleteTrainItemUserQuestionByIds(Integer[] ids);

    /**
     * 删除用户课件功能点信息
     * 
     * @param id 用户课件功能点主键
     * @return 结果
     */
    public int deleteTrainItemUserQuestionById(Integer id);
}
