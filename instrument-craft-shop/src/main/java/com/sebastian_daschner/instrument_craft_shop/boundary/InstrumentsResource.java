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

package com.sebastian_daschner.instrument_craft_shop.boundary;

import com.sebastian_daschner.instrument_craft_shop.entity.Instrument;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.Enumeration;

@Path("instruments")
public class InstrumentsResource {

	String reqId;
	String parentspan = null;
	String currentspan = null;
	public int instrument_count = 1;
    @Inject
    InstrumentCraftShop instrumentCraftShop;

    @POST
    public void createInstrument(@Valid @NotNull Instrument instrument, @Context HttpServletRequest request) {
        logHeaders(request);
        instrumentCraftShop.craftInstrument(instrument, reqId, parentspan, instrument_count);
    }

    private void logHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            System.out.println(header + ": " + request.getHeader(header));
			if(header.equals("x-request-id")) {
				reqId = request.getHeader(header);
			}
			if (header.equals("x-b3-parentspanid")) {
				parentspan = request.getHeader(header);
				System.out.println("Set parentspan to existing value: " + parentspan);
			}
			if (header.equals("x-b3-spanid")) {
				currentspan = request.getHeader(header);
				System.out.println("currentspan: " + currentspan);
			}
			if (header.equals("count")) {
				instrument_count = Integer.parseInt(request.getHeader(header));
				System.out.println("Count: " + instrument_count);
				if (instrument_count < 1) {
					// Must print at least one instrument
					instrument_count = 1;
				}
			}
			System.out.println();
		}
		
		if (parentspan == null) {
			if (currentspan != null) 
				parentspan = currentspan;
			else
				parentspan = "";
			System.out.println("Set parentspanid to current span: " + parentspan);
		} // if (parentspan == null) 

	}
} // public class InstrumentsResource 
