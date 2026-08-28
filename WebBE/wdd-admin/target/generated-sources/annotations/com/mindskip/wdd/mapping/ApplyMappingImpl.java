package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.apply.ApplyEditRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyInfoVM;
import com.mindskip.wdd.viewmodel.apply.ApplyPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ApplyMappingImpl implements ApplyMapping {

    @Override
    public ApplyPageResponseVM toApplyResponseVM(Apply apply) {
        if ( apply == null ) {
            return null;
        }

        ApplyPageResponseVM applyPageResponseVM = new ApplyPageResponseVM();

        applyPageResponseVM.setCount( apply.getCount() );
        applyPageResponseVM.setCreateUser( apply.getCreateUser() );
        applyPageResponseVM.setDeleted( apply.getDeleted() );
        applyPageResponseVM.setId( apply.getId() );
        applyPageResponseVM.setLimited( apply.getLimited() );
        applyPageResponseVM.setName( apply.getName() );
        applyPageResponseVM.setStatus( apply.getStatus() );

        applyPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(apply.getCreateTime()) );
        applyPageResponseVM.setApplyEndTime( DateTimeUtil.dateTimeFullFormat(apply.getApplyEndTime()) );
        applyPageResponseVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(apply.getLimitStartTime()) );
        applyPageResponseVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(apply.getLimitEndTime()) );

        return applyPageResponseVM;
    }

    @Override
    public ApplyInfoVM toApplyInfoVM(Apply apply) {
        if ( apply == null ) {
            return null;
        }

        ApplyInfoVM applyInfoVM = new ApplyInfoVM();

        if ( apply.getCreateUser() != null ) {
            applyInfoVM.setCreateUser( String.valueOf( apply.getCreateUser() ) );
        }
        applyInfoVM.setId( apply.getId() );
        applyInfoVM.setName( apply.getName() );

        applyInfoVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(apply.getCreateTime()) );
        applyInfoVM.setApplyEndTime( DateTimeUtil.dateTimeFullFormat(apply.getApplyEndTime()) );
        applyInfoVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(apply.getLimitStartTime()) );
        applyInfoVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(apply.getLimitEndTime()) );

        return applyInfoVM;
    }

    @Override
    public Apply toApply(ApplyEditRequestVM applyEditRequestVM) {
        if ( applyEditRequestVM == null ) {
            return null;
        }

        Apply apply = new Apply();

        apply.setApplyArchiveId( applyEditRequestVM.getApplyArchiveId() );
        apply.setCount( applyEditRequestVM.getCount() );
        apply.setId( applyEditRequestVM.getId() );
        apply.setLimited( applyEditRequestVM.getLimited() );
        apply.setName( applyEditRequestVM.getName() );
        apply.setNeedAudit( applyEditRequestVM.getNeedAudit() );

        apply.setApplyEndTime( DateTimeUtil.parse(applyEditRequestVM.getApplyEndTime()) );

        return apply;
    }

    @Override
    public void mapApply(ApplyEditRequestVM applyEditRequestVM, Apply apply) {
        if ( applyEditRequestVM == null ) {
            return;
        }

        apply.setApplyArchiveId( applyEditRequestVM.getApplyArchiveId() );
        apply.setCount( applyEditRequestVM.getCount() );
        apply.setId( applyEditRequestVM.getId() );
        apply.setLimited( applyEditRequestVM.getLimited() );
        apply.setName( applyEditRequestVM.getName() );
        apply.setNeedAudit( applyEditRequestVM.getNeedAudit() );

        apply.setApplyEndTime( DateTimeUtil.parse(applyEditRequestVM.getApplyEndTime()) );
    }

    @Override
    public ApplyEditRequestVM toApplyEditRequestVM(Apply apply) {
        if ( apply == null ) {
            return null;
        }

        ApplyEditRequestVM applyEditRequestVM = new ApplyEditRequestVM();

        applyEditRequestVM.setApplyArchiveId( apply.getApplyArchiveId() );
        applyEditRequestVM.setCount( apply.getCount() );
        applyEditRequestVM.setId( apply.getId() );
        applyEditRequestVM.setLimited( apply.getLimited() );
        applyEditRequestVM.setName( apply.getName() );
        applyEditRequestVM.setNeedAudit( apply.getNeedAudit() );

        applyEditRequestVM.setApplyEndTime( DateTimeUtil.dateTimeFullFormat(apply.getApplyEndTime()) );

        return applyEditRequestVM;
    }
}
