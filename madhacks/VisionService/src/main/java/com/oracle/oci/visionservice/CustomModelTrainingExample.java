/**
 * Copyright (c) 2016, 2021, Oracle and/or its affiliates.  All rights reserved.
 * This software is dual-licensed to you under the Universal Permissive License (UPL) 1.0 as shown at https://oss.oracle.com/licenses/upl or Apache License 2.0 as shown at http://www.apache.org/licenses/LICENSE-2.0. You may choose either license.
 */
package com.oracle.oci.visionservice;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.pic.ocas.vision.AIServiceVisionClient;
import com.oracle.pic.ocas.vision.model.CreateModelDetails;
import com.oracle.pic.ocas.vision.model.CreateProjectDetails;
import com.oracle.pic.ocas.vision.model.Model;
import com.oracle.pic.ocas.vision.model.ObjectStorageDataset;
import com.oracle.pic.ocas.vision.model.OperationStatus;
import com.oracle.pic.ocas.vision.model.Project;
import com.oracle.pic.ocas.vision.requests.CreateModelRequest;
import com.oracle.pic.ocas.vision.requests.CreateProjectRequest;
import com.oracle.pic.ocas.vision.requests.GetModelRequest;
import com.oracle.pic.ocas.vision.requests.GetProjectRequest;
import com.oracle.pic.ocas.vision.requests.GetWorkRequestRequest;
import com.oracle.pic.ocas.vision.responses.CreateModelResponse;
import com.oracle.pic.ocas.vision.responses.CreateProjectResponse;
import com.oracle.pic.ocas.vision.responses.GetModelResponse;
import com.oracle.pic.ocas.vision.responses.GetProjectResponse;
import com.oracle.pic.ocas.vision.responses.GetWorkRequestResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * This class provides an example of how to use OCI Vision Service to manage custom model training.
 * <p>
 * The Vision Service queried by this example will be assigned:
 * <ul>
 * <li>an endpoint url defined by constant ENDPOINT</li>
 * <li>
 * The configuration file used by service clients will be sourced from the default
 * location (~/.oci/config) and the CONFIG_PROFILE profile will be used.
 * </li>
 * </ul>
 * </p>
 * <p>
 * The sample attempts to create ObjectDetection custom model under newly created project. Model uses training dataset
 * in object storage configurable using following static variables.
 * <ul>
 * <li>an object storage namespace name defined by constant OBJECTSTORAGE_NAMESPACE</li>
 * <li>an object storage object bucket name defined by constant OBJECTSTORAGE_BUCKETNAME</li>
 * <li>an object storage object name defined by constant OBJECTSTORAGE_OBJECTNAME</li>
 * </ul>
 * </p>
 * <p>
 * Successful run of this sample will create a project and a model under the compartment defined by constant COMPARTMENT_ID.
 * </p>

 */
public class CustomModelTrainingExample {
    /*
    private static final String ENDPOINT = "https://vision-preprod.aiservice.us-ashburn-1.oci.oraclecloud.com";
    private static final String REGION = "us-ashburn-1";

    private static final String CONFIG_LOCATION = "~/.oci/config";
    private static final String CONFIG_PROFILE = "DEFAULT";
    private static final String COMPARTMENT_ID = "<enter-your-compartment-ocid-here"; //e.g. "ocid1.tenancy.oc1..aaaaaaaa6xo4q4r2l2nvcr3sl657pwla5k3xtbk2s6vgyrvxfuh4p66frooq";
    private static final String OBJECTSTORAGE_NAMESPACE ="<enter-your-objectstorage-namespsace-here>";//e.g. "axhheqi2ofpb";\
    private static final String OBJECTSTORAGE_BUCKETNAME = "<enter-your-bucket-name-here>";//e.g "transfer-learning-dataset"
    private static final String OBJECTSTORAGE_OBJECTNAME ="<enter-your-object-name-here>";// e.g. "od_coco69_v4.json";
    private static final double MAX_TRAINING_DURATION_IN_HOURS = 0.001;

    private static final int SECONDS_TO_SLEEP_BETWEEN_REQUESTS = 6;
    private static final int MAX_NUM_OF_ATTEMPTS = 40;

    */
    /**
     * The entry point for the custom model training example.
     *
     * @param args Arguments to provide to the example. This example expects no arguments.
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            throw new IllegalArgumentException(
                    "This example expects no argument");
        }
        System.out.println("Start Running ModelTrainingManager Example ...");

        // Configuring the AuthenticationDetailsProvider. It's assuming there is a default OCI config file
        // "~/.oci/config", and a profile in that config with the name defined in CONFIG_PROFILE variable.
        final ConfigFileReader.ConfigFile configFile =  ConfigFileReader.parse(Constants.CONFIG_LOCATION, Constants.CONFIG_PROFILE);
        final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

        // Set up AI Service Vision client with credentials and endpoint
        final AIServiceVisionClient aiServiceVisionClient = new AIServiceVisionClient(provider);
        //aiServiceVisionClient.setEndpoint(ENDPOINT);
        aiServiceVisionClient.setRegion(Constants.REGION);

        //Create a Project
        String projectName = UUID.randomUUID().toString();
        CreateProjectDetails details =
                CreateProjectDetails.builder()
                        .compartmentId(Constants.COMPARTMENT_ID)
                        .displayName(projectName)
                        .freeformTags(new HashMap<String, String>())
                        .definedTags(new HashMap<String, Map<String, Object>>())
                        .build();

        CreateProjectRequest request =
                CreateProjectRequest.builder().createProjectDetails(details).build();

        System.out.printf("Calling createProject with %s \n", request.getCreateProjectDetails().toString());
        CreateProjectResponse projectResponse = aiServiceVisionClient.createProject(request);
        System.out.printf("createProject projectResponse workRequestId: %s opcRequestId: %s \n",
                projectResponse.getOpcWorkRequestId(), projectResponse.getOpcRequestId());

        if (StringUtils.isBlank(projectResponse.getOpcWorkRequestId())) {
            throw new Exception("createProject call failed.");
        }
        System.out.println("createProject call succeeded.");

        if (!isWorkRequestSucceeded(aiServiceVisionClient, projectResponse.getOpcWorkRequestId())) {
            throw new Exception("createProject operation failed.");
        }
        System.out.println("createProject operation succeeded.\n");

        //Get newly created project
        final GetWorkRequestResponse workRequest = getWorkRequest(aiServiceVisionClient, projectResponse.getOpcWorkRequestId());
        final String projectId = workRequest.getWorkRequest().getResources().get(0).getIdentifier();
        GetProjectRequest getProjectRequest = GetProjectRequest.builder().projectId(projectId).build();
        GetProjectResponse getProjectResponse = null;
        try {
            System.out.printf("Calling getProject projectId %s \n", getProjectRequest.getProjectId());
            getProjectResponse = aiServiceVisionClient.getProject(getProjectRequest);
            System.out.printf("getProject response succeeded. opcRequestId: %s Project: %s \n\n",
                    getProjectResponse.getOpcRequestId(),
                    getProjectResponse.getProject());
        } catch (Exception e) {
            throw new Exception("Error occurred while trying to call getProject api", e);
        }
        Project project = getProjectResponse.getProject();

        //Create a Model (ObjectDetection)
        String modelName = UUID.randomUUID().toString();
        CreateModelDetails createModelDetails =
                CreateModelDetails.builder()
                        .projectId(project.getId())
                        .compartmentId(Constants.COMPARTMENT_ID)
                        .modelType(Model.ModelType.ObjectDetection)
                        .displayName(modelName)
                        .trainingDataset(ObjectStorageDataset.builder()
                                .namespace(Constants.OBJECTSTORAGE_NAMESPACE)
                                .bucketName(Constants.OBJECTSTORAGE_BUCKETNAME)
                                .objectName(Constants.OBJECTSTORAGE_OBJECTNAME)
                                .build())
                        .maxTrainingDurationInHours(Constants.MAX_TRAINING_DURATION_IN_HOURS)
                        .freeformTags(new HashMap<String, String>())
                        .definedTags(new HashMap<String, Map<String, Object>>())
                        .build();

        CreateModelRequest createModelRequest = CreateModelRequest.builder().createModelDetails(createModelDetails).build();

        System.out.printf("Calling createModel with %s \n", createModelRequest.getCreateModelDetails().toString());
        CreateModelResponse modelResponse = aiServiceVisionClient.createModel(createModelRequest);
        System.out.printf("createModel modelResponse workRequestId: %s opcRequestId: %s \n",
                modelResponse.getOpcWorkRequestId(), modelResponse.getOpcRequestId());

        if (StringUtils.isBlank(modelResponse.getOpcWorkRequestId())) {
            throw new Exception("createModel call failed.");
        }
        System.out.println("createModel call succeeded.");

        //Model creation is a long process, here we will only check if model creation has gone into inProgress state
        if (!isWorkRequestInProgress(aiServiceVisionClient, modelResponse.getOpcWorkRequestId())) {
            throw new Exception("createModel operation failed.");
        }
        System.out.println("createModel operation succeeded.\n");

        //Get newly created model
        final GetWorkRequestResponse modelWorkRequest = getWorkRequest(aiServiceVisionClient, modelResponse.getOpcWorkRequestId());
        final String modelId = modelWorkRequest.getWorkRequest().getResources().get(0).getIdentifier();
        GetModelRequest getModelRequest = GetModelRequest.builder().modelId(modelId).build();
        GetModelResponse getModelResponse = null;
        try {
            System.out.printf("Calling getModel modelId %s \n", getModelRequest.getModelId());
            getModelResponse = aiServiceVisionClient.getModel(getModelRequest);
            System.out.printf("getModel response succeeded. opcRequestId: %s Model: %s \n\n",
                    getModelResponse.getOpcRequestId(),
                    getModelResponse.getModel());
        } catch (Exception e) {
            throw new Exception("Error occurred while trying to call getModel api", e);
        }
    }

    private static  boolean isWorkRequestInStatus(AIServiceVisionClient client, String workRequestId, OperationStatus status){
        GetWorkRequestResponse response = null;
        try {
            for (int i = 0; i < Constants.MAX_NUM_OF_ATTEMPTS; i++) {
                TimeUnit.SECONDS.sleep(Constants.SECONDS_TO_SLEEP_BETWEEN_REQUESTS);
                response = getWorkRequest(client, workRequestId);

                if (response == null || response.getWorkRequest() == null) {
                    System.err.printf("Calling getWorkRequest returned null response or empty workRequest object %s \n",
                            workRequestId);
                    continue;
                }

                if (response.getWorkRequest().getStatus() == status) {
                    return true;
                } else if (response.getWorkRequest().getStatus() == OperationStatus.Failed
                        || response.getWorkRequest().getStatus() == OperationStatus.Canceled) {
                    return false;
                }
            }
        } catch (final InterruptedException ex) {
            System.err.println("WorkRequest polling was interrupted. \n");
            ex.printStackTrace();
        }

        System.err.println("WorkRequest timed out.");
        if (response != null) {
            System.err.printf("WorkRequest status: %s \n", response.getWorkRequest().getStatus());
        }
        return false;
    }

    private static boolean isWorkRequestInProgress(AIServiceVisionClient client, String workRequestId){
        return isWorkRequestInStatus(client, workRequestId, OperationStatus.InProgress);
    }

    private static boolean isWorkRequestSucceeded(
            AIServiceVisionClient client, String workRequestId) {
        return isWorkRequestInStatus(client, workRequestId, OperationStatus.Succeeded);
    }

    private static GetWorkRequestResponse getWorkRequest(AIServiceVisionClient client, String workRequestId) {
        GetWorkRequestRequest request = GetWorkRequestRequest.builder().workRequestId(workRequestId).build();

        GetWorkRequestResponse response = null;
        try {
            System.out.printf("Calling getWorkRequest with workRequestId: %s \n", request.getWorkRequestId());
            response = client.getWorkRequest(request);
            System.out.printf("getWorkRequest response opcRequestId: %s WorkRequest: %s \n\n",
                    response.getOpcRequestId(), response.getWorkRequest());
        } catch (Exception e) {
            System.err.println("Error occurred while trying to call getWorkRequest api");
            e.printStackTrace();
        }

        return response;
    }
}
