package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ueit.VmWare;
import com.mindskip.wdd.domain.ueit.VmWareClone;
import com.mindskip.wdd.viewmodel.ueit.VmWareVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:39+0800",
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
}
