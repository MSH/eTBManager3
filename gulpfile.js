'use strict';


var gulp = require('gulp'),
    runSequence = require('run-sequence'),
    jshint = require('gulp-jshint'),
    webpack = require('webpack'),
    gutil = require('gulp-util'),
    del = require('del'),
    config = require('./client/config'),
    nodemon = require('gulp-nodemon'),
    open = require('gulp-open'),
    path = require('path'),
    less = require('gulp-less'),
    uglify = require('gulp-uglify');


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
        'client-jshint',
        ['client-msgs', 'bootstrap-fonts', 'entry-point', 'client-copy', 'less'],
        'webpack-prod');
});


/**
 * Prepare the client side for development and run the proxy server
 */
gulp.task('run', function() {
    return runSequence(
        'clean',
        'client-jshint',
        ['client-msgs', 'entry-point', 'bootstrap-fonts', 'less'],
        'watches',
        'proxy-server',
        'open'
        );
});


/**
 * Delete files from destination directory
 */
gulp.task('clean', function(cb) {
    del( path.join(distPath, '/**/*'), cb);
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
gulp.task('client-copy', function() {
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
gulp.task('client-jshint', function() {
    return gulp.src(clientPath + 'src/scripts/**/*.js')
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
            throw new gutil.PluginError('webpack:build-prod', err);
        }
        gutil.log('[webpack:build-prod]', stats.toString({
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
 * Copy the unglified version of the entrypoint.js file to the server resources dir
 */
gulp.task('entry-point', function() {
    return gulp.src( path.join(clientPath, 'src/entrypoint.js'))
        .pipe(uglify())
        .pipe(gulp.dest('src/main/resources/templates'));
});



/**
 * Generate message files from the message files in server side
 */
gulp.task('client-msgs', function(cb) {
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
        console.log('[client-messages]: ' + data);
    });

    proc.stderr.on('data', function (data) {
        console.log('[client-messages]: ' + data);
    });

    proc.stdout.on('close', function(code) {
        cb(code);
    })
});


gulp.task('watches', function() {
    gulp.watch( path.join(clientPath, 'less/**/*') , ['less']);
    gulp.watch( path.join(clientPath, 'src/entrypoint.js') , ['entry-point']);
    gulp.watch( path.join(clientPath, 'proxy/webpack-dev.config.js') , ['run']);
    gulp.watch( 'src/main/resources/messages*.properties', ['client-msgs', 'proxy-server']);
    gulp.watch( path.join(clientPath, 'gulpfile.js') , ['run']);

});