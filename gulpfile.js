'use strict';


var gulp = require('gulp'),
    runSequence = require('run-sequence'),
    jshint = require('gulp-jshint'),
    webpack = require('webpack'),
    gutil = require('gulp-util'),
    del = require('del'),
    config = require('./client-config'),
    fs = require('fs');




/**
 * Default task when none is specified in the gulp command line
 */
gulp.task('default', function() {
    return runSequence('build');
});


gulp.task('build', function() {
    return runSequence('clean', 'client-jshint', ['client-copy', 'webpack-dev']);
});


/**
 * Delete files from destination directory
 */
gulp.task('clean', function(cb) {
    del(config.dist + '/**/*', cb);
});


/**
 * Copy static files that are not automatically processed
 */
gulp.task('client-copy', function() {
    gulp.src([
        'index.html',
        'favicon.ico'
        ], {cwd: config.client})
    .pipe(gulp.dest(config.dist))
});


/**
 * Check JS quality of syntax in client code
 */
gulp.task('client-jshint', function() {
    return gulp.src(config.client + '/scripts/**/*.js')
        .pipe(jshint())
        .pipe(jshint.reporter('jshint-stylish'));
});


/**
 * Generate the final java-script code
 */
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
