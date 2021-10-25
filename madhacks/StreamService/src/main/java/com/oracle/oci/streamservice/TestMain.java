/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.streamservice;

import com.oracle.bmc.ClientConfiguration;
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.streaming.StreamClient;
import com.oracle.bmc.streaming.model.PutMessagesDetails;
import com.oracle.bmc.streaming.model.PutMessagesDetailsEntry;
import com.oracle.bmc.streaming.requests.PutMessagesRequest;
import com.oracle.bmc.streaming.responses.PutMessagesResponse;
import com.oracle.oci.streamservice.util.Configuration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VKOKATNU
 */
public class TestMain {
    
    private final Configuration config;
    private final BasicAuthenticationDetailsProvider provider;
    
    public TestMain() throws IOException {
        config = new Configuration();//all default values set in constructor
        provider = new ConfigFileAuthenticationDetailsProvider(config.getOciConfigPath(), config.getOciProfile());
        
    }

    /**
     *
     */
    public void process() {
        System.out.println("com.oracle.oci.streams.Service.process() -- process - ");
        if (null == provider) {
            System.out.println("-- process -- authProvider not initialised -- ");
            return;
        }
        ClientConfiguration clientConfig = ClientConfiguration.builder()
                .connectionTimeoutMillis(180000)
                .readTimeoutMillis(180000)
                .build();
        /* Create a service client */
        StreamClient client = new StreamClient(provider, clientConfig);
        client.setEndpoint(config.getStreamEndpoint());
        
        //unique string to idetify message
        long number = new Date().getTime();
        String msgId = "mdhck/greenport/sensormsg/"+number;
        
        /* Create a request and dependent object(s). */ 
        PutMessagesDetails putMessagesDetails = PutMessagesDetails.builder()
                .messages(new ArrayList<>(Arrays.asList(PutMessagesDetailsEntry.builder()
                .key("SomeData".getBytes())
                .value("Some byte data - remote call".getBytes()).build()))).build();
        System.out.println("-- process -- " + putMessagesDetails.getMessages());
        
        PutMessagesRequest putMessagesRequest = PutMessagesRequest.builder()
                .streamId(config.getStreamOCid())
                .putMessagesDetails(putMessagesDetails)
                .opcRequestId(msgId).build();

        /* Send request to the Client */
        PutMessagesResponse response = client.putMessages(putMessagesRequest);
        
        System.out.println("com.oracle.oci.streams.Service.process() -- response - " + response.get__httpStatusCode__());
    }
    
    public static void main(String[] args) {
        
        try {
            System.err.println(" Processing main ...");
            TestMain test = new TestMain();
            test.process();

            System.err.println(" Done! ");
        } catch (IOException ex) {
            Logger.getLogger(TestMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
}
