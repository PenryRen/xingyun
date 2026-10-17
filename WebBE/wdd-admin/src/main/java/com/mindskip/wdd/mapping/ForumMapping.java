package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.forum.ForumEditRequestVM;
import com.mindskip.wdd.viewmodel.forum.ForumPageResponseVM;
import com.mindskip.wdd.viewmodel.forum.comment.ForumCommentPageResponseVM;
import org.mapstruct.*;


/**
 * @version 1.9.0
 * @description: ForumMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface ForumMapping {

    /**
     * To forum response vm forum page response vm.
     *
     * @param forum the forum
     * @return the forum page response vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(forum.getCreateTime()))")})
    ForumPageResponseVM toForumResponseVM(Forum forum);

    /**
     * To forum forum.
     *
     * @param forumEditRequestVM the forum edit request vm
     * @return the forum
     */
    Forum toForum(ForumEditRequestVM forumEditRequestVM);

    /**
     * Map forum.
     *
     * @param forumEditRequestVM the forum edit request vm
     * @param forum              the forum
     */
    @InheritConfiguration
    void mapForum(ForumEditRequestVM forumEditRequestVM, @MappingTarget Forum forum);

    /**
     * To forum edit request vm forum edit request vm.
     *
     * @param forum the forum
     * @return the forum edit request vm
     */
    ForumEditRequestVM toForumEditRequestVM(Forum forum);


    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(forumComment.getCreateTime()))")})
    ForumCommentPageResponseVM toForumCommentPageResponseVM(ForumComment forumComment);
}
