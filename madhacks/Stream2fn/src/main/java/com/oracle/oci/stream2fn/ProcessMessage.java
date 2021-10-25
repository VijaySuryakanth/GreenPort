/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.stream2fn;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
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
public class ProcessMessage {

    private ObjectStorageClient objStoreClient = null;

    public void handleRequest(String message) {
        String result = "failed";
        System.err.println(" -- stream message --- "+message);
        
        try {
            Configuration configuration = new Configuration();
            ConfigFileAuthenticationDetailsProvider provider = configuration.getConfigFileAuthProvider();
            //ResourcePrincipalAuthenticationDetailsProvider provider = ResourcePrincipalAuthenticationDetailsProvider.builder().build();
            objStoreClient = new ObjectStorageClient(provider);

            if (objStoreClient == null) {
                System.err.println("There was a problem creating the ObjectStorage Client object. Please check logs");
                return;
            }
            
            String nameSpace = "frrudica1wgd";
            
            ObjectInfo objectInfo = new ObjectInfo();
            
             GetObjectRequest gor = GetObjectRequest.builder()
                    .namespaceName(nameSpace)
                    .bucketName(objectInfo.getBucketName())
                    .objectName(objectInfo.getName())
                    .build();
            System.err.println("Getting content for object " + objectInfo.getName() + " from bucket " + objectInfo.getBucketName());

            GetObjectResponse response = objStoreClient.getObject(gor);
            
            result = new BufferedReader(new InputStreamReader(response.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));
            
            System.err.println(" -- respsonse -- "+result);
            
            StringBuffer buffer = new StringBuffer(result);
            buffer.append("\n");
            buffer.append(objectInfo.getContent());
            InputStream story = new ByteArrayInputStream(buffer.toString().getBytes());
            
            
            /*
            List<InputStream> streams = Arrays.asList(
                    new ByteArrayInputStream(result.getBytes()),
                    new ByteArrayInputStream("\n".getBytes()),
                    new ByteArrayInputStream(objectInfo.getContent().getBytes()));
            InputStream story = new SequenceInputStream(Collections.enumeration(streams));
            */
            System.err.println(" -- message -- "+story.toString());
            
            PutObjectRequest por = PutObjectRequest.builder()
                    .namespaceName(nameSpace)
                    .bucketName(objectInfo.getBucketName())
                    .objectName(objectInfo.getName())
                    .putObjectBody(story)
                    .build();

            PutObjectResponse poResp = objStoreClient.putObject(por);
            result = "Successfully submitted Put request for object " + objectInfo.getName() + " in bucket " + objectInfo.getBucketName() + ". OPC reuquest ID is " + poResp.getOpcRequestId();
            System.err.println(result);

        } catch (Exception ex) {
            Logger.getLogger(ProcessMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        System.err.println(" --- Processing main... ");
        new ProcessMessage().handleRequest("");
        System.err.println(" --- DONE!. ");
    }

}
