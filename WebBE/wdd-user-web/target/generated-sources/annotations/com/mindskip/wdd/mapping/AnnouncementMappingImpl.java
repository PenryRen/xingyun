package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementDetailRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageResponseVM;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:39+0800",
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

        announcementPageResponseVM.setCreateUser( announcement.getCreateUser() );
        announcementPageResponseVM.setId( announcement.getId() );
        announcementPageResponseVM.setImageSrc( announcement.getImageSrc() );
        announcementPageResponseVM.setImportanted( announcement.getImportanted() );
        announcementPageResponseVM.setOverhead( announcement.getOverhead() );
        announcementPageResponseVM.setTitle( announcement.getTitle() );

        announcementPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(announcement.getCreateTime()) );

        return announcementPageResponseVM;
    }

    @Override
    public Announcement toAnnouncement(AnnouncementDetailRequestVM announcementDetailRequestVM) {
        if ( announcementDetailRequestVM == null ) {
            return null;
        }

        Announcement announcement = new Announcement();

        announcement.setContent( announcementDetailRequestVM.getContent() );
        try {
            if ( announcementDetailRequestVM.getCreateTime() != null ) {
                announcement.setCreateTime( new SimpleDateFormat().parse( announcementDetailRequestVM.getCreateTime() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        announcement.setId( announcementDetailRequestVM.getId() );
        announcement.setImageSrc( announcementDetailRequestVM.getImageSrc() );
        announcement.setImportanted( announcementDetailRequestVM.getImportanted() );
        announcement.setOverhead( announcementDetailRequestVM.getOverhead() );
        announcement.setTitle( announcementDetailRequestVM.getTitle() );

        return announcement;
    }

    @Override
    public void mapAnnouncement(AnnouncementDetailRequestVM announcementDetailRequestVM, Announcement announcement) {
        if ( announcementDetailRequestVM == null ) {
            return;
        }

        announcement.setContent( announcementDetailRequestVM.getContent() );
        try {
            if ( announcementDetailRequestVM.getCreateTime() != null ) {
                announcement.setCreateTime( new SimpleDateFormat().parse( announcementDetailRequestVM.getCreateTime() ) );
            }
            else {
                announcement.setCreateTime( null );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        announcement.setId( announcementDetailRequestVM.getId() );
        announcement.setImageSrc( announcementDetailRequestVM.getImageSrc() );
        announcement.setImportanted( announcementDetailRequestVM.getImportanted() );
        announcement.setOverhead( announcementDetailRequestVM.getOverhead() );
        announcement.setTitle( announcementDetailRequestVM.getTitle() );
    }

    @Override
    public AnnouncementDetailRequestVM toAnnouncementEditRequestVM(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }

        AnnouncementDetailRequestVM announcementDetailRequestVM = new AnnouncementDetailRequestVM();

        announcementDetailRequestVM.setContent( announcement.getContent() );
        announcementDetailRequestVM.setId( announcement.getId() );
        announcementDetailRequestVM.setImageSrc( announcement.getImageSrc() );
        announcementDetailRequestVM.setImportanted( announcement.getImportanted() );
        announcementDetailRequestVM.setOverhead( announcement.getOverhead() );
        announcementDetailRequestVM.setTitle( announcement.getTitle() );

        announcementDetailRequestVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(announcement.getCreateTime()) );

        return announcementDetailRequestVM;
    }

    @Override
    public AnnouncementArchiveVM toAnnouncementArchiveVM(AnnouncementArchive announcementArchive) {
        if ( announcementArchive == null ) {
            return null;
        }

        AnnouncementArchiveVM announcementArchiveVM = new AnnouncementArchiveVM();

        announcementArchiveVM.setId( announcementArchive.getId() );
        announcementArchiveVM.setName( announcementArchive.getName() );

        return announcementArchiveVM;
    }

    @Override
    public List<AnnouncementArchiveVM> toAnnouncementArchiveVMList(List<AnnouncementArchive> announcementArchiveList) {
        if ( announcementArchiveList == null ) {
            return null;
        }

        List<AnnouncementArchiveVM> list = new ArrayList<AnnouncementArchiveVM>( announcementArchiveList.size() );
        for ( AnnouncementArchive announcementArchive : announcementArchiveList ) {
            list.add( toAnnouncementArchiveVM( announcementArchive ) );
        }

        return list;
    }
}
