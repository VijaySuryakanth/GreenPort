/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.stream2fn;

import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author VKOKATNU
 */
public class ProcessMessagefn {

    
    public String handleRequest(String message) {
        String result = "failed";
        System.err.println(" -- stream message --- "+message);
        
        try {   
            result = "message from fn task";
        } catch (Exception ex) {
            Logger.getLogger(ProcessMessagefn.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    public static void main(String[] args){
        System.err.println(" --- Processing main... ");
        new ProcessMessagefn().handleRequest("");
        System.err.println(" --- DONE!. ");
    }

}
