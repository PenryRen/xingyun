package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.excel.UserVM;
import com.mindskip.wdd.viewmodel.login.LoginResponseVM;
import com.mindskip.wdd.viewmodel.profile.ProfileInfoVM;
import com.mindskip.wdd.viewmodel.user.UserEditRequestVM;
import com.mindskip.wdd.viewmodel.user.UserEditResponseVM;
import com.mindskip.wdd.viewmodel.user.UserPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:38+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserMappingImpl implements UserMapping {

    @Override
    public UserPageResponseVM toUserPageResponseVM(User user) {
        if ( user == null ) {
            return null;
        }

        UserPageResponseVM userPageResponseVM = new UserPageResponseVM();

        userPageResponseVM.setAge( user.getAge() );
        userPageResponseVM.setDepartmentId( user.getDepartmentId() );
        userPageResponseVM.setEmail( user.getEmail() );
        userPageResponseVM.setId( user.getId() );
        userPageResponseVM.setIdCard( user.getIdCard() );
        userPageResponseVM.setImagePath( user.getImagePath() );
        userPageResponseVM.setJobTitle( user.getJobTitle() );
        userPageResponseVM.setPhone( user.getPhone() );
        userPageResponseVM.setRealName( user.getRealName() );
        userPageResponseVM.setSex( user.getSex() );
        userPageResponseVM.setStatus( user.getStatus() );
        userPageResponseVM.setUserName( user.getUserName() );
        userPageResponseVM.setUserUuid( user.getUserUuid() );
        userPageResponseVM.setWorkNo( user.getWorkNo() );

        userPageResponseVM.setBirthDay( DateTimeUtil.dateTimeFullFormat(user.getBirthDay()) );
        userPageResponseVM.setLastActiveTime( DateTimeUtil.dateTimeFullFormat(user.getLastActiveTime()) );
        userPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(user.getCreateTime()) );
        userPageResponseVM.setModifyTime( DateTimeUtil.dateTimeFullFormat(user.getModifyTime()) );

        return userPageResponseVM;
    }

    @Override
    public User toUser(UserEditRequestVM userEditRequestVM) {
        if ( userEditRequestVM == null ) {
            return null;
        }

        User user = new User();

        user.setAge( userEditRequestVM.getAge() );
        user.setCreateUser( userEditRequestVM.getCreateUser() );
        user.setDepartmentId( userEditRequestVM.getDepartmentId() );
        user.setEmail( userEditRequestVM.getEmail() );
        user.setId( userEditRequestVM.getId() );
        user.setIdCard( userEditRequestVM.getIdCard() );
        user.setImagePath( userEditRequestVM.getImagePath() );
        user.setJobTitle( userEditRequestVM.getJobTitle() );
        user.setPassword( userEditRequestVM.getPassword() );
        user.setPhone( userEditRequestVM.getPhone() );
        user.setRealName( userEditRequestVM.getRealName() );
        user.setRoleId( userEditRequestVM.getRoleId() );
        user.setSex( userEditRequestVM.getSex() );
        user.setStatus( userEditRequestVM.getStatus() );
        user.setSystemRole( userEditRequestVM.getSystemRole() );
        user.setUserName( userEditRequestVM.getUserName() );
        user.setWorkNo( userEditRequestVM.getWorkNo() );

        user.setBirthDay( DateTimeUtil.toDate(userEditRequestVM.getBirthDay()) );

        return user;
    }

    @Override
    public UserEditResponseVM toUserEditResponseVM(User user) {
        if ( user == null ) {
            return null;
        }

        UserEditResponseVM userEditResponseVM = new UserEditResponseVM();

        userEditResponseVM.setAge( user.getAge() );
        userEditResponseVM.setDepartmentId( user.getDepartmentId() );
        userEditResponseVM.setEmail( user.getEmail() );
        userEditResponseVM.setId( user.getId() );
        userEditResponseVM.setIdCard( user.getIdCard() );
        userEditResponseVM.setImagePath( user.getImagePath() );
        userEditResponseVM.setJobTitle( user.getJobTitle() );
        userEditResponseVM.setPhone( user.getPhone() );
        userEditResponseVM.setRealName( user.getRealName() );
        userEditResponseVM.setRoleId( user.getRoleId() );
        userEditResponseVM.setSex( user.getSex() );
        userEditResponseVM.setStatus( user.getStatus() );
        userEditResponseVM.setSystemRole( user.getSystemRole() );
        userEditResponseVM.setUserName( user.getUserName() );
        userEditResponseVM.setUserUuid( user.getUserUuid() );
        userEditResponseVM.setWorkNo( user.getWorkNo() );

        userEditResponseVM.setBirthDay( DateTimeUtil.dateTimeFullFormat(user.getBirthDay()) );

        return userEditResponseVM;
    }

    @Override
    public User toUser(UserVM userVM) {
        if ( userVM == null ) {
            return null;
        }

        User user = new User();

        user.setDepartmentId( userVM.getDepartmentId() );
        user.setEmail( userVM.getEmail() );
        user.setId( userVM.getId() );
        user.setIdCard( userVM.getIdCard() );
        user.setJobTitle( userVM.getJobTitle() );
        user.setPassword( userVM.getPassword() );
        user.setPhone( userVM.getPhone() );
        user.setRealName( userVM.getRealName() );
        user.setUserName( userVM.getUserName() );
        user.setWorkNo( userVM.getWorkNo() );

        return user;
    }

    @Override
    public ProfileInfoVM toAdminInfoVM(User user) {
        if ( user == null ) {
            return null;
        }

        ProfileInfoVM profileInfoVM = new ProfileInfoVM();

        profileInfoVM.setAge( user.getAge() );
        profileInfoVM.setEmail( user.getEmail() );
        profileInfoVM.setImagePath( user.getImagePath() );
        profileInfoVM.setPhone( user.getPhone() );
        profileInfoVM.setRealName( user.getRealName() );
        profileInfoVM.setSex( user.getSex() );
        profileInfoVM.setUserName( user.getUserName() );

        profileInfoVM.setLastActiveTime( DateTimeUtil.dateTimeFullFormat(user.getLastActiveTime()) );
        profileInfoVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(user.getCreateTime()) );

        return profileInfoVM;
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
}
