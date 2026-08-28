package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.user.CurrentUserInfoVM;
import com.mindskip.wdd.viewmodel.user.FeedbackRequestVM;
import com.mindskip.wdd.viewmodel.user.LoginResponseVM;
import com.mindskip.wdd.viewmodel.user.RegisterRequestVM;
import com.mindskip.wdd.viewmodel.user.UpdateRequestVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:39+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserMappingImpl implements UserMapping {

    @Override
    public User toUser(RegisterRequestVM registerRequestVM) {
        if ( registerRequestVM == null ) {
            return null;
        }

        User user = new User();

        user.setDepartmentId( registerRequestVM.getDepartmentId() );
        user.setPassword( registerRequestVM.getPassword() );
        user.setRealName( registerRequestVM.getRealName() );
        user.setUserName( registerRequestVM.getUserName() );

        return user;
    }

    @Override
    public CurrentUserInfoVM toCurrentUserInfo(User user) {
        if ( user == null ) {
            return null;
        }

        CurrentUserInfoVM currentUserInfoVM = new CurrentUserInfoVM();

        currentUserInfoVM.setAge( user.getAge() );
        currentUserInfoVM.setEmail( user.getEmail() );
        currentUserInfoVM.setIdCard( user.getIdCard() );
        currentUserInfoVM.setImagePath( user.getImagePath() );
        currentUserInfoVM.setJobTitle( user.getJobTitle() );
        currentUserInfoVM.setPhone( user.getPhone() );
        currentUserInfoVM.setRealName( user.getRealName() );
        currentUserInfoVM.setSex( user.getSex() );
        currentUserInfoVM.setUserName( user.getUserName() );
        currentUserInfoVM.setWorkNo( user.getWorkNo() );

        currentUserInfoVM.setBirthDay( DateTimeUtil.dateFormat(user.getBirthDay()) );
        currentUserInfoVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(user.getCreateTime()) );

        return currentUserInfoVM;
    }

    @Override
    public User toUpdateUser(UpdateRequestVM updateRequestVM) {
        if ( updateRequestVM == null ) {
            return null;
        }

        User user = new User();

        user.setAge( updateRequestVM.getAge() );
        user.setEmail( updateRequestVM.getEmail() );
        user.setIdCard( updateRequestVM.getIdCard() );
        user.setImagePath( updateRequestVM.getImagePath() );
        user.setJobTitle( updateRequestVM.getJobTitle() );
        user.setPhone( updateRequestVM.getPhone() );
        user.setRealName( updateRequestVM.getRealName() );
        user.setSex( updateRequestVM.getSex() );
        user.setWorkNo( updateRequestVM.getWorkNo() );

        user.setBirthDay( DateTimeUtil.toDate(updateRequestVM.getBirthDay()) );

        return user;
    }

    @Override
    public LoginResponseVM toLoginVM(User user) {
        if ( user == null ) {
            return null;
        }

        LoginResponseVM loginResponseVM = new LoginResponseVM();

        loginResponseVM.setRealName( user.getRealName() );
        loginResponseVM.setUserName( user.getUserName() );

        return loginResponseVM;
    }

    @Override
    public Feedback toFeedback(FeedbackRequestVM FeedbackRequestVM) {
        if ( FeedbackRequestVM == null ) {
            return null;
        }

        Feedback feedback = new Feedback();

        feedback.setContact( FeedbackRequestVM.getContact() );
        feedback.setContent( FeedbackRequestVM.getContent() );

        return feedback;
    }
}
