/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.stream2fn;

import com.google.common.base.Supplier;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AbstractAuthenticationDetailsProvider;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author VKOKATNU
 */
public class Configuration {

    String tenantOCID;
    String userId;
    String fingerprint;
    String privateKeyFile;
    String passphrase;
    String region;
    String instanceOcid;
    String action;

    public Configuration() {
        this.tenantOCID = System.getenv("TENANT_OCID");
        this.userId = System.getenv("USER_OCID");
        this.fingerprint = System.getenv("PUBLIC_KEY_FINGERPRINT");
        this.privateKeyFile = System.getenv("PRIVATE_KEY_LOCATION");
        this.passphrase = System.getenv("PASSPHRASE");
        this.region = System.getenv("REGION");
        this.instanceOcid = System.getenv("INSTANCE_OCID");
        this.action = System.getenv("ACTION");

        System.out.println(" -- inside Configuration.java -- " + this.tenantOCID);
    }

    /**
     * 
     * @return
     * @throws Exception 
     */
    public ConfigFileAuthenticationDetailsProvider getConfigFileAuthProvider() throws Exception {
        
        ConfigFileAuthenticationDetailsProvider authDetailsProvider = null;
        if (null == this.tenantOCID) {
            String confFilePath = "C:/lift/SRs/dockersh/dev-sdk/keys/config/dev-oci-config";
            String profile = "DEFAULT";
            authDetailsProvider = new ConfigFileAuthenticationDetailsProvider(confFilePath, profile);            
        } 
        return authDetailsProvider;
    }
    
    /**
     * 
     * @return
     * @throws Exception 
     */
    public AuthenticationDetailsProvider authProvider() throws Exception {
        addLocalValues();//delete this line
        
        AuthenticationDetailsProvider authDetailsProvider = null;
        if (null == this.tenantOCID) {
            String confFilePath = "C:/lift/SRs/dockersh/dev-sdk/keys/config/dev-oci-config";
            String profile = "DEFAULT";
            authDetailsProvider = new ConfigFileAuthenticationDetailsProvider(confFilePath, profile);            
        } else {
            System.err.println(" -- SimpleAuthenticationDetailsProvider -- ");
            InputStream is = new ByteArrayInputStream(this.privateKeyFile.getBytes());
            Path temp = Files.createTempFile("pri_key", ".pem");
            Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);
            Supplier<InputStream> privateKeySupplier = () -> {
                try {
                    InputStream instrm = Thread.currentThread().getContextClassLoader().getResourceAsStream("user_api_private.pem");
                    return instrm;                   
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            };
            
            authDetailsProvider = SimpleAuthenticationDetailsProvider.builder()
                    .tenantId(this.tenantOCID)
                    .userId(this.userId)
                    .fingerprint(this.fingerprint)
                    .privateKeySupplier(privateKeySupplier)
                    .passPhrase(this.passphrase)
                    .region(Region.EU_FRANKFURT_1)
                    .build();           
            
        }
        return authDetailsProvider;
    }

    private void addLocalValues(){
        this.tenantOCID = "ocid1.tenancy.oc1..aaaaaaaaxn3r3dh3lh5n5hyo6tkbtmwjvr4wzlahs3u76c4enhckxhy4kpaa";
        this.userId = "ocid1.user.oc1..aaaaaaaakpa3klyfbdwprdej3tvov742attpkeb5rv6xp3y2poibwnwqn3bq";
        this.fingerprint = "05:61:63:8b:e5:4f:1c:a1:a9:58:85:64:d7:3f:0e:37";
        this.privateKeyFile = "";
        this.passphrase = "";
        this.region = "eu-frankfurt-1";
        this.instanceOcid = "ocid1.instance.oc1.eu-frankfurt-1.antheljtzjgvoqyckt7f34kpd2iivvei2wk26hfnlpgnm72ofw5fmg4uuohq";
        this.action = "START";      
    }
    
    
    public String getInstanceOcid() {
        return instanceOcid;
    }

    public void setInstanceOcid(String instanceOcid) {
        this.instanceOcid = instanceOcid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTenantOCID() {
        return tenantOCID;
    }

    public void setTenantOCID(String tenantOCID) {
        this.tenantOCID = tenantOCID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

}
