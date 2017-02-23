/**
 * Generate the page to display the list of files to download
 */

var Handlebars = require('handlebars');
var fs = require('fs');
var path = require('path');
var mkdirp = require('mkdirp');

/**
 * Configuration
 */
var config = {
    downloadDir: null,
    targetDir: '../../target/site',
    urlPath: null 
}

/**
 * List of files to download
 */
var files = [];

/**
 * Interpret the configuration from the command line
 */
function readCommandLine() {
    // get arguments
    process.argv.forEach(arg => {
       const s = arg.split('=');
       if (s.length == 2) {
           switch(s[0]) {
               case 'download-dir':
                    config.downloadDir = s[1];
                    break;
                case 'target-dir':
                    config.targetDir = s[1];
                    break;
                case 'url-path':
                    config.urlPath = s[1];
                    break;
           }
       }
    });

    if (!config.downloadDir) {
        throw new Error('Download dir not specified. Inform argument download-dir=<dir with files>');
    }

    if (!path.isAbsolute(config.downloadDir)) {
        config.downloadDir = path.join(__dirname, config.downloadDir);
    }

    if (!path.isAbsolute(config.targetDir)) {
        config.targetDir = path.join(__dirname, config.targetDir);
    }

    if (!config.urlPath) {
        throw new Error('No url path specified. Inform argument url-path=<url>');
    }
}

/**
 * get list of files to download
 */
function getFilesToDownload() {
    files = fs.readdirSync(config.downloadDir);
}

/**
 * Generate the destination file
 */
function generateFile() {
    var fname = path.join(__dirname, 'template/download.html');
    var s = fs.readFileSync(fname, 'utf8');

    var template = Handlebars.compile(s);

    var out = template({ config: config, files: files });

    // force the creation of the directories, if they don't exist
    mkdirp.sync(config.targetDir);

    // destination file name
    var fdest = path.join(config.targetDir, 'download.html');

    fs.writeFileSync(fdest, out);
}



/**
 * Script execution
 */

readCommandLine();

getFilesToDownload();

generateFile();