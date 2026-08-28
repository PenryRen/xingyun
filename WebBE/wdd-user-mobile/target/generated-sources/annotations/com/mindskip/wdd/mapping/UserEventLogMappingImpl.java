package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.user.event.UserEventPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:46+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserEventLogMappingImpl implements UserEventLogMapping {

    @Override
    public UserEventPageResponseVM toUserEventLogVM(UserEventLog userEventLog) {
        if ( userEventLog == null ) {
            return null;
        }

        UserEventPageResponseVM userEventPageResponseVM = new UserEventPageResponseVM();

        userEventPageResponseVM.setContent( userEventLog.getContent() );
        if ( userEventLog.getId() != null ) {
            userEventPageResponseVM.setId( userEventLog.getId().intValue() );
        }

        userEventPageResponseVM.setCreateTime( DateTimeUtil.dateFormat(userEventLog.getCreateTime()) );

        return userEventPageResponseVM;
    }
}
