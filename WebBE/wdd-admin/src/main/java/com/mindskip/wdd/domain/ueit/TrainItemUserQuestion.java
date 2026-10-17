package com.mindskip.wdd.domain.ueit;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户课件功能点对象 t_train_item_user_question
 * 
 * @author libl
 * @date 2025-04-09
 */
@Data
@TableName(value = "t_train_item_user_question")
public class TrainItemUserQuestion implements Serializable {

    private static final long serialVersionUID = 8308822592705609080L;

    /** ID */
    private Integer id;

    /** 用户ID */
    private Integer userId;

    /** 课程ID */
    private Integer trainId;

    /** 课件ID */
    private Integer courseWareId;

    /** 问题ID */
    private Long questionId;

    /** 完成情况 */
    private Boolean completion;
}
