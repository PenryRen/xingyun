package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.CredentialTemplate;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.domain.frame.CredentialItemFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.credential.CredentialEditRequestVM;
import com.mindskip.wdd.viewmodel.credential.CredentialEditResponseVM;
import com.mindskip.wdd.viewmodel.credential.CredentialItemVM;
import com.mindskip.wdd.viewmodel.credential.CredentialPageResponseVM;
import com.mindskip.wdd.viewmodel.credential.CredentialUserPageResponseVM;
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
public class CredentialMappingImpl implements CredentialMapping {

    @Override
    public CredentialPageResponseVM toCredentialPageResponseVM(CredentialTemplate credentialTemplate) {
        if ( credentialTemplate == null ) {
            return null;
        }

        CredentialPageResponseVM credentialPageResponseVM = new CredentialPageResponseVM();

        credentialPageResponseVM.setCompany( credentialTemplate.getCompany() );
        credentialPageResponseVM.setCreateUser( credentialTemplate.getCreateUser() );
        credentialPageResponseVM.setId( credentialTemplate.getId() );
        credentialPageResponseVM.setName( credentialTemplate.getName() );
        credentialPageResponseVM.setTemplateImagePath( credentialTemplate.getTemplateImagePath() );

        credentialPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(credentialTemplate.getCreateTime()) );

        return credentialPageResponseVM;
    }

    @Override
    public CredentialEditResponseVM toCredentialEditResponseVM(CredentialTemplate credentialTemplate) {
        if ( credentialTemplate == null ) {
            return null;
        }

        CredentialEditResponseVM credentialEditResponseVM = new CredentialEditResponseVM();

        credentialEditResponseVM.setCompany( credentialTemplate.getCompany() );
        credentialEditResponseVM.setId( credentialTemplate.getId() );
        credentialEditResponseVM.setName( credentialTemplate.getName() );
        credentialEditResponseVM.setTemplateImagePath( credentialTemplate.getTemplateImagePath() );

        return credentialEditResponseVM;
    }

    @Override
    public CredentialTemplate toCredentialTemplate(CredentialEditRequestVM credentialEditRequestVM) {
        if ( credentialEditRequestVM == null ) {
            return null;
        }

        CredentialTemplate credentialTemplate = new CredentialTemplate();

        credentialTemplate.setCompany( credentialEditRequestVM.getCompany() );
        credentialTemplate.setId( credentialEditRequestVM.getId() );
        credentialTemplate.setName( credentialEditRequestVM.getName() );
        credentialTemplate.setTemplateImagePath( credentialEditRequestVM.getTemplateImagePath() );

        return credentialTemplate;
    }

    @Override
    public void mapCredentialTemplate(CredentialEditRequestVM credentialEditRequestVM, CredentialTemplate credentialTemplate) {
        if ( credentialEditRequestVM == null ) {
            return;
        }

        credentialTemplate.setCompany( credentialEditRequestVM.getCompany() );
        credentialTemplate.setId( credentialEditRequestVM.getId() );
        credentialTemplate.setName( credentialEditRequestVM.getName() );
        credentialTemplate.setTemplateImagePath( credentialEditRequestVM.getTemplateImagePath() );
    }

    @Override
    public CredentialItemFrame toCredentialItemFrame(CredentialItemVM credentialItemVM) {
        if ( credentialItemVM == null ) {
            return null;
        }

        CredentialItemFrame credentialItemFrame = new CredentialItemFrame();

        credentialItemFrame.setElementLeft( credentialItemVM.getElementLeft() );
        credentialItemFrame.setElementTop( credentialItemVM.getElementTop() );
        credentialItemFrame.setElementX( credentialItemVM.getElementX() );
        credentialItemFrame.setElementY( credentialItemVM.getElementY() );
        credentialItemFrame.setFontColor( credentialItemVM.getFontColor() );
        credentialItemFrame.setFontSize( credentialItemVM.getFontSize() );
        credentialItemFrame.setFontWeight( credentialItemVM.getFontWeight() );
        credentialItemFrame.setKey( credentialItemVM.getKey() );
        credentialItemFrame.setText( credentialItemVM.getText() );
        credentialItemFrame.setType( credentialItemVM.getType() );
        credentialItemFrame.setValidityMonth( credentialItemVM.getValidityMonth() );

        return credentialItemFrame;
    }

    @Override
    public List<CredentialItemFrame> toCredentialItemFrameList(List<CredentialItemVM> credentialItemVMList) {
        if ( credentialItemVMList == null ) {
            return null;
        }

        List<CredentialItemFrame> list = new ArrayList<CredentialItemFrame>( credentialItemVMList.size() );
        for ( CredentialItemVM credentialItemVM : credentialItemVMList ) {
            list.add( toCredentialItemFrame( credentialItemVM ) );
        }

        return list;
    }

    @Override
    public CredentialItemVM toCredentialItemVM(CredentialItemFrame credentialItemFrame) {
        if ( credentialItemFrame == null ) {
            return null;
        }

        CredentialItemVM credentialItemVM = new CredentialItemVM();

        credentialItemVM.setElementLeft( credentialItemFrame.getElementLeft() );
        credentialItemVM.setElementTop( credentialItemFrame.getElementTop() );
        credentialItemVM.setElementX( credentialItemFrame.getElementX() );
        credentialItemVM.setElementY( credentialItemFrame.getElementY() );
        credentialItemVM.setFontColor( credentialItemFrame.getFontColor() );
        credentialItemVM.setFontSize( credentialItemFrame.getFontSize() );
        credentialItemVM.setFontWeight( credentialItemFrame.getFontWeight() );
        credentialItemVM.setKey( credentialItemFrame.getKey() );
        credentialItemVM.setText( credentialItemFrame.getText() );
        credentialItemVM.setType( credentialItemFrame.getType() );
        credentialItemVM.setValidityMonth( credentialItemFrame.getValidityMonth() );

        return credentialItemVM;
    }

    @Override
    public List<CredentialItemVM> toCredentialItemVMList(List<CredentialItemFrame> credentialItemFrameList) {
        if ( credentialItemFrameList == null ) {
            return null;
        }

        List<CredentialItemVM> list = new ArrayList<CredentialItemVM>( credentialItemFrameList.size() );
        for ( CredentialItemFrame credentialItemFrame : credentialItemFrameList ) {
            list.add( toCredentialItemVM( credentialItemFrame ) );
        }

        return list;
    }

    @Override
    public CredentialUserPageResponseVM toCredentialUserPageResponseVM(UserCredential userCredential) {
        if ( userCredential == null ) {
            return null;
        }

        CredentialUserPageResponseVM credentialUserPageResponseVM = new CredentialUserPageResponseVM();

        credentialUserPageResponseVM.setCredentialImagePath( userCredential.getCredentialImagePath() );
        credentialUserPageResponseVM.setId( userCredential.getId() );

        credentialUserPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(userCredential.getCredentialBuildTime()) );

        return credentialUserPageResponseVM;
    }
}
