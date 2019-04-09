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

import javax.ejb.Stateless;

import java.util.concurrent.locks.LockSupport;
import javax.inject.Inject;

@Stateless
public class MakerBot {

    @Inject
    Persist persist;

    public void print(String instrument, String reqId) {
        LockSupport.parkNanos(80_000_000L);

        long millis = (long) (Math.random() * 1000);

        try  {
            Thread.sleep(millis);
        }
        catch (Exception e) {}

        System.out.println("Printing a " + instrument + " after sleeping for " + millis + " ms");

        persist.persistInstrument(instrument, reqId);
    }

}
