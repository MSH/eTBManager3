/**
 * Export the property message files to a CSV file containing the messages to be translated
 *
 * Created by rmemoria on Oct 8th, 2015.
 */


var fs = require('fs'),
    path = require('path'),
    Promise = require('bluebird'),
    readline = require('readline');

/**
 * Source and destination folder
 */
var config = {
    // folder where messages_.properties files are
    src: path.join(__dirname, '../../src/main/resources'),

    // languages to convert
    langs: ['en', 'ru']
};

// file path for the main list of messages
const fname = path.join(config.src, 'messages.properties');

// create the main list of messages
const msgs = {};
fs.readFileSync(fname, 'utf-8')
    .split('\n')
    .forEach(s => {
        const v = s.split('=');
        if (v.length > 1) {
            const id = v.shift().trim();
            const msg = v.join('=');
            msgs[id] = { default: msg };
        }
    });

// read each file with messages
config.langs.forEach(lang => {
    const fname = path.join(config.src, 'messages_' + lang + '.properties');

    fs.readFileSync(fname, 'utf-8')
        .split('\n')
        .forEach(s => {
            const v = s.split('=');
            if (v.length > 1) {
                const id = v.shift().trim();
                const msg = v.join('=');
                var item = msgs[id];
                if (item) {
                    item[lang] = msg;
                } else {
                    // message doesn't exist in the default file
                    item = {};
                    item[lang] = msg;
                    msgs[id] = item;
                    console.log('not found: ' + lang + ' -> ' + id);
                }
            }
        })
});

// generate array
const res = Object.keys(msgs)
    .map(id => {
        const item = msgs[id];
        const vals = [id, item.default];
        config.langs.forEach(lang => vals.push(item[lang]));

        return vals.map(s => s ? '"' + s.replace(/"/g, '""') + '"': '""')
            .join(',');
    });

// insert CSV header
const header = ['"id"','"Default"'];
config.langs.forEach(lang => header.push('"' + lang + '"'));

res.splice(0, 0, header.join(','));

// write file
const destFile = path.join(__dirname, 'messages.csv');
fs.writeFileSync(destFile, res.join('\n'), 'utf-8');

console.log('Generated:  ' + destFile);
