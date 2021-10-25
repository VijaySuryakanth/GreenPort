/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.visionservice;

/**
 *
 * @author VKOKATNU
 */
public class Constants {
    
    static final String ENDPOINT = "https://vision.aiservice.us-ashburn-1.oci.oraclecloud.com";
    static final String REGION = "us-ashburn-1";

    //final static String CONFIG_LOCATION = "~/.oci/config";
    final static String CONFIG_LOCATION = "C:/lift/SRs/dockersh/dev-sdk/keys/config/dev-oci-config";
    final static String CONFIG_PROFILE = "DEFAULT";

    static final String NAMESPACE_NAME = "frrudica1wgd";
    static final String BUCKET_NAME = "gp_bucket";
    static final String PREFIX = "result";
    
    final static String COMPARTMENT_ID = "ocid1.compartment.oc1..aaaaaaaawrvaz3qenxkroaq6zxnqfndsqvm4nylcxlnhpyp7mrbbo2pzhboa";
    static final String OBJECTSTORAGE_NAMESPACE ="frrudica1wgd";//e.g. "axhheqi2ofpb";\
    static final String OBJECTSTORAGE_BUCKETNAME = "gp_Store";//e.g "transfer-learning-dataset"
    static final String OBJECTSTORAGE_OBJECTNAME ="results";// e.g. "od_coco69_v4.json";
    static final double MAX_TRAINING_DURATION_IN_HOURS = 0.001;

    static final int SECONDS_TO_SLEEP_BETWEEN_REQUESTS = 6;
    static final int MAX_NUM_OF_ATTEMPTS = 40;
}
