package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.forum.*;
import com.mindskip.wdd.viewmodel.user.CommentPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @version 1.9.0
 * @description: The interface Forum mapping.
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface ForumMapping {

    /**
     * To forum response vm forum page response vm.
     *
     * @param forum the forum
     * @return the forum page response vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateFormat(forum.getCreateTime()))")})
    ForumPageResponseVM toForumResponseVM(Forum forum);


    /**
     * To forum archive vm list list.
     *
     * @param forumArchiveList the forum archive list
     * @return the list
     */
    List<ForumArchiveVM> toForumArchiveVMList(List<ForumArchive> forumArchiveList);


    /**
     * To forum forum.
     *
     * @param forumRequestVM the forum request vm
     * @return the forum
     */
    Forum toForum(ForumRequestVM forumRequestVM);


    /**
     * To forum comment forum comment.
     *
     * @param forumCommentAdd the forum comment add
     * @return the forum comment
     */
    ForumComment toForumComment(ForumCommentAdd forumCommentAdd);

    /**
     * To forum comment page response vm forum comment page response vm.
     *
     * @param forumComment the forum comment
     * @return the forum comment page response vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateFormat(forumComment.getCreateTime()))")})
    ForumCommentPageResponseVM toForumCommentPageResponseVM(ForumComment forumComment);


    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateFormat(forumComment.getCreateTime()))")})
    CommentPageResponseVM toCommentPageResponseVM(ForumComment forumComment);

}
