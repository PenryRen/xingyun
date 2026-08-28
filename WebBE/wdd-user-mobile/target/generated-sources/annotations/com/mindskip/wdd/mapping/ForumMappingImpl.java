package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.forum.ForumArchiveVM;
import com.mindskip.wdd.viewmodel.forum.ForumCommentAdd;
import com.mindskip.wdd.viewmodel.forum.ForumCommentPageResponseVM;
import com.mindskip.wdd.viewmodel.forum.ForumPageResponseVM;
import com.mindskip.wdd.viewmodel.forum.ForumRequestVM;
import com.mindskip.wdd.viewmodel.user.CommentPageResponseVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:28:30+0800",
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
        forumPageResponseVM.setForumArchiveId( forum.getForumArchiveId() );
        forumPageResponseVM.setId( forum.getId() );
        forumPageResponseVM.setTitle( forum.getTitle() );

        forumPageResponseVM.setCreateTime( DateTimeUtil.dateFormat(forum.getCreateTime()) );

        return forumPageResponseVM;
    }

    @Override
    public List<ForumArchiveVM> toForumArchiveVMList(List<ForumArchive> forumArchiveList) {
        if ( forumArchiveList == null ) {
            return null;
        }

        List<ForumArchiveVM> list = new ArrayList<ForumArchiveVM>( forumArchiveList.size() );
        for ( ForumArchive forumArchive : forumArchiveList ) {
            list.add( forumArchiveToForumArchiveVM( forumArchive ) );
        }

        return list;
    }

    @Override
    public Forum toForum(ForumRequestVM forumRequestVM) {
        if ( forumRequestVM == null ) {
            return null;
        }

        Forum forum = new Forum();

        forum.setContent( forumRequestVM.getContent() );
        forum.setForumArchiveId( forumRequestVM.getForumArchiveId() );
        forum.setTitle( forumRequestVM.getTitle() );

        return forum;
    }

    @Override
    public ForumComment toForumComment(ForumCommentAdd forumCommentAdd) {
        if ( forumCommentAdd == null ) {
            return null;
        }

        ForumComment forumComment = new ForumComment();

        forumComment.setContent( forumCommentAdd.getContent() );
        forumComment.setForumArchiveId( forumCommentAdd.getForumArchiveId() );
        forumComment.setForumId( forumCommentAdd.getForumId() );
        forumComment.setParentId( forumCommentAdd.getParentId() );

        return forumComment;
    }

    @Override
    public ForumCommentPageResponseVM toForumCommentPageResponseVM(ForumComment forumComment) {
        if ( forumComment == null ) {
            return null;
        }

        ForumCommentPageResponseVM forumCommentPageResponseVM = new ForumCommentPageResponseVM();

        forumCommentPageResponseVM.setContent( forumComment.getContent() );
        forumCommentPageResponseVM.setForumArchiveId( forumComment.getForumArchiveId() );
        forumCommentPageResponseVM.setForumId( forumComment.getForumId() );
        forumCommentPageResponseVM.setId( forumComment.getId() );
        forumCommentPageResponseVM.setParentId( forumComment.getParentId() );

        forumCommentPageResponseVM.setCreateTime( DateTimeUtil.dateFormat(forumComment.getCreateTime()) );

        return forumCommentPageResponseVM;
    }

    @Override
    public CommentPageResponseVM toCommentPageResponseVM(ForumComment forumComment) {
        if ( forumComment == null ) {
            return null;
        }

        CommentPageResponseVM commentPageResponseVM = new CommentPageResponseVM();

        commentPageResponseVM.setContent( forumComment.getContent() );
        commentPageResponseVM.setForumId( forumComment.getForumId() );
        commentPageResponseVM.setId( forumComment.getId() );

        commentPageResponseVM.setCreateTime( DateTimeUtil.dateFormat(forumComment.getCreateTime()) );

        return commentPageResponseVM;
    }

    protected ForumArchiveVM forumArchiveToForumArchiveVM(ForumArchive forumArchive) {
        if ( forumArchive == null ) {
            return null;
        }

        ForumArchiveVM forumArchiveVM = new ForumArchiveVM();

        forumArchiveVM.setId( forumArchive.getId() );
        forumArchiveVM.setLevel( forumArchive.getLevel() );
        forumArchiveVM.setName( forumArchive.getName() );

        return forumArchiveVM;
    }
}
