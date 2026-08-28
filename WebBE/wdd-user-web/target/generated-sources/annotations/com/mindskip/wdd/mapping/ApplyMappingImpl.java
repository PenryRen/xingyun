package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveVM;
import com.mindskip.wdd.viewmodel.apply.ApplyPageResponseVM;
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
public class ApplyMappingImpl implements ApplyMapping {

    @Override
    public ApplyPageResponseVM toApplyPageResponseVM(Apply apply) {
        if ( apply == null ) {
            return null;
        }

        ApplyPageResponseVM applyPageResponseVM = new ApplyPageResponseVM();

        applyPageResponseVM.setId( apply.getId() );
        applyPageResponseVM.setName( apply.getName() );

        applyPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(apply.getCreateTime()) );
        applyPageResponseVM.setApplyEndTime( DateTimeUtil.dateTimeFullFormat(apply.getApplyEndTime()) );
        applyPageResponseVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(apply.getLimitStartTime()) );
        applyPageResponseVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(apply.getLimitEndTime()) );

        return applyPageResponseVM;
    }

    @Override
    public ApplyArchiveVM toApplyArchiveVM(ApplyArchive applyArchive) {
        if ( applyArchive == null ) {
            return null;
        }

        ApplyArchiveVM applyArchiveVM = new ApplyArchiveVM();

        applyArchiveVM.setId( applyArchive.getId() );
        applyArchiveVM.setName( applyArchive.getName() );

        return applyArchiveVM;
    }

    @Override
    public List<ApplyArchiveVM> toApplyArchiveVMList(List<ApplyArchive> applyArchiveList) {
        if ( applyArchiveList == null ) {
            return null;
        }

        List<ApplyArchiveVM> list = new ArrayList<ApplyArchiveVM>( applyArchiveList.size() );
        for ( ApplyArchive applyArchive : applyArchiveList ) {
            list.add( toApplyArchiveVM( applyArchive ) );
        }

        return list;
    }
}
