/*
 * Webpack development server configuration
 *
 * This file is set up for serving the webpack-dev-server, which will watch for changes and recompile as required if
 * the subfolder /webpack-dev-server/ is visited. Visiting the root will not automatically reload.
 */
'use strict';
var webpack = require('webpack'),
    path = require('path'),
    config = require('./client/config');

var contextPath = path.join( __dirname, 'client', config.clientSrc),
    outPath = path.join(__dirname, 'client', config.distPath, 'scripts');

console.log('SOURCE = ' + contextPath);
console.log('OUTPUT = ' + outPath);

module.exports = {

    context: contextPath,

    output: {
        filename: 'main.js',
        path: outPath,
        publicPath: '/scripts/'
    },

    cache: true,
    debug: true,
    devtool: false,
    entry: [
        config.mainScript
    ],


    stats: {
        colors: true,
        reasons: true
    },


    module: {
        loaders: [{
            test: /\.jsx?$/,
            exclude: /node_modules/,
            loader: 'babel'
        }, {
            test: /\.less/,
            loader: 'style-loader!css-loader!less-loader'
        }, {
            test: /\.css$/,
            loader: 'style-loader!css-loader'
        }, {
            test: /\.(png|jpg)$/,
            loader: 'url-loader?limit=8192'
        },
        {
            test: /\.(png|woff|woff2|eot|ttf|svg)$/,
            loader: 'url-loader?limit=100000' }
        ]
    },

    plugins: [
        new webpack.NoErrorsPlugin(),
        new webpack.optimize.UglifyJsPlugin()
    ]

};
