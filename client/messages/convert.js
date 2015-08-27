/**
 * Convert the messages used in Java server side (iso 8859-1 format) to json format (utf-8)
 *
 * Created by rmemoria on 24/8/15.
 */


var fs = require('fs'),
    path = require('path'),
    PP = require('node-properties-parser');

var config = {
    src: '../../src/main/resources',
    dest: './'
}


var fname = path.join( config.src, 'messages_pt_BR.properties');
var fdest = path.join( config.dest, 'messages_pt_br.json');

PP.read(fname, function(err, res) {
    if (err) {
        console.log('err = ' + err);
        return;
    }

    fs.writeFileSync(fdest, JSON.stringify(res, null, 4));
    console.log('Generated ' + fdest);
});

