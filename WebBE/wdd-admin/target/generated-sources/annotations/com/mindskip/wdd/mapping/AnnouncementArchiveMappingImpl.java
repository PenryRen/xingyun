package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:38+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class AnnouncementArchiveMappingImpl implements AnnouncementArchiveMapping {

    @Override
    public AnnouncementArchive toAnnouncementArchive(AnnouncementArchiveEditRequestVM announcementArchiveEditRequestVM) {
        if ( announcementArchiveEditRequestVM == null ) {
            return null;
        }

        AnnouncementArchive announcementArchive = new AnnouncementArchive();

        announcementArchive.setDeleted( announcementArchiveEditRequestVM.getDeleted() );
        announcementArchive.setId( announcementArchiveEditRequestVM.getId() );
        announcementArchive.setItemOrder( announcementArchiveEditRequestVM.getItemOrder() );
        announcementArchive.setName( announcementArchiveEditRequestVM.getName() );
        announcementArchive.setParentId( announcementArchiveEditRequestVM.getParentId() );

        return announcementArchive;
    }

    @Override
    public void mapAnnouncementArchive(AnnouncementArchiveEditRequestVM announcementArchiveEditRequestVM, AnnouncementArchive announcementArchive) {
        if ( announcementArchiveEditRequestVM == null ) {
            return;
        }

        announcementArchive.setDeleted( announcementArchiveEditRequestVM.getDeleted() );
        announcementArchive.setId( announcementArchiveEditRequestVM.getId() );
        announcementArchive.setItemOrder( announcementArchiveEditRequestVM.getItemOrder() );
        announcementArchive.setName( announcementArchiveEditRequestVM.getName() );
        announcementArchive.setParentId( announcementArchiveEditRequestVM.getParentId() );
    }

    @Override
    public AnnouncementArchiveEditRequestVM toAnnouncementArchiveEditRequestVM(AnnouncementArchive announcementArchive) {
        if ( announcementArchive == null ) {
            return null;
        }

        AnnouncementArchiveEditRequestVM announcementArchiveEditRequestVM = new AnnouncementArchiveEditRequestVM();

        announcementArchiveEditRequestVM.setDeleted( announcementArchive.getDeleted() );
        announcementArchiveEditRequestVM.setId( announcementArchive.getId() );
        announcementArchiveEditRequestVM.setItemOrder( announcementArchive.getItemOrder() );
        announcementArchiveEditRequestVM.setName( announcementArchive.getName() );
        announcementArchiveEditRequestVM.setParentId( announcementArchive.getParentId() );

        return announcementArchiveEditRequestVM;
    }

    @Override
    public ArchiveVM toArchiveVM(AnnouncementArchive forumArchive) {
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
