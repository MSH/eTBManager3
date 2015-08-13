'use strict';

var express = require('express');
var httpProxy = require('http-proxy'),
    config = require('../config');

// We need to add a configuration to our proxy server,
// as we are now proxying outside localhost
var proxy = httpProxy.createProxyServer({
    changeOrigin: true
});

var app = express();


app.use(express.static(config.proxy.static));


// setup development proxy
app.all(config.proxy.dev.path, function (req, res) {
    proxy.web(req, res, {
        target: config.proxy.dev.url
    });
});


// setup webpack proxy
var bundle = require('./bundle.js');
bundle();
app.all(config.proxy.webpack.path, function (req, res) {
    proxy.web(req, res, {
        target: config.proxy.webpack.url
    });
});


proxy.on('error', function(e) {
    console.log('Could not connect to proxy, please try again...');
});

// start the server
app.listen(config.proxy.port, function () {
    console.log('Server running on port ' + config.proxy.port);
});
