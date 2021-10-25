/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.visionservice.model;

/**
 *
 * @author VKOKATNU
 */
public class Containers {
    
    private String containerName;
    private String containerId;

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }   
    
    public Containers getContainer(){
        Containers container = new Containers();
        container.setContainerId("");
        
        return container;
    }
}
