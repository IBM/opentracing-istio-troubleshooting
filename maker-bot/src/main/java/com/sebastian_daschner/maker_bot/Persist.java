/*
Copyright 2019 IBM Corp. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */


package com.sebastian_daschner.maker_bot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class Persist {

    private Client client, pipeClient;
    private WebTarget target_persist;
    private WebTarget target_pipeline;

    String pipeline_url;
    String persist_url;

    @PostConstruct
    private void initClient() {
        int timeout = Integer.parseInt(System.getenv("READ_TIMEOUT"));
        System.out.println ("Read timeout: " + timeout);

        client = ClientBuilder.newBuilder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();
        
        pipeClient = ClientBuilder.newBuilder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();
        
        // Use URL in environment
        pipeline_url = System.getenv("PIPELINE_URL");
        persist_url = System.getenv("PERSIST_URL");
        System.out.println ("Using persistence and pipeline URLs from environment: " + persist_url + " : " + pipeline_url);
        target_persist = client.target(persist_url);
    }

    public void persistInstrument(String instrument, String reqId) {
        JsonObject requestBody = createRequestBody();
        sendRequest(requestBody, reqId);
    }

    private JsonObject createRequestBody() {
        return Json.createObjectBuilder()
                .add("instrument", "Instrument_type_goes_here")
                .build();
    }

    private void sendRequest(JsonObject requestBody, String reqId) {
        
        if (Math.random() < 0.2) {
            System.out.println("Kicking off processing pipeline to: " + pipeline_url + "/work"); 
            target_pipeline = pipeClient.target(pipeline_url + "/work");
        } else {
            System.out.println("Kicking off processing pipeline to: " + pipeline_url); 
            target_pipeline = pipeClient.target(pipeline_url);
        }

        Response r;
        try {
            r = target_pipeline.request().post(Entity.json(requestBody));
            validateResponse(r);
        } catch (Exception e) {
            throw e;
        }
        System.out.println("Recieved pipeline status: " + r.getStatus());
        if ( ! String.valueOf(r.getStatus()).startsWith("2") ) {
            System.out.println("Pipeline processing error");
        }
        try {
            System.out.println("Saving instrument to database");
            r = target_persist.request().post(Entity.json(requestBody));
            validateResponse(r);
        } catch (Exception e) {
            throw new IllegalStateException("Could not save instrument, reason: " + e.getMessage(), e);
        }
    }

    private void validateResponse(Response response) {
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
            throw new IllegalStateException("Could not perform request, status: " + response.getStatus());
    }    

    @PreDestroy
    private void closeClient() { 
        client.close();
        pipeClient.close();
    }
}
