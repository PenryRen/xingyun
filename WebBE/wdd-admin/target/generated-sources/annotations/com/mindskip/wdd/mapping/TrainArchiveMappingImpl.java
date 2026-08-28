package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.TrainArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.train.archive.TrainArchiveEditRequestVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class TrainArchiveMappingImpl implements TrainArchiveMapping {

    @Override
    public TrainArchive toTrainArchive(TrainArchiveEditRequestVM trainArchiveEditRequestVM) {
        if ( trainArchiveEditRequestVM == null ) {
            return null;
        }

        TrainArchive trainArchive = new TrainArchive();

        trainArchive.setDeleted( trainArchiveEditRequestVM.getDeleted() );
        trainArchive.setId( trainArchiveEditRequestVM.getId() );
        trainArchive.setItemOrder( trainArchiveEditRequestVM.getItemOrder() );
        trainArchive.setName( trainArchiveEditRequestVM.getName() );
        trainArchive.setParentId( trainArchiveEditRequestVM.getParentId() );

        return trainArchive;
    }

    @Override
    public void mapTrainArchive(TrainArchiveEditRequestVM trainArchiveEditRequestVM, TrainArchive trainArchive) {
        if ( trainArchiveEditRequestVM == null ) {
            return;
        }

        trainArchive.setDeleted( trainArchiveEditRequestVM.getDeleted() );
        trainArchive.setId( trainArchiveEditRequestVM.getId() );
        trainArchive.setItemOrder( trainArchiveEditRequestVM.getItemOrder() );
        trainArchive.setName( trainArchiveEditRequestVM.getName() );
        trainArchive.setParentId( trainArchiveEditRequestVM.getParentId() );
    }

    @Override
    public TrainArchiveEditRequestVM toTrainArchiveEditRequestVM(TrainArchive trainArchive) {
        if ( trainArchive == null ) {
            return null;
        }

        TrainArchiveEditRequestVM trainArchiveEditRequestVM = new TrainArchiveEditRequestVM();

        trainArchiveEditRequestVM.setDeleted( trainArchive.getDeleted() );
        trainArchiveEditRequestVM.setId( trainArchive.getId() );
        trainArchiveEditRequestVM.setItemOrder( trainArchive.getItemOrder() );
        trainArchiveEditRequestVM.setName( trainArchive.getName() );
        trainArchiveEditRequestVM.setParentId( trainArchive.getParentId() );

        return trainArchiveEditRequestVM;
    }

    @Override
    public ArchiveVM toArchiveVM(TrainArchive trainArchive) {
        if ( trainArchive == null ) {
            return null;
        }

        ArchiveVM archiveVM = new ArchiveVM();

        archiveVM.setValue( trainArchive.getId() );
        archiveVM.setLabel( trainArchive.getName() );
        archiveVM.setId( trainArchive.getId() );
        archiveVM.setName( trainArchive.getName() );

        return archiveVM;
    }
}
