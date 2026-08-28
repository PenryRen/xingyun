package com.mindskip.wdd.repository;

import java.util.List;
import com.mindskip.wdd.domain.ueit.ExamPaperAnswerError;
import org.apache.ibatis.annotations.Mapper;

/**
 * 试卷核验Mapper接口
 * 
 * @author libl
 * @date 2025-04-10
 */
@Mapper
public interface ExamPaperAnswerErrorMapper 
{
    /**
     * 查询试卷核验
     * 
     * @param id 试卷核验主键
     * @return 试卷核验
     */
    public ExamPaperAnswerError selectExamPaperAnswerErrorById(Long id);

    /**
     * 查询试卷核验列表
     * 
     * @param examPaperAnswerError 试卷核验
     * @return 试卷核验集合
     */
    public List<ExamPaperAnswerError> selectExamPaperAnswerErrorList(ExamPaperAnswerError examPaperAnswerError);

    /**
     * 新增试卷核验
     * 
     * @param examPaperAnswerError 试卷核验
     * @return 结果
     */
    public int insertExamPaperAnswerError(ExamPaperAnswerError examPaperAnswerError);

    /**
     * 修改试卷核验
     * 
     * @param examPaperAnswerError 试卷核验
     * @return 结果
     */
    public int updateExamPaperAnswerError(ExamPaperAnswerError examPaperAnswerError);

    /**
     * 删除试卷核验
     * 
     * @param id 试卷核验主键
     * @return 结果
     */
    public int deleteExamPaperAnswerErrorById(Long id);

    /**
     * 批量删除试卷核验
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExamPaperAnswerErrorByIds(Long[] ids);
}
