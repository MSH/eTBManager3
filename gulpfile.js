'use strict';


var gulp = require('gulp'),
    runSequence = require('run-sequence'),
    jshint = require('gulp-jshint'),
    webpack = require('webpack'),
    gutil = require('gulp-util'),
    del = require('del'),
    config = require('./src/main/client/config'),
    nodemon = require('gulp-nodemon'),
    open = require('gulp-open'),
    path = require('path');

// the client path
var clientPath = path.join(__dirname, 'src/main/client');


/**
 * Default task when none is specified in the gulp command line
 */
gulp.task('default', function() {
    return runSequence('build');
});


gulp.task('build', function() {
    return runSequence('clean', 'client-jshint', ['client-copy', 'webpack-dev']);
});


gulp.task('run', function() {
    return runSequence('client-jshint', 'proxy-server', 'open');
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
    .pipe(gulp.dest(config.dist));
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
gulp.task('webpack-prod', [], function(callback) {
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



/**
 * Open the browser after app has started
 */
gulp.task('open', function() {
    var options = {
        uri: 'http://localhost:3000/index.html'
    };

    return gulp.src(__filename)
        .pipe(open(options));
});


// just to avoid that the callback is called twice
var first = true;

/**
 * Run the application
 */
gulp.task('proxy-server', function(cb) {
    console.log('PATH = ' + clientPath);
    nodemon({
        script: 'server.js',
        cwd: path.join(clientPath, 'proxy'),
        env: {'NODE_ENV': 'development'},
//        tasks: ['server-lint'],
    }).on('start', function() {
        console.log('STARTED');
        if (first) {
            first = false;
            cb();
        }
    });
});
