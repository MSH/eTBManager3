/*
 * Webpack production configuration
 *
 * This file will generate the final javascript file to be deployed to the browser
 */
'use strict';
var webpack = require('webpack'),
    path = require('path'),
    config = require('./config'),
    I18nPlugin = require('i18n-webpack-plugin');

var langs = [ config.defaultLanguage ];

var contextPath = path.join( __dirname, config.clientSrc),
    outPath = path.join(__dirname, config.codeGenDistPath);


module.exports = langs.map( function(lang) {

    // get list of messages for the given language
    var messages = require('./messages/messages_' + lang + '.json');

    // return the configuration to be used in production mode
    return {
        context: contextPath,

        output: {
            filename: 'app.js',
            path: path.join( outPath, 'scripts', lang),
            publicPath: '/scripts/' + lang + '/'
        },

        cache: true,
        debug: true,
        devtool: false,
        entry: [
            path.join(contextPath, 'scripts', config.mainScript),
        ],

        stats: {
            colors: true,
            reasons: true
        },


        resolve: {
            extensions: ['', '.js', '.jsx']
        },

        module: {
            loaders: [
                {
                    test: /\.jsx/,
                    loader: 'babel-loader',
                    exclude: /node_modules/,
                    query: {
                        // https://github.com/babel/babel-loader#options
                        cacheDirectory: true,
                        presets: ['es2015', 'react']
                    }
                },
                {
                    test: /\.js/,
                    loader: 'babel-loader',
                    exclude: /node_modules/,
                    query: {
                        // https://github.com/babel/babel-loader#options
                        cacheDirectory: true,
                        presets: ['es2015', 'react']
                    }
                },
                {
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
                    test: /(?!fontawesome)\.(png|woff|woff2|eot|ttf|svg)$/,
                    loader: 'url-loader?limit=100000'
                },
                {   // font awesome fonts
                    test: /(?=.*fontawesome).*(?=.*\.woff(2))?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
                    loader: 'url-loader?limit=10000&minetype=application/font-woff'
                },
                {   // font awesome fonts
                    test: /(?=.*fontawesome).*\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
                    loader: 'file-loader'
                }
            ]
        },

        plugins: [
            new webpack.NoErrorsPlugin(),
            new webpack.DefinePlugin({
                __DEV__: false,
                'process.env.NODE_ENV': '"production"'
            }),
//            new webpack.optimize.CommonsChunkPlugin(/* chunkName= */'vendor', /* filename= */'../vendor.js'),
//            new webpack.optimize.UglifyJsPlugin({sourceMap: false}),
            new I18nPlugin(messages)
        ]
    }
});
