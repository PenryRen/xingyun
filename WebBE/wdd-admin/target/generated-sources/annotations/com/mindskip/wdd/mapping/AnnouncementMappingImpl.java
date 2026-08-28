package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementEditRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementInfoVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class AnnouncementMappingImpl implements AnnouncementMapping {

    @Override
    public AnnouncementPageResponseVM toAnnouncementResponseVM(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }

        AnnouncementPageResponseVM announcementPageResponseVM = new AnnouncementPageResponseVM();

        announcementPageResponseVM.setContent( announcement.getContent() );
        announcementPageResponseVM.setCreateUser( announcement.getCreateUser() );
        announcementPageResponseVM.setDeleted( announcement.getDeleted() );
        announcementPageResponseVM.setId( announcement.getId() );
        announcementPageResponseVM.setImageSrc( announcement.getImageSrc() );
        announcementPageResponseVM.setImportanted( announcement.getImportanted() );
        announcementPageResponseVM.setOverhead( announcement.getOverhead() );
        announcementPageResponseVM.setTitle( announcement.getTitle() );

        announcementPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(announcement.getCreateTime()) );

        return announcementPageResponseVM;
    }

    @Override
    public Announcement toAnnouncement(AnnouncementEditRequestVM announcementEditRequestVM) {
        if ( announcementEditRequestVM == null ) {
            return null;
        }

        Announcement announcement = new Announcement();

        announcement.setAnnouncementArchiveId( announcementEditRequestVM.getAnnouncementArchiveId() );
        announcement.setContent( announcementEditRequestVM.getContent() );
        announcement.setId( announcementEditRequestVM.getId() );
        announcement.setImageSrc( announcementEditRequestVM.getImageSrc() );
        announcement.setImportanted( announcementEditRequestVM.getImportanted() );
        announcement.setOverhead( announcementEditRequestVM.getOverhead() );
        announcement.setTitle( announcementEditRequestVM.getTitle() );

        return announcement;
    }

    @Override
    public void mapAnnouncement(AnnouncementEditRequestVM announcementEditRequestVM, Announcement announcement) {
        if ( announcementEditRequestVM == null ) {
            return;
        }

        announcement.setAnnouncementArchiveId( announcementEditRequestVM.getAnnouncementArchiveId() );
        announcement.setContent( announcementEditRequestVM.getContent() );
        announcement.setId( announcementEditRequestVM.getId() );
        announcement.setImageSrc( announcementEditRequestVM.getImageSrc() );
        announcement.setImportanted( announcementEditRequestVM.getImportanted() );
        announcement.setOverhead( announcementEditRequestVM.getOverhead() );
        announcement.setTitle( announcementEditRequestVM.getTitle() );
    }

    @Override
    public AnnouncementEditRequestVM toAnnouncementEditRequestVM(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }

        AnnouncementEditRequestVM announcementEditRequestVM = new AnnouncementEditRequestVM();

        announcementEditRequestVM.setAnnouncementArchiveId( announcement.getAnnouncementArchiveId() );
        announcementEditRequestVM.setContent( announcement.getContent() );
        announcementEditRequestVM.setId( announcement.getId() );
        announcementEditRequestVM.setImageSrc( announcement.getImageSrc() );
        announcementEditRequestVM.setImportanted( announcement.getImportanted() );
        announcementEditRequestVM.setOverhead( announcement.getOverhead() );
        announcementEditRequestVM.setTitle( announcement.getTitle() );

        return announcementEditRequestVM;
    }

    @Override
    public AnnouncementInfoVM toAnnouncementInfoVM(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }

        AnnouncementInfoVM announcementInfoVM = new AnnouncementInfoVM();

        announcementInfoVM.setContent( announcement.getContent() );
        if ( announcement.getCreateUser() != null ) {
            announcementInfoVM.setCreateUser( String.valueOf( announcement.getCreateUser() ) );
        }
        announcementInfoVM.setDeleted( announcement.getDeleted() );
        announcementInfoVM.setId( announcement.getId() );
        announcementInfoVM.setImageSrc( announcement.getImageSrc() );
        announcementInfoVM.setImportanted( announcement.getImportanted() );
        announcementInfoVM.setOverhead( announcement.getOverhead() );
        announcementInfoVM.setTitle( announcement.getTitle() );

        announcementInfoVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(announcement.getCreateTime()) );

        return announcementInfoVM;
    }
}
