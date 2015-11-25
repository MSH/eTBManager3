/**
 * Convert the messages used in Java server side (iso 8859-1 format) to json format (utf-8)
 *
 * Created by rmemoria on 24/8/15.
 */


var fs = require('fs'),
    path = require('path'),
    PP = require('node-properties-parser'),
    Promise = require('bluebird'),
    readline = require('readline');

/**
 * Source and destination folder
 */
var config = {

    // folder where messages_.properties files are
    src: path.join(__dirname, '../../src/main/resources'),

    // folder where messages_.json files will be created
    dest: path.join(__dirname, './')
};


// get the list of all files that start with 'messages_'
var files = fs.readdirSync(config.src).filter( function(fname) {
    return (fname.indexOf("messages_") === 0);
});


console.log('Message files found:');
console.log(files);


new Promise.all( files.map(function(fname) {
    // get the language in the file name
    var lang = fname.replace('messages_', '').split('.').shift();

    var f = path.join(config.src, fname);
    var reader = readline.createInterface({
        input: fs.createReadStream(f)
    })

    // read the file and convert to an object
    return new Promise(function(resolve, reject) {
        var obj = {};

        // read lines of the text file
        reader.on('line', function(line) {
            line = line.trim();
            if (line.length === 0 || line[0] === '#') {
                return;
            }
            // check the separation
            var index = line.indexOf('=');
            var key = line.substring(0, index).trim();
            var msg = line.slice(index + 1).trim();
            if (key) {
                obj[key] = msg;
            }
        });

        // end of file
        reader.on('close', function() {
            resolve(obj);
        });
    })
    // receive the object with the keys
    // write the file
    .then(function(obj) {
        return new Promise(function(resolve, reject) {
            var fdest = path.join(config.dest, 'messages_' + lang + '.json');
            var txt = JSON.stringify(obj, null, 4);
            fs.writeFile(fdest, txt, function(err) {
                if (err) {
                    console.log(err);
                    return reject(err);
                }

                console.log('* Generated ' + fdest);
                resolve();
            });
        });
    })
}))
.then(function() {
    process.exit(0);
})
.catch(function(err) {
    console.log(err);
    process.exit(-1);
});


// // default messages
// var msgs;

// (new Promise(function(resolve, reject) {
//     // read the English file (default) with the keys
//     PP.read( path.join(config.src, 'messages_en.properties'), function(err, res) {
//         if (err) {
//             reject(err);
//         }
//         else {
//             msgs = res;
//             resolve(res);
//         }
//     });
// }))
// .then(function() {

// })
// .then(function() {

//     // Add all promises in a queue of promises
//     return Promise.all(files.map(function(fname) {

//         // get the language
//         var lang = fname.replace('messages_', '').split('.').shift();

//         // english language will be used as keys
//         if (lang === 'en') {
//             return;
//         }

//         // create a promise for every file to be converted
//         return new Promise(function(resolve, reject) {

//             // read the messages<lang>.properties file
//             PP.read( path.join(config.src, fname), function(err, res) {
//                 if (err) {
//                     reject(err);
//                 }
//                 else {
//                     var obj = {};
//                     // generate new configuration file based on the english language
//                     for (var key in res) {
//                         var val = res[key];
//                         var newkey = msgs[key];
//                         if (newkey) {
//                             obj[newkey] = val;
//                         }
//                     }

//                     // write the file with new keys
//                     var fdest = path.join(config.dest, 'messages_' + lang + '.json');
//                     fs.writeFile(fdest, JSON.stringify(obj, null, 4), function(err) {
//                         if (err) {
//                             reject(err);
//                         }
//                         else {
//                             console.log('* Generated ' + fdest);
//                             resolve();
//                         }
//                     });
//                 }
//             })
//         });

//     }));

// })
// .then(function() {
//     process.exit(0);
// })
// .catch(function(err) {
//     console.log(err);
//     process.exit(-1);
// });

