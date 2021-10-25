/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.visionservice;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oracle.oci.visionservice.model.Containers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VKOKATNU
 */
public class ParseJson {


    /**
     * 
     * @param jsonStr 
     */
    public Containers parseJson2Container(String jsonStr) {
        System.out.println(" -- parseJson2Container -- ");
        JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
        Set<String> keys = jsonObject.keySet();
        System.out.println(keys);
        Containers container = new Containers();
        for (String key : keys) {
            if (null != key && key.equalsIgnoreCase("ontologyClasses")) {
                JsonElement element = jsonObject.get(key);
                if (element.isJsonArray()) {
                    JsonArray ontoArrayObj = element.getAsJsonArray();
                    JsonObject jsonObj = ontoArrayObj.get(0).getAsJsonObject();
                    container.setContainerName(jsonObj.get("name").getAsString());
                    System.out.println(" -- NAME: " + container.getContainerName());
                }
            }
            if (null != key && key.equalsIgnoreCase("imageText")) {
                JsonObject imgJsonObj = jsonObject.getAsJsonObject("imageText");
                JsonElement wordsEle = imgJsonObj.get("words");
                //JsonElement element = wordsEle.getAsJsonObject("");
                if (wordsEle.isJsonArray()) {
                    JsonArray ontoArrayObj = wordsEle.getAsJsonArray();
                    StringBuilder builder = new StringBuilder();
                    for(int i = 0; i < 3; ++i){
                        //JsonObject jsonObj = ontoArrayObj.get(i).getAsJsonObject();
                        JsonElement jsonEle = ontoArrayObj.get(i);
                        if(null != jsonEle){
                            //System.out.println(" -- jsonEle -- " + jsonEle);
                            JsonObject jsonObj = jsonEle.getAsJsonObject();
                            builder.append(jsonObj.get("text").getAsString());
                            builder.append(" ");
                        }
                    }
                    container.setContainerId(builder.toString());
                    System.out.println(" -- ID: " +container.getContainerId());
                    /*
                    JsonObject ele01 = ontoArrayObj.get(0).getAsJsonObject();
                    JsonObject ele02 = ontoArrayObj.get(1).getAsJsonObject();
                    JsonObject ele03 = ontoArrayObj.get(2).getAsJsonObject();
                    System.out.println(" -- " + ele01.get("text") + " " + ele02.get("text") + " " + ele03.get("text"));
                     */
                }
            }
        }
        return container;
    }

    /**
     *
     * @param fileContent
     */
    public void write2File(String fileContent) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("src/main/resources/documents/result.txt"));
            writer.write(fileContent);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ImageAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @return
     */
    public String readFile() {
        BufferedReader reader = null;
        StringBuilder resultStringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader("src/main/resources/documents/result.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (Exception ex) {
            Logger.getLogger(ParseJson.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ParseJson.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resultStringBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            ParseJson parserJson = new ParseJson();
            String json = parserJson.readFile();
            parserJson.parseJson2Container(json);
            System.out.println(" Done! ");
        } catch (Exception ex) {
            Logger.getLogger(ParseJson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
