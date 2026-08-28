package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.credential.CredentialPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:47+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CredentialMappingImpl implements CredentialMapping {

    @Override
    public CredentialPageResponseVM toCredentialPageResponseVM(UserCredential userCredential) {
        if ( userCredential == null ) {
            return null;
        }

        CredentialPageResponseVM credentialPageResponseVM = new CredentialPageResponseVM();

        credentialPageResponseVM.setCredentialImagePath( userCredential.getCredentialImagePath() );
        credentialPageResponseVM.setId( userCredential.getId() );

        credentialPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(userCredential.getCreateTime()) );
        credentialPageResponseVM.setCredentialBuildTime( DateTimeUtil.dateTimeFullFormat(userCredential.getCredentialBuildTime()) );

        return credentialPageResponseVM;
    }
}
