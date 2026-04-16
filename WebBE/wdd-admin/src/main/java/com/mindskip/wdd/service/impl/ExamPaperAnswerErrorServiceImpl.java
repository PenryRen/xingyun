package com.mindskip.wdd.service.impl;


import com.mindskip.wdd.domain.ueit.ExamPaperAnswerError;
import com.mindskip.wdd.repository.ExamPaperAnswerErrorMapper;
import com.mindskip.wdd.service.ExamPaperAnswerErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 试卷核验Service业务层处理
 * 
 * @author libl
 * @date 2025-04-10
 */
@Service
public class ExamPaperAnswerErrorServiceImpl implements ExamPaperAnswerErrorService
{
    @Autowired
    private ExamPaperAnswerErrorMapper examPaperAnswerErrorMapper;

    /**
     * 查询试卷核验
     * 
     * @param id 试卷核验主键
     * @return 试卷核验
     */
    @Override
    public ExamPaperAnswerError selectExamPaperAnswerErrorById(Long id)
    {
        return examPaperAnswerErrorMapper.selectExamPaperAnswerErrorById(id);
    }

    /**
     * 查询试卷核验列表
     * 
     * @param examPaperAnswerError 试卷核验
     * @return 试卷核验
     */
    @Override
    public List<ExamPaperAnswerError> selectExamPaperAnswerErrorList(ExamPaperAnswerError examPaperAnswerError)
    {
        return examPaperAnswerErrorMapper.selectExamPaperAnswerErrorList(examPaperAnswerError);
    }

    /**
     * 新增试卷核验
     * 
     * @param examPaperAnswerError 试卷核验
     * @return 结果
     */
    @Override
    public int insertExamPaperAnswerError(ExamPaperAnswerError examPaperAnswerError)
    {
        examPaperAnswerError.setCreateTime(new Date());
        return examPaperAnswerErrorMapper.insertExamPaperAnswerError(examPaperAnswerError);
    }

    /**
     * 修改试卷核验
     * 
     * @param examPaperAnswerError 试卷核验
     * @return 结果
     */
    @Override
    public int updateExamPaperAnswerError(ExamPaperAnswerError examPaperAnswerError)
    {
        examPaperAnswerError.setUpdateTime(new Date());
        return examPaperAnswerErrorMapper.updateExamPaperAnswerError(examPaperAnswerError);
    }

    /**
     * 批量删除试卷核验
     * 
     * @param ids 需要删除的试卷核验主键
     * @return 结果
     */
    @Override
    public int deleteExamPaperAnswerErrorByIds(Long[] ids)
    {
        return examPaperAnswerErrorMapper.deleteExamPaperAnswerErrorByIds(ids);
    }

    /**
     * 删除试卷核验信息
     * 
     * @param id 试卷核验主键
     * @return 结果
     */
    @Override
    public int deleteExamPaperAnswerErrorById(Long id)
    {
        return examPaperAnswerErrorMapper.deleteExamPaperAnswerErrorById(id);
    }
}
