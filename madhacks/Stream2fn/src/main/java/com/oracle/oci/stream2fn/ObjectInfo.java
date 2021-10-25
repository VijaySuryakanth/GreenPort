/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.stream2fn;

/**
 *
 * @author VKOKATNU
 */
public class ObjectInfo {

    private String name;
    private String bucketName;
    private String content;

    public ObjectInfo(){
        this.bucketName = "gp_store";
        this.name = "stream-msg";  
        this.content = " message posted from fn client";                
    }
    
    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
