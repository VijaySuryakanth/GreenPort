/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.visionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.oci.visionservice.model.Containers;
import com.oracle.pic.ocas.vision.AIServiceVisionClient;
import com.oracle.pic.ocas.vision.model.AnalyzeImageDetails;
import com.oracle.pic.ocas.vision.model.ImageClassificationFeature;
import com.oracle.pic.ocas.vision.model.ImageFeature;
import com.oracle.pic.ocas.vision.model.ImageObjectDetectionFeature;
import com.oracle.pic.ocas.vision.model.ImageTextDetectionFeature;
import com.oracle.pic.ocas.vision.model.InlineImageDetails;
import com.oracle.pic.ocas.vision.requests.AnalyzeImageRequest;
import com.oracle.pic.ocas.vision.responses.AnalyzeImageResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VKOKATNU
 */
public class ImageAnalyzer {

    private static final String ENDPOINT = "https://vision.aiservice.us-ashburn-1.oci.oraclecloud.com";
    private static final String REGION = "us-ashburn-1";

    //private final static String CONFIG_LOCATION = "~/.oci/config";
    private final static String CONFIG_LOCATION = "C:/lift/SRs/dockersh/dev-sdk/keys/config/dev-oci-config";
    private final static String CONFIG_PROFILE = "DEFAULT";

    /**
     * 
     * @return
     * @throws Exception 
     */
    public String process(String filename) throws Exception {
        System.out.println("Start Running AnalyzeImage ...");

        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(CONFIG_LOCATION, CONFIG_PROFILE);
        final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

        // Set up AI Service Vision client with credentials and endpoint/region
        final AIServiceVisionClient aiServiceVisionClient = new AIServiceVisionClient(provider);
        //aiServiceVisionClient.setEndpoint(ENDPOINT);
        aiServiceVisionClient.setRegion(REGION);

        // Read image file from resources folder
        byte[] bytes;
        //bytes = Files.readAllBytes(Paths.get("src/main/resources/cat.jpg"));
        //bytes = Files.readAllBytes(Paths.get("src/main/resources/documents/container03.jpg"));
        bytes = Files.readAllBytes(Paths.get("src/main/resources/documents/"+filename));

        // Now let's add ImageFeature features into a list, you can add multiple features if you want
        List<ImageFeature> features = new ArrayList<>();
        ImageFeature classifyFeature = ImageClassificationFeature.builder()
                .maxResults(10)
                .build();
        ImageFeature detectImageFeature = ImageObjectDetectionFeature.builder()
                .maxResults(10)
                .build();
        ImageFeature textDetectImageFeature = ImageTextDetectionFeature.builder().build();

        features.add(classifyFeature);
        features.add(detectImageFeature);
        features.add(textDetectImageFeature);

        // Let's wrap image bytes into InlineDocumentDetails
        InlineImageDetails inlineImageDetails = InlineImageDetails.builder()
                .data(bytes)
                .build();

        // Now include everything in AnalyzeImageDetails body
        AnalyzeImageDetails analyzeImageDetails = AnalyzeImageDetails.builder()
                .image(inlineImageDetails)
                .features(features)
                .build();

        // Build request, send, and get response
        AnalyzeImageRequest request = AnalyzeImageRequest.builder()
                .analyzeImageDetails(analyzeImageDetails)
                .build();

        AnalyzeImageResponse response = aiServiceVisionClient.analyzeImage(request);

        // Parse response
        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));

        String json = mapper.writeValueAsString(response.getAnalyzeImageResult());
        System.out.println("AnalyzeImage Result");
        //System.out.println(json);
        //write2File(json);        
        return json;
    }

    /**
     * 
     */
    private void service(){
        try {            
            String json = process("container00.jpg");
            ParseJson parseJson = new ParseJson();
            Containers container = parseJson.parseJson2Container(json);
            new DBService().saveData(container);
        } catch (Exception ex) {
            Logger.getLogger(ImageAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        try {
            System.err.println("Processing main...");
            ImageAnalyzer analyze = new ImageAnalyzer();
            analyze.service();
            System.err.println("Done!");
        } catch (Exception ex) {
            Logger.getLogger(ImageAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
