/**
 * Convert the messages used in Java server side (iso 8859-1 format) to json format (utf-8)
 *
 * Created by rmemoria on 24/8/15.
 */


var fs = require('fs'),
    path = require('path'),
    PP = require('node-properties-parser'),
    Promise = require('bluebird');

/**
 * Source and destination folder
 */
var config = {

    // folder where messages_.properties files are
    src: path.join(__dirname, '../../src/main/resources'),

    // folder where messages_.json files will be created
    dest: path.join(__dirname, './')
}


// get the list of all files that start with 'messages_'
var files = fs.readdirSync(config.src).filter( function(fname) {
    return (fname.indexOf("messages_") === 0);
});


console.log('Message files found:');
console.log(files);

// default messages
var msgs;

(new Promise(function(resolve, reject) {
    // read the English file (default) with the keys
    PP.read( path.join(config.src, 'messages_en.properties'), function(err, res) {
        if (err) {
            reject(err);
        }
        else {
            msgs = res;
            resolve(res);
        }
    });
}))
.then(function() {

})
.then(function() {

    // Add all promises in a queue of promises
    return Promise.all(files.map(function(fname) {

        // get the language
        var lang = fname.replace('messages_', '').split('.').shift();

        // english language will be used as keys
        if (lang === 'en') {
            return;
        }

        // create a promise for every file to be converted
        return new Promise(function(resolve, reject) {

            // read the messages<lang>.properties file
            PP.read( path.join(config.src, fname), function(err, res) {
                if (err) {
                    reject(err);
                }
                else {
                    var obj = {};
                    // generate new configuration file based on the english language
                    for (var key in res) {
                        var val = res[key];
                        var newkey = msgs[key];
                        if (newkey) {
                            obj[newkey] = val;
                        }
                    }

                    // write the file with new keys
                    var fdest = path.join(config.dest, 'messages_' + lang + '.json');
                    fs.writeFile(fdest, JSON.stringify(obj, null, 4), function(err) {
                        if (err) {
                            reject(err);
                        }
                        else {
                            console.log('* Generated ' + fdest)
                            resolve();
                        }
                    });
                }
            })
        });

    }));

})
.then(function() {
    process.exit(0);
})
.catch(function(err) {
    console.log(err);
    process.exit(-1);
});


// Add all promises in a queue of promises
//Promise.all(files.map(function(fname) {
//
//    // get the language
//    var lang = fname.replace('messages_', '').split('.').shift();
//
//    // create a promise for every file to be converted
//    return new Promise(function(resolve, reject) {
//
//        // read the messages<lang>.properties file
//        PP.read( path.join(config.src, fname), function(err, res) {
//            if (err) {
//                reject(err);
//            }
//            else {
//                var fdest = path.join(config.dest, 'messages_' + lang + '.json');
//                fs.writeFileSync(fdest, JSON.stringify(res, null, 4));
//                console.log('* Generated ' + fdest)
//                resolve();
//            }
//        })
//    });
//
//}))
