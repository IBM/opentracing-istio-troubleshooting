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

import com.sebastian_daschner.instrument_craft_shop.control.MakerBot;
import com.sebastian_daschner.instrument_craft_shop.entity.Instrument;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class InstrumentCraftShop {

    @Inject
    MakerBot makerBot;

    public void craftInstrument(Instrument instrument, String reqId, String parentspan, int instrument_count) {
        makerBot.printInstrument(instrument.getType(), reqId, parentspan, instrument_count);

        System.out.println("printing instrument " + instrument.getType() + " with price $" + instrument.getPrice());
    }

}
