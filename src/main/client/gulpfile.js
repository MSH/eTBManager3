'use strict';


var gulp = require('gulp'),
    runSequence = require('run-sequence'),
    jshint = require('gulp-jshint'),
//    openb = require('gulp-open'),
    webpack = require('webpack'),
    gutil = require('gulp-util');


/**
 * Configuration file
 */
var config = {
    client: './client',
    dist: './dist'
};


/**
 * jshint in client code
 */
gulp.task('cli-jshint', function() {
    return gulp.src(config.client + '/**/*.js')
        .pipe(jshint())
        .pipe(jshint.reporter('jshint-stylish'));
});



/**
 * Default task when none is specified in the gulp command line
 */
gulp.task('default', function() {
    return runSequence('webpack-dev');
});


gulp.task('webpack-dev', [], function(callback) {
    var devCompiler = webpack(require('./webpack.config'));

    devCompiler.run(function(err, stats) {
        if(err) {
            throw new gutil.PluginError('webpack:build-dev', err);
        }
        gutil.log('[webpack:build-dev]', stats.toString({
            colors: true
        }));
        callback();
    });
});
