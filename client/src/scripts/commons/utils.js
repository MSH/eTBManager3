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


/**
 * Set a value of an object by giving its property. The property argument may reference a
 * nested property, separated by dots or brackets. Ex.: val[1].prop
 * @param {[type]} obj        The object to set the value in
 * @param {[type]} prop       The name of the property
 * @param {[type]} val        The value to set in the given property
 * @param {[type]} autoCreate If true and one of the nested properties doesn't exist, an empty object will be set.
 *                            If false and one of the nested properties doesn't exist an exception will be thrown
 */
export function setValue(obj, prop, val, autoCreate) {
	let value = obj;
    let s = prop.replace(/\[(\w+)\]/g, '.$1'); // convert indexes to properties
    s = s.replace(/^\./, '');           // strip a leading dot
    var props = s.split('.');

    for (var i = 0; i < props.length - 1; i++) {
        const k = props[i];
        let newval = value[k];

        // new value was found ?
        if (!newval) {
            // object is to be created automatically ?
            if (autoCreate) {
                newval = {};
                value[k] = newval;
            }
            else {
                // if value is not found, raises an exception
                throw new Error('Cannot set property ' + prop);
            }
        }
        value = newval;
    }

    // set the value
    const p = props[props.length - 1];
    value[p] = val;
}

/**
 * Compare if two objects have the same properties and values. property values are compared
 * using === operator, i.e, a shallow comparation is done
 * @param  {object} obj1 The first object
 * @param  {bbject} obj2 The second object
 * @return {boolean}     True if both object are the same
 */
export function objEqual(obj1, obj2) {
    if (Object.keys(obj1).length !== Object.keys(obj2).length) {
        return false;
    }

    for (var k in obj1) {
        if (obj1[k] !== obj2[k]) {
            return false;
        }
    }
    return true;
}

/**
 * Check if value is null or undefined
 * @param  {[type]}  val [description]
 * @return {Boolean}     [description]
 */
export function isEmpty(val) {
    return val === undefined || val === null;
}

/**
 * Return true if object is a promise
 * @param  {object}  obj The object to be tested
 * @return {Boolean}     [description]
 */
export function isPromise(obj) {
    return !!obj && (typeof obj === 'object' || typeof obj === 'function') && typeof obj.then === 'function';
}

/**
 * Test if object is a function
 * @param  {[type]}  obj [description]
 * @return {Boolean}     [description]
 */
export function isFunction(obj) {
    return !!obj && (typeof obj === 'function');
}

/**
 * Check if an object is a string
 * @param  {[type]}  obj [description]
 * @return {Boolean}     true if value is a string, otherwise return false
 */
export function isString(obj) {
    return typeof obj === 'string';
}
