package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ueit.VmWare;
import com.mindskip.wdd.domain.ueit.VmWareClone;
import com.mindskip.wdd.viewmodel.ueit.VmWareImport;
import com.mindskip.wdd.viewmodel.ueit.VmWarePageRequestVM;
import com.mindskip.wdd.viewmodel.ueit.VmWareVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:39+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class VmWareMappingImpl implements VmWareMapping {

    @Override
    public VmWareVM toVmWareVM(VmWare vmWare) {
        if ( vmWare == null ) {
            return null;
        }

        VmWareVM vmWareVM = new VmWareVM();

        vmWareVM.setClasses( vmWare.getClasses() );
        vmWareVM.setExamPaperId( vmWare.getExamPaperId() );
        vmWareVM.setGuid( vmWare.getGuid() );
        vmWareVM.setVmName( vmWare.getVmName() );
        vmWareVM.setVmParentId( vmWare.getVmParentId() );

        return vmWareVM;
    }

    @Override
    public VmWareClone toVmWareClone(VmWare VmWare) {
        if ( VmWare == null ) {
            return null;
        }

        VmWareClone vmWareClone = new VmWareClone();

        vmWareClone.setUrl( VmWare.getUrl() );
        vmWareClone.setVmPassword( VmWare.getVmPassword() );
        vmWareClone.setVmUsername( VmWare.getVmUsername() );

        return vmWareClone;
    }

    @Override
    public VmWare toVmWare(VmWareClone vmWareClone) {
        if ( vmWareClone == null ) {
            return null;
        }

        VmWare vmWare = new VmWare();

        vmWare.setUrl( vmWareClone.getUrl() );
        vmWare.setVmPassword( vmWareClone.getVmPassword() );
        vmWare.setVmUsername( vmWareClone.getVmUsername() );

        return vmWare;
    }

    @Override
    public VmWare requestVMToVmWare(VmWarePageRequestVM requestVM) {
        if ( requestVM == null ) {
            return null;
        }

        VmWare vmWare = new VmWare();

        vmWare.setClasses( requestVM.getClasses() );
        vmWare.setStatus( requestVM.getStatus() );
        vmWare.setVmName( requestVM.getVmName() );
        vmWare.setVmType( requestVM.getVmType() );

        return vmWare;
    }

    @Override
    public VmWare toVmWareFromImport(VmWareImport vmWareImport) {
        if ( vmWareImport == null ) {
            return null;
        }

        VmWare vmWare = new VmWare();

        vmWare.setClasses( vmWareImport.getClasses() );
        vmWare.setCreateTime( vmWareImport.getCreateTime() );
        vmWare.setCreateUser( vmWareImport.getCreateUser() );
        vmWare.setDeleted( vmWareImport.getDeleted() );
        vmWare.setDisabled( vmWareImport.getDisabled() );
        vmWare.setDiskName( vmWareImport.getDiskName() );
        vmWare.setDiskSize( vmWareImport.getDiskSize() );
        vmWare.setExamPaperId( vmWareImport.getExamPaperId() );
        vmWare.setGuid( vmWareImport.getGuid() );
        vmWare.setId( vmWareImport.getId() );
        vmWare.setStatus( vmWareImport.getStatus() );
        vmWare.setUpdateTime( vmWareImport.getUpdateTime() );
        vmWare.setUpdateUser( vmWareImport.getUpdateUser() );
        vmWare.setUrl( vmWareImport.getUrl() );
        vmWare.setValidCreateTime( vmWareImport.getValidCreateTime() );
        vmWare.setValidEndTime( vmWareImport.getValidEndTime() );
        vmWare.setVmCpu( vmWareImport.getVmCpu() );
        vmWare.setVmIp( vmWareImport.getVmIp() );
        vmWare.setVmName( vmWareImport.getVmName() );
        vmWare.setVmParentId( vmWareImport.getVmParentId() );
        vmWare.setVmPassword( vmWareImport.getVmPassword() );
        vmWare.setVmStorage( vmWareImport.getVmStorage() );
        vmWare.setVmType( vmWareImport.getVmType() );
        vmWare.setVmUserId( vmWareImport.getVmUserId() );
        vmWare.setVmUsername( vmWareImport.getVmUsername() );

        return vmWare;
    }
}
