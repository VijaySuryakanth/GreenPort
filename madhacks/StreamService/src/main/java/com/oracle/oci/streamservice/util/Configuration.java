/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.streamservice.util;

/**
 *
 * @author VKOKATNU
 */
public class Configuration {

    private String ociConfigPath;
    private String ociProfile;
    private String streamEndpoint;
    private String streamOCid;
    

    public Configuration() {
        this.ociConfigPath = ClassLoader.getSystemClassLoader().getResource("oci-config").getPath();
        //this.ociConfigPath = "/home/opc/.oci/oci-config";
        
        
        this.ociProfile = "MH_Tenancy_Profile";
        this.streamOCid = "ocid1.stream.oc1.ap-hyderabad-1.amaaaaaa5gpeaiyak6wlj44dxn3btqbsqvtvhyeaemf5rxnoftvyqwetlpva";
        this.streamEndpoint = "https://cell-1.streaming.ap-hyderabad-1.oci.oraclecloud.com";
        
        /*        
        this.ociProfile = "DEFAULT";
        this.streamOCid = "ocid1.stream.oc1.eu-frankfurt-1.amaaaaaazjgvoqyalhuzjkeew7wdnukh7nahaombgvx572hwtdd6bkvexpoq";
        this.streamEndpoint = "https://cell-1.streaming.eu-frankfurt-1.oci.oraclecloud.com";
        */
    }

    public Configuration(String ociConfigPath, String ociProfile, String streamOCId, String streamEndpoint) {
        this.ociConfigPath = ociConfigPath;
        this.ociProfile = ociProfile;
        this.streamOCid = streamOCId;
        this.streamEndpoint = streamEndpoint;
    }

    public String getOciConfigPath() {
        return ociConfigPath;
    }

    public void setOciConfigPath(String ociConfigPath) {
        this.ociConfigPath = ociConfigPath;
    }

    public String getOciProfile() {
        return ociProfile;
    }

    public void setOciProfile(String ociProfile) {
        this.ociProfile = ociProfile;
    }

    public String getStreamOCid() {
        return streamOCid;
    }

    public void setStreamOCid(String streamOCid) {
        this.streamOCid = streamOCid;
    }

    public String getStreamEndpoint() {
        return streamEndpoint;
    }

    public void setStreamEndpoint(String StreamEndpoint) {
        this.streamEndpoint = StreamEndpoint;
    }

}
