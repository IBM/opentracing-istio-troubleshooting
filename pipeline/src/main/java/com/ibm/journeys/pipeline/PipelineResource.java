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

package com.ibm.journeys.pipeline;

import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Enumeration;
import javax.inject.Inject;
import java.util.ArrayList;
import javax.ws.rs.core.Response;

@Path("process")
public class PipelineResource {

    String reqId;
    @Inject
    NextNode nn;

    @POST
    @Path("/work")
    public Response doWork(JsonObject jsonObject, @Context HttpServletRequest request) throws Exception {

        logHeaders(request);
        String instrument = jsonObject.getString("instrument", null);
        try {        
            String work_time = System.getenv("WORK_TIME");
            long millis = (long) (Math.random() * (Integer.parseInt(work_time) * 10000));
            System.out.println("doWork process time = " + millis + " ms");

            Thread.sleep(millis);
            System.out.println("processing " + instrument + " after sleeping for " + millis + " ms");
            nn.nextStep(jsonObject);
        } catch (Throwable t) {
            System.out.println("Oops");
            t.printStackTrace();
            throw t;
        }
    
        return  Response.ok("Pipeline step completed.").build();
    }

    @POST
    public Response doProcess(JsonObject jsonObject, @Context HttpServletRequest request) throws Exception {
        try {
			// Do something with the payload and call next in chain
			logHeaders(request);
			String instrument = jsonObject.getString("instrument", null);

			String work_time = System.getenv("WORK_TIME");
			String error_rate = System.getenv("ERROR_RATE");
			System.out.println("Error rate: " + error_rate);

			System.out.println("Starting processing node.");

			float err = Float.parseFloat(error_rate);
			System.out.println("Max work time (secs) = " + work_time + " Error rate: " + err);
			long millis = (long) (Math.random() * (Integer.parseInt(work_time) * 1000));
			System.out.println("Work time = " + millis + " ms");

			// Simulate error conditions for purposes of tracing demonstration.
			if (Math.random() < err) {
				throw new IllegalArgumentException("Illegal method argument");
			}

			// Element 
			if (Math.random() < err) {
				ArrayList<Integer> list = new ArrayList();
				list.get(4);
			}

			// NPE
			if (Math.random() < err) {
				ArrayList<Integer> list = null;
				list.get(4);
            }
            
            // Long delay in sync process
			if (Math.random() < err) {
                millis = millis * 20;
            }

			Thread.sleep(millis);
			System.out.println("Processing " + instrument + " after sleeping for " + millis + " ms");
			nn.nextStep(jsonObject);

		} catch (Throwable t) {
			System.out.println("Oops!");
			t.printStackTrace();
            throw t;
		}
        return  Response.ok("Step completed.").build();
    }

    private void logHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            System.out.println(header + ": " + request.getHeader(header));
			if(header.equals("x-request-id")) {
				reqId = request.getHeader(header);
			}
        }
        System.out.println();
    }
}
