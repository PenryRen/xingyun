package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaperArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperArchiveEditRequestVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ExamPaperArchiveMappingImpl implements ExamPaperArchiveMapping {

    @Override
    public ExamPaperArchive toExamPaperArchive(ExamPaperArchiveEditRequestVM examPaperArchiveEditRequestVM) {
        if ( examPaperArchiveEditRequestVM == null ) {
            return null;
        }

        ExamPaperArchive examPaperArchive = new ExamPaperArchive();

        examPaperArchive.setDeleted( examPaperArchiveEditRequestVM.getDeleted() );
        examPaperArchive.setId( examPaperArchiveEditRequestVM.getId() );
        examPaperArchive.setItemOrder( examPaperArchiveEditRequestVM.getItemOrder() );
        examPaperArchive.setName( examPaperArchiveEditRequestVM.getName() );
        examPaperArchive.setParentId( examPaperArchiveEditRequestVM.getParentId() );

        return examPaperArchive;
    }

    @Override
    public void mapExamPaperArchive(ExamPaperArchiveEditRequestVM examPaperArchiveEditRequestVM, ExamPaperArchive examPaperArchive) {
        if ( examPaperArchiveEditRequestVM == null ) {
            return;
        }

        examPaperArchive.setDeleted( examPaperArchiveEditRequestVM.getDeleted() );
        examPaperArchive.setId( examPaperArchiveEditRequestVM.getId() );
        examPaperArchive.setItemOrder( examPaperArchiveEditRequestVM.getItemOrder() );
        examPaperArchive.setName( examPaperArchiveEditRequestVM.getName() );
        examPaperArchive.setParentId( examPaperArchiveEditRequestVM.getParentId() );
    }

    @Override
    public ExamPaperArchiveEditRequestVM toExamPaperArchiveEditRequestVM(ExamPaperArchive examPaperArchive) {
        if ( examPaperArchive == null ) {
            return null;
        }

        ExamPaperArchiveEditRequestVM examPaperArchiveEditRequestVM = new ExamPaperArchiveEditRequestVM();

        examPaperArchiveEditRequestVM.setDeleted( examPaperArchive.getDeleted() );
        examPaperArchiveEditRequestVM.setId( examPaperArchive.getId() );
        examPaperArchiveEditRequestVM.setItemOrder( examPaperArchive.getItemOrder() );
        examPaperArchiveEditRequestVM.setName( examPaperArchive.getName() );
        examPaperArchiveEditRequestVM.setParentId( examPaperArchive.getParentId() );

        return examPaperArchiveEditRequestVM;
    }

    @Override
    public ArchiveVM toArchiveVM(ExamPaperArchive examPaperArchive) {
        if ( examPaperArchive == null ) {
            return null;
        }

        ArchiveVM archiveVM = new ArchiveVM();

        archiveVM.setValue( examPaperArchive.getId() );
        archiveVM.setLabel( examPaperArchive.getName() );
        archiveVM.setId( examPaperArchive.getId() );
        archiveVM.setName( examPaperArchive.getName() );

        return archiveVM;
    }
}
