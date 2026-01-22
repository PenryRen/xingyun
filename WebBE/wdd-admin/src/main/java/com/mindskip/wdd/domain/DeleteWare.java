package com.mindskip.wdd.domain;

import java.util.List;

public class DeleteWare {
    private String vmName;

    private List<String> volumes;

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public List<String> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<String> volumes) {
        this.volumes = volumes;
    }
}
