package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:38+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ApplyArchiveMappingImpl implements ApplyArchiveMapping {

    @Override
    public ApplyArchive toApplyArchive(ApplyArchiveEditRequestVM applyArchiveEditRequestVM) {
        if ( applyArchiveEditRequestVM == null ) {
            return null;
        }

        ApplyArchive applyArchive = new ApplyArchive();

        applyArchive.setDeleted( applyArchiveEditRequestVM.getDeleted() );
        applyArchive.setId( applyArchiveEditRequestVM.getId() );
        applyArchive.setItemOrder( applyArchiveEditRequestVM.getItemOrder() );
        applyArchive.setName( applyArchiveEditRequestVM.getName() );
        applyArchive.setParentId( applyArchiveEditRequestVM.getParentId() );

        return applyArchive;
    }

    @Override
    public void mapApplyArchive(ApplyArchiveEditRequestVM applyArchiveEditRequestVM, ApplyArchive applyArchive) {
        if ( applyArchiveEditRequestVM == null ) {
            return;
        }

        applyArchive.setDeleted( applyArchiveEditRequestVM.getDeleted() );
        applyArchive.setId( applyArchiveEditRequestVM.getId() );
        applyArchive.setItemOrder( applyArchiveEditRequestVM.getItemOrder() );
        applyArchive.setName( applyArchiveEditRequestVM.getName() );
        applyArchive.setParentId( applyArchiveEditRequestVM.getParentId() );
    }

    @Override
    public ApplyArchiveEditRequestVM toApplyArchiveEditRequestVM(ApplyArchive applyArchive) {
        if ( applyArchive == null ) {
            return null;
        }

        ApplyArchiveEditRequestVM applyArchiveEditRequestVM = new ApplyArchiveEditRequestVM();

        applyArchiveEditRequestVM.setDeleted( applyArchive.getDeleted() );
        applyArchiveEditRequestVM.setId( applyArchive.getId() );
        applyArchiveEditRequestVM.setItemOrder( applyArchive.getItemOrder() );
        applyArchiveEditRequestVM.setName( applyArchive.getName() );
        applyArchiveEditRequestVM.setParentId( applyArchive.getParentId() );

        return applyArchiveEditRequestVM;
    }

    @Override
    public ArchiveVM toArchiveVM(ApplyArchive applyArchive) {
        if ( applyArchive == null ) {
            return null;
        }

        ArchiveVM archiveVM = new ArchiveVM();

        archiveVM.setValue( applyArchive.getId() );
        archiveVM.setLabel( applyArchive.getName() );
        archiveVM.setId( applyArchive.getId() );
        archiveVM.setName( applyArchive.getName() );

        return archiveVM;
    }
}
