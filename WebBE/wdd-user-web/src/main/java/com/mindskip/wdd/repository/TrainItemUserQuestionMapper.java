package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ueit.TrainItemUserQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户课件功能点Mapper接口
 * 
 * @author libl
 * @date 2024-04-09
 */
@Mapper
public interface TrainItemUserQuestionMapper extends BaseMapper<TrainItemUserQuestion>
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
     * 删除用户课件功能点
     * 
     * @param id 用户课件功能点主键
     * @return 结果
     */
    public int deleteTrainItemUserQuestionById(Integer id);

    /**
     * 批量删除用户课件功能点
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTrainItemUserQuestionByIds(Integer[] ids);
}
