'use strict';


var remove = function(param) {
    for(var i=0; i<this.routes.length; i++) {
        var r = this.routes[i];
        if(r.handler === param || r.path === param) {
            this.routes.splice(i, 1);
            return this;
        }
    }
    return this;
};


var eventCallback = function() {
    var route = Router.current;
    if (!route.routes) {
        return;
    }
    route.check(Router.currpath);
};


var check = function(path) {
    for (var i=0; i<this.routes.length; i++) {
        var route = this.routes[i],
            match,
            currpath,
            data = {path: path};

        if (route.exp) {
            match = route.exp.exec(path);
            if (match) {
                var params = {};
                for (var k = 0; k < route.params.length; k++) {
                    params[route.params[k]] = match[k + 1];
                }
                data.params = params;
            }
        }
        else {
            match = path.indexOf(route.path) === 0;
            if (match) {
                currpath = path.substring(route.path.length);
            }
        }
        // there is a match ?
        if(match) {
            data.route = route;
            Router.current = route;
            Router.currpath = currpath;
            route.handler.apply({}, [data, eventCallback]);
            return this;
        }
    }
    return this;
};


var get = function(path) {
    for (var i = 0; i < this.routes.length; i++) {
        var r = this.routes[i];
        if (r.path === path) {
            return r;
        }
    }
};

var add = function (path, handler) {
    var r = prepare(path);
    r.handler = handler;
    r.add = add;
    r.remove = remove;
    r.check = check;
    if (!this.routes) {
        this.routes = [];
    }
    this.routes.push(r);
    return this;
};


/**
 * Prepare a route path, checking if there are parameters declared
 * @param  {String} path The router
 */
function prepare(path) {
    var route = { path: path };
    // has parameters ?
    if (path.indexOf('/:') === -1) {
        return route;
    }

    // get the params
    var s = path.split('/');
    route.params = [];
    for (var i = 1; i < s.length; i++) {
        var p = s[i];
        if (p[0] === ':') {
            route.params.push(p.substring(1));
        }
    }

    // create the expression
    var exp = path.replace(/\//g, '\\/');
    route.params.forEach(function(item) {
        exp = exp.replace(':' + item, '(.*)');
    });
    route.exp = new RegExp(exp);
    return route;
}


var Router = {
    routes: [],
    mode: null,
    root: '/',
    config: function(options) {
        this.mode = options && options.mode && options.mode === 'history' &&
                    !!(history.pushState) ? 'history' : 'hash';
        this.root = options && options.root ? '/' + this.clearSlashes(options.root) + '/' : '/';
        return this;
    },
    getHash: function() {
        var fragment = '';
        if(this.mode === 'history') {
            fragment = this.clearSlashes(decodeURI(location.pathname + location.search));
            fragment = fragment.replace(/\?(.*)$/, '');
            fragment = this.root !== '/' ? fragment.replace(this.root, '') : fragment;
        } else {
            var match = window.location.href.match(/#(.*)$/);
            fragment = match ? match[0].substring(1) : '';
        }
        return fragment;
//        return this.clearSlashes(fragment);
    },
/*

    clearSlashes: function(path) {
        return path.toString().replace(/\/$/, '').replace(/^\//, '');
    },
*/
    add: add,
    remove: remove,
    get: get,
    check: check,
    flush: function() {
        this.routes = [];
        this.mode = null;
        this.root = '/';
        return this;
    },
    listen: function() {
        var self = this;
        var current = self.getHash();
        var fn = function() {
            var hash = self.getHash();
            if(current !== hash) {
                current = hash;
                self.check(current);
            }
        };
        clearInterval(this.interval);
        this.interval = setInterval(fn, 50);
        return this;
    },
    navigate: function(path) {
        path = path ? path : '';
        if(this.mode === 'history') {
            history.pushState(null, null, this.root + this.clearSlashes(path));
        } else {
            window.location.href.match(/#(.*)$/);
            window.location.href = window.location.href.replace(/#(.*)$/, '') + '#' + path;
        }
        return this;
    }
};

module.exports = Router;