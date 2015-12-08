/**
 * Set of utility functions used throughout the application
 */

export function format(fmtstr) {
  var args = Array.prototype.slice.call(arguments, 1);
  return fmtstr.replace(/\{(\d+)\}/g, function(match, index) {
    return args[index];
  });
}

/**
 * Return a property value from an object. The property name supports nested properties
 * (p1.p2.p3, for example) and indexed property as well (p[1] for example)
 * @param  {[type]} obj  [description]
 * @param  {[type]} prop [description]
 * @return {[type]}      [description]
 */
export function getValue(obj, prop) {
	let value = obj;
    let s = prop.replace(/\[(\w+)\]/g, '.$1'); // convert indexes to properties
    s = s.replace(/^\./, '');           // strip a leading dot
    var a = s.split('.');
    for (var i = 0, n = a.length; i < n; ++i) {
        var k = a[i];
        if (k in value) {
            value = value[k];
        }
        else {
            return null;
        }
    }
    return value;
}


export function setValue(obj, prop, val) {
	let value = obj;
    let s = prop.replace(/\[(\w+)\]/g, '.$1'); // convert indexes to properties
    s = s.replace(/^\./, '');           // strip a leading dot
    var a = s.split('.');

    for (var i = 0, n = a.length - 1; i < n; ++i) {
        var k = a[i];
        if (k in value) {
            value = value[k];
        }
        else {
            return null;
        }
    }

    value[prop] = val;
}
