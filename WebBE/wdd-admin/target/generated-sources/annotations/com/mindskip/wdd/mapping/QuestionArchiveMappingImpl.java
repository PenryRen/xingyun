package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.question.QuestionArchiveEditRequestVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:38+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class QuestionArchiveMappingImpl implements QuestionArchiveMapping {

    @Override
    public QuestionArchive toQuestionArchive(QuestionArchiveEditRequestVM questionArchiveEditRequestVM) {
        if ( questionArchiveEditRequestVM == null ) {
            return null;
        }

        QuestionArchive questionArchive = new QuestionArchive();

        questionArchive.setDeleted( questionArchiveEditRequestVM.getDeleted() );
        questionArchive.setId( questionArchiveEditRequestVM.getId() );
        questionArchive.setItemOrder( questionArchiveEditRequestVM.getItemOrder() );
        questionArchive.setName( questionArchiveEditRequestVM.getName() );
        questionArchive.setParentId( questionArchiveEditRequestVM.getParentId() );

        return questionArchive;
    }

    @Override
    public void mapQuestionArchive(QuestionArchiveEditRequestVM questionArchiveEditRequestVM, QuestionArchive questionArchive) {
        if ( questionArchiveEditRequestVM == null ) {
            return;
        }

        questionArchive.setDeleted( questionArchiveEditRequestVM.getDeleted() );
        questionArchive.setId( questionArchiveEditRequestVM.getId() );
        questionArchive.setItemOrder( questionArchiveEditRequestVM.getItemOrder() );
        questionArchive.setName( questionArchiveEditRequestVM.getName() );
        questionArchive.setParentId( questionArchiveEditRequestVM.getParentId() );
    }

    @Override
    public QuestionArchiveEditRequestVM toQuestionArchiveEditRequestVM(QuestionArchive questionArchive) {
        if ( questionArchive == null ) {
            return null;
        }

        QuestionArchiveEditRequestVM questionArchiveEditRequestVM = new QuestionArchiveEditRequestVM();

        questionArchiveEditRequestVM.setDeleted( questionArchive.getDeleted() );
        questionArchiveEditRequestVM.setId( questionArchive.getId() );
        questionArchiveEditRequestVM.setItemOrder( questionArchive.getItemOrder() );
        questionArchiveEditRequestVM.setName( questionArchive.getName() );
        questionArchiveEditRequestVM.setParentId( questionArchive.getParentId() );

        return questionArchiveEditRequestVM;
    }

    @Override
    public ArchiveVM toArchiveVM(QuestionArchive questionArchive) {
        if ( questionArchive == null ) {
            return null;
        }

        ArchiveVM archiveVM = new ArchiveVM();

        archiveVM.setValue( questionArchive.getId() );
        archiveVM.setLabel( questionArchive.getName() );
        archiveVM.setId( questionArchive.getId() );
        archiveVM.setName( questionArchive.getName() );

        return archiveVM;
    }
}
