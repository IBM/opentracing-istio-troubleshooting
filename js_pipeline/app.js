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

var express = require("express");
var bodyParser = require('body-parser');
var sender = require('request');
var axios = require('axios');
var log4js = require('log4js');

var logger = log4js.getLogger();
logger.level = 'debug';

/* end of dependency setup */

var port = process.env.PORT || 8080;

var app = express();

app.use(function(req, res, next) {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
  next();
});

app.post('/process_js', function(req, res) {

    var b3headers = ["x-b3-traceid",
                     "x-b3-spanid",
                     "x-b3-sampled",
                     "x-b3-parentspanid"];
    axios.defaults.headers.post = {};
    b3headers.forEach(function(hdr) {
        console.log(hdr);
        var value = req.headers[hdr];
        if (value) {
            console.log("Copying " + hdr + "=" + value + " to POST header.")
            axios.defaults.headers.post[hdr] = value;
        }
      });
    
    console.log('Running JS pipeline process');
    var path  = process.env.NEXT_STEP_URL;
    axios.post(path,{}).then((response) => {
	logger.debug("Finished pipeline call");

	console.log(response.data);
	console.log(response.status);
	console.log(response.statusText);
	console.log(response.headers);
	console.log(response.config);

	res.send(JSON.stringify({
	    outcome: "success"
	}, null, 3));
    }).catch(err => {console.log(err); res.send(); });
});


app.listen(port);
logger.debug("Listening on port ", port);
