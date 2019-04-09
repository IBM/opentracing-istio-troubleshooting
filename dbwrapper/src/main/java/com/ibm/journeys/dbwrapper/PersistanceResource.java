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

package com.ibm.journeys.dbwrapper;

import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Enumeration;


@Path("persist")
public class PersistanceResource {

    @POST
    public void instrument(JsonObject jsonObject, @Context HttpServletRequest request) {

        // Write instrument to a database
		logHeaders(request);
        String instrument = jsonObject.getString("instrument", null);

        long millis = (long) (Math.random() * 1000);

        // Simulate potentially wrong running call to database.
        System.out.println("Persisting " + instrument + " to data store.");
        try  {
            Thread.sleep(millis);
        }
        catch (Exception e) {}
    }

    private void logHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            System.out.println(header + ": " + request.getHeader(header));
        }
        System.out.println();
    }
}
