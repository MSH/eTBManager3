'use strict';


var gulp = require('gulp'),
    runSequence = require('run-sequence'),
    eslint = require('gulp-eslint'),
    webpack = require('webpack'),
    gutil = require('gulp-util'),
    del = require('del'),
    config = require('./client/config'),
    nodemon = require('gulp-nodemon'),
    open = require('gulp-open'),
    path = require('path'),
    less = require('gulp-less'),
    uglify = require('gulp-uglify'),
    babel = require('gulp-babel');


// the client path
var clientPath = path.join(__dirname, 'client');
var srcPath = path.join(__dirname, 'client/src');
var distPath = path.join(__dirname, 'client', config.distPath);


/**
 * Default task when none is specified in the gulp command line
 */
gulp.task('default', function() {
    return runSequence('build');
});


/**
 * Build the client side for production mode
 */
gulp.task('build', function() {
    return runSequence(
        'clean',
        'eslint',
        ['msgs', 'bootstrap-fonts', 'copy', 'less'],
        'webpack-prod',
        // transpile is being called in order to be analysed by sonar
        'transpile');
});


/**
 * Prepare the client side for development and run the proxy server
 */
gulp.task('run', function() {
    return runSequence(
        'clean',
        ['msgs', 'bootstrap-fonts', 'less'],
        'watches',
        'proxy-server',
        'open'
        );
});


/**
 * Delete files from destination directory
 */
gulp.task('clean', function() {
    del( path.join(distPath, '/**/*'));
});


/**
 * Copy the files necessary for
 **/
gulp.task('bootstrap-fonts', function() {
    gulp.src([
        'node_modules/bootstrap/dist/fonts/**/*'
        ])
    .pipe(gulp.dest( path.join(srcPath, 'fonts')));
});


/**
 * Copy static files that are not automatically processed
 */
gulp.task('copy', function() {
    console.log(srcPath);
    console.log('TO -> ' + distPath);
    gulp.src([
        'favicon.ico',
        'images/**/*',
        'fonts/**/*'
        ], {cwd: srcPath})
    .pipe(gulp.dest( function(file) {
            // this script keep the relative path in src list when copied to dest
            var fname = path.relative(srcPath, path.dirname(file.path));
            fname = path.join(distPath, fname);
            return fname;
        }));
});


/**
 * Check JS quality of syntax in client code
 */
gulp.task('eslint', function() {
    return gulp.src(clientPath + '/src/scripts/**/*.{js,jsx}')
        .pipe(eslint())
        .pipe(eslint.format())
        .pipe(eslint.failAfterError());
});


/**
 * Generate the final java-script code
 */
gulp.task('webpack-prod', [], function(callback) {
    var webpackCompiler = webpack(require('./client/webpack.config'));

    webpackCompiler.run(function(err, stats) {
        if (err || stats.hasErrors()) {
            callback(err || stats.hasErrors());
        }
        gutil.log('[webpack:build-prod]', stats.toString({
            colors: true
        }));
        callback();
    });
});


/**
 * Convert the code from JSX syntax to pure java script
 */
gulp.task('transpile', [], function() {
    return gulp.src(clientPath + '/src/scripts/**/*.{js,jsx}')
        .pipe(babel({ presets: ['react'] }))
        .pipe(gulp.dest('target/client'));
});


/**
 * Open the browser after app has started
 */
gulp.task('open', function() {
    var options = {
        uri: 'http://localhost:' + config.proxy.port
    };

    // return gulp.src(__filename)
    //     .pipe(open(options));
});


// just to avoid that the callback is called twice
var first = true;

/**
 * Run the application
 */
gulp.task('proxy-server', function(cb) {
    nodemon({
        script: 'server.js',
        cwd: path.join(clientPath, 'proxy'),
        env: {'NODE_ENV': 'development'},
    }).on('start', function() {
        console.log('STARTED');
        if (first) {
            first = false;
            cb();
        }
    });
});


/**
 * Generate css style files from less files
 */
gulp.task('less', function() {
    return gulp.src( path.join(clientPath, 'less/theme.less'))
        .pipe(less({
            paths: [ path.join(__dirname, 'node_modules')]
        }))
        .pipe(gulp.dest( path.join(srcPath, 'styles')));
});


/**
 * Generate message files from the message files in server side
 */
gulp.task('msgs', function(cb) {
    var spawn = require('child_process').spawn;

    var dir = path.join(clientPath, 'messages');

    // node program to run
    var proc = spawn('node',
        [ path.join(dir, 'convert')],
        {
            cmd: dir
        }
    );

    proc.stdout.on('data', function (data) {
        console.log('[messages]: ' + data);
    });

    proc.stderr.on('data', function (data) {
        console.log('[messages]: ' + data);
    });

    proc.stdout.on('close', function(code) {
        console.log('[message]: FINISHED');
        cb(code);
    })
});


/**
 * Files to watch during development time
 **/
gulp.task('watches', function() {
    gulp.watch( path.join(clientPath, 'less/**/*') , ['less']);
    gulp.watch( path.join(clientPath, 'proxy/webpack-dev.config.js') , ['run']);
    gulp.watch( 'src/main/resources/messages*.properties', ['msgs', 'proxy-server']);
    gulp.watch( 'gulpfile.js', ['run']);
});