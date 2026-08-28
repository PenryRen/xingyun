package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.user.FeedbackPageResponseVM;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogPageResponseVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:39+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserEventLogMappingImpl implements UserEventLogMapping {

    @Override
    public UserEventLogPageResponseVM toUserEventLogResponseVM(UserEventLog userEventLog) {
        if ( userEventLog == null ) {
            return null;
        }

        UserEventLogPageResponseVM userEventLogPageResponseVM = new UserEventLogPageResponseVM();

        userEventLogPageResponseVM.setContent( userEventLog.getContent() );
        userEventLogPageResponseVM.setId( userEventLog.getId() );
        userEventLogPageResponseVM.setUserId( userEventLog.getUserId() );
        userEventLogPageResponseVM.setUserName( userEventLog.getUserName() );

        userEventLogPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(userEventLog.getCreateTime()) );

        return userEventLogPageResponseVM;
    }

    @Override
    public List<UserEventLogPageResponseVM> toUserEventLogPageResponseVMList(List<UserEventLog> userEventLogList) {
        if ( userEventLogList == null ) {
            return null;
        }

        List<UserEventLogPageResponseVM> list = new ArrayList<UserEventLogPageResponseVM>( userEventLogList.size() );
        for ( UserEventLog userEventLog : userEventLogList ) {
            list.add( toUserEventLogResponseVM( userEventLog ) );
        }

        return list;
    }

    @Override
    public FeedbackPageResponseVM toFeedbackPageResponseVM(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }

        FeedbackPageResponseVM feedbackPageResponseVM = new FeedbackPageResponseVM();

        feedbackPageResponseVM.setContact( feedback.getContact() );
        feedbackPageResponseVM.setContent( feedback.getContent() );
        if ( feedback.getId() != null ) {
            feedbackPageResponseVM.setId( feedback.getId().longValue() );
        }

        feedbackPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(feedback.getCreateTime()) );

        return feedbackPageResponseVM;
    }
}
