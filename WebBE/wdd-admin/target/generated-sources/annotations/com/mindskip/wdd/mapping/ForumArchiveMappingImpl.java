package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.forum.ForumArchiveEditRequestVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:39+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ForumArchiveMappingImpl implements ForumArchiveMapping {

    @Override
    public ForumArchive toForumArchive(ForumArchiveEditRequestVM forumArchiveEditRequestVM) {
        if ( forumArchiveEditRequestVM == null ) {
            return null;
        }

        ForumArchive forumArchive = new ForumArchive();

        forumArchive.setDeleted( forumArchiveEditRequestVM.getDeleted() );
        forumArchive.setId( forumArchiveEditRequestVM.getId() );
        forumArchive.setItemOrder( forumArchiveEditRequestVM.getItemOrder() );
        forumArchive.setName( forumArchiveEditRequestVM.getName() );
        forumArchive.setParentId( forumArchiveEditRequestVM.getParentId() );

        return forumArchive;
    }

    @Override
    public void mapForumArchive(ForumArchiveEditRequestVM forumArchiveEditRequestVM, ForumArchive forumArchive) {
        if ( forumArchiveEditRequestVM == null ) {
            return;
        }

        forumArchive.setDeleted( forumArchiveEditRequestVM.getDeleted() );
        forumArchive.setId( forumArchiveEditRequestVM.getId() );
        forumArchive.setItemOrder( forumArchiveEditRequestVM.getItemOrder() );
        forumArchive.setName( forumArchiveEditRequestVM.getName() );
        forumArchive.setParentId( forumArchiveEditRequestVM.getParentId() );
    }

    @Override
    public ForumArchiveEditRequestVM toForumArchiveEditRequestVM(ForumArchive forumArchive) {
        if ( forumArchive == null ) {
            return null;
        }

        ForumArchiveEditRequestVM forumArchiveEditRequestVM = new ForumArchiveEditRequestVM();

        forumArchiveEditRequestVM.setDeleted( forumArchive.getDeleted() );
        forumArchiveEditRequestVM.setId( forumArchive.getId() );
        forumArchiveEditRequestVM.setItemOrder( forumArchive.getItemOrder() );
        forumArchiveEditRequestVM.setName( forumArchive.getName() );
        forumArchiveEditRequestVM.setParentId( forumArchive.getParentId() );

        return forumArchiveEditRequestVM;
    }

    @Override
    public ArchiveVM toArchiveVM(ForumArchive forumArchive) {
        if ( forumArchive == null ) {
            return null;
        }

        ArchiveVM archiveVM = new ArchiveVM();

        archiveVM.setValue( forumArchive.getId() );
        archiveVM.setLabel( forumArchive.getName() );
        archiveVM.setId( forumArchive.getId() );
        archiveVM.setName( forumArchive.getName() );

        return archiveVM;
    }
}
