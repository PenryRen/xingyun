package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.forum.ForumEditRequestVM;
import com.mindskip.wdd.viewmodel.forum.ForumPageResponseVM;
import com.mindskip.wdd.viewmodel.forum.comment.ForumCommentPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ForumMappingImpl implements ForumMapping {

    @Override
    public ForumPageResponseVM toForumResponseVM(Forum forum) {
        if ( forum == null ) {
            return null;
        }

        ForumPageResponseVM forumPageResponseVM = new ForumPageResponseVM();

        forumPageResponseVM.setContent( forum.getContent() );
        forumPageResponseVM.setId( forum.getId() );
        forumPageResponseVM.setTitle( forum.getTitle() );

        forumPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(forum.getCreateTime()) );

        return forumPageResponseVM;
    }

    @Override
    public Forum toForum(ForumEditRequestVM forumEditRequestVM) {
        if ( forumEditRequestVM == null ) {
            return null;
        }

        Forum forum = new Forum();

        forum.setContent( forumEditRequestVM.getContent() );
        forum.setForumArchiveId( forumEditRequestVM.getForumArchiveId() );
        forum.setId( forumEditRequestVM.getId() );
        forum.setTitle( forumEditRequestVM.getTitle() );

        return forum;
    }

    @Override
    public void mapForum(ForumEditRequestVM forumEditRequestVM, Forum forum) {
        if ( forumEditRequestVM == null ) {
            return;
        }

        forum.setContent( forumEditRequestVM.getContent() );
        forum.setForumArchiveId( forumEditRequestVM.getForumArchiveId() );
        forum.setId( forumEditRequestVM.getId() );
        forum.setTitle( forumEditRequestVM.getTitle() );
    }

    @Override
    public ForumEditRequestVM toForumEditRequestVM(Forum forum) {
        if ( forum == null ) {
            return null;
        }

        ForumEditRequestVM forumEditRequestVM = new ForumEditRequestVM();

        forumEditRequestVM.setContent( forum.getContent() );
        forumEditRequestVM.setForumArchiveId( forum.getForumArchiveId() );
        forumEditRequestVM.setId( forum.getId() );
        forumEditRequestVM.setTitle( forum.getTitle() );

        return forumEditRequestVM;
    }

    @Override
    public ForumCommentPageResponseVM toForumCommentPageResponseVM(ForumComment forumComment) {
        if ( forumComment == null ) {
            return null;
        }

        ForumCommentPageResponseVM forumCommentPageResponseVM = new ForumCommentPageResponseVM();

        forumCommentPageResponseVM.setContent( forumComment.getContent() );
        if ( forumComment.getId() != null ) {
            forumCommentPageResponseVM.setId( forumComment.getId().intValue() );
        }

        forumCommentPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(forumComment.getCreateTime()) );

        return forumCommentPageResponseVM;
    }
}
