'use strict';

var crud = require('../common/crud'),
	assert = require('assert'),
//	shortid = require('shortid'),
	u = require('../common/uniquename'),
	cslist = require('./country-structure-test').model,
	Promise = require('bluebird');

var crudAdminUnit = crud('adminunit');


/**
 * Create a memory model
 */
function createModel() {
	var tree = [];
	// create admin units level 1
	for (var i = 1; i <= 3; i++) {
		var item = {
			data: {
				name: u('Region ' + i),
				csId: cslist[0].id
			},
			children: []
		};
		tree.push(item);

		// create admin unit level 2
		for (var k = 1; k <= 12; k++) {
			var item2 = {
				data: {
					name: u('City ' + i + '.' + k),
					csId: cslist[1].id
				},
				children: []
			};
			item.children.push(item2);

			// create admin unit level 3
			for (var n = 1; n <= 2; n++) {
				var item3 = {
					data: {
						name: u('Municipality ' + i + '.' + k + '.' + n),
						csId: cslist[2].id
					}
				};
				item2.children.push(item3);
			}
		}
	}

	return tree;
}



var ids = [];
var model;



/**
 * TEST STARTING POINT
 */
describe('admin-unit', function() {


	this.timeout(50000);


	/**
	 * Create the administrative units
	 */
	it('# create', function() {
		model = exports.model = createModel();

		// create a list of promises to create aus from a list
 		var createPromises = function(lst, parent) {
 			// create promises of each item in teh list
 			var proms = lst.map(function(item) {
 				if (parent) {
 					item.data.parentId = parent.id;
 				}

 				// create a new administrative unit
 				return crudAdminUnit.create(item.data)
 					.then(function(res) {
 						// save the ID
 						ids.push(res);
 						if (item.children) {
	 						return createPromises(item.children, item.data);
 						}
 					});
 			});
 			return Promise.all(proms);
 		};

 		// this promise will conclude when all aus are created
 		return createPromises(exports.model)
 			// load all units created
 			.then(function() {
 				var req = {
 					ids: ids,
 					profile: 'detailed'
 				};
 				return crudAdminUnit.findMany(req);
 			})
 			// check if codes are ok
 			.then(function(res) {
 				// set the codes of the model
				for (var i = 0; i < res.list.length; i++) {
					var obj = res.list[i];
					var au = findById(obj.id);
					assert(au);
					if (au) {
						au.data.code = obj.code;
					}
				}
				// check if codes were created acordingly
				assertCodes(exports.model);
 			});
	});


	/**
	 * Find an administrative unit by its ID
	 */
	it('# findOne', function() {
		var au = model[0].data;
		var parent;
		return crudAdminUnit.findOne(au.id)
			.then(function(res) {
				assert(res.id);
				assert(res.name);
				assert(res.csId);
				assert(res.csName);
				assert.equal(res.id, au.id);
				assert.equal(res.name, au.name);
				assert.equal(res.csId, au.csId);
				assert.equal(res.csName, cslist[0].name);

				// prepare next query
				parent = au;
				au = model[0].children[0].data;
				return crudAdminUnit.findOne(au.id);
			})
			.then(function(res) {
				assert(res.id);
				assert(res.name);
				assert(res.csId);
				assert(res.csName);
				assert(res.parentId);
				assert(res.parentName);

				assert.equal(res.id, au.id);
				assert.equal(res.name, au.name);
				assert.equal(res.csId, au.csId);
				assert.equal(res.parentName, parent.name);
				assert.equal(res.parentId, parent.id);
				assert.equal(res.csName, cslist[1].name);
			});
	});


	/**
	 * Update the name of the administrative unit
	 */
	it('# update', function() {
		// admin unit of 2nd level
		var suffix = ' v2';
		var au = model[0].children[0].data;

		var data = {
			name: au.name + suffix,
			csId: au.csId,
			parentId: au.parentId
		};

		return crudAdminUnit.update(au.id, data)
			.then(function() {
				return crudAdminUnit.findOne(au.id);
			})
			.then(function(res) {
				assert.equal(res.id, au.id);
				assert.equal(res.name, data.name);
				assert.equal(res.csId, au.csId);
				assert.equal(res.parentId, au.parentId);
			});
	});


	/**
	 * Move the admin unit to another parent
	 */
	it('# move to another branch', function() {
		var au = model[0].children[0].data;
		var newparent = model[1].data;

		var data = {
			name: au.name,
			csId: au.csId,
			parentId: newparent.id
		};

		// move to a new parent
		return crudAdminUnit.update(au.id, data)
			.then(function() {
				// load the new data
				return crudAdminUnit.findOne(au.id);
			})
			// check the parent
			.then(function(res) {
				assert.equal(res.parentId, newparent.id);
				assert.equal(res.parentName, newparent.name);

				return crudAdminUnit.findMany({ids: ids, profile: 'detailed'});
			})
			.then(function(res) {
				// check if codes are correctly adjusted
				var tree = mountTree(res.list);
				assertCodes(tree);

				// count children of new parent
				return crudAdminUnit.findMany({parentId: newparent.id});
			})
			.then(function(res) {
				assert(res.count, 13);
				assert.equal(res.list.length, res.count);

				// return to previous branch
				data.parentId = model[0].data.id;
				return crudAdminUnit.update(au.id, data);
			})
			.then(function() {
				// return all items again
				return crudAdminUnit.findMany({ids: ids, profile: 'detailed'});
			})
			.then(function(res) {
				// check if codes are correctly adjusted
				var tree = mountTree(res.list);
				assertCodes(tree);

				// count children of new parent
				return crudAdminUnit.findMany({parentId: newparent.id});
			});
	});



	it('# query roots', function() {
		return crudAdminUnit.findMany({rootUnits: true, profile: 'detailed' })
			.then(function(res) {
				assert(res.count >= 3);
				assert(res.list);
				assert.equal(res.count, res.list.length);

				res.list.forEach( function(item) {
					assert(!item.parentId);
					assert(!item.parentName);
					assert.equal(item.code.length, 3);
				});
			});
	});

// // 	it('# query child', function() {
// // 		// query REGION-2
// // // UPDATE NEEDED
// // 		var pid = items[0].data.id;
// // 		return crudAdminUnit.findMany({parentId: pid,  })
// // 			.then(function(res) {
// // 				assert(res.list);
// // 				assert(res.count);
// // 				assert.equal(res.count, 2);

// // 				var pcode = items[0].data.code;
// // 				res.list.forEach( function(item) {
// // 					assert(item.parentId);
// // 					assert(item.parentName);
// // 					assert.equal(item.parentId, pid);
// // 				});
// // 			});
// // 	});


	it('# query key', function() {
		var key = 'City ';
		return crudAdminUnit.findMany({key: key, parentId: model[0].data.id })
			.then(function(res) {
				assert(res.list);
				assert(res.count);
				assert.equal(res.count, 12);
				assert.equal(res.count, res.list.length);

				res.list.forEach(function(item) {
					assert.equal(item.name.slice(0, key.length), key);
				});
			});
	});



	it('# query all children', function() {
		// query all children of ROOT-2
		var pid = model[1].data.id;

		return crudAdminUnit.findMany({parentId: pid, includeChildren: true, profile: 'detailed'})
			.then(function(res) {
				// including 12 cities and 2 municipalities for each city found
				assert.equal(res.count, 12 * 3);
			});
	});


	/**
	 * Test page supporting, i.e, load just the limit per page
	 */
	it('# paging', function() {
		var data = {
			page: 0,
			rpp: 5,
			parentId: model[0].data.id
		};

		return crudAdminUnit.findMany(data)
		.then(function(res) {
			assert.equal(res.count, 12);
			assert.equal(res.list.length, 5);

			data.page = 1;
			return crudAdminUnit.findMany(data);
		})
		.then(function(res) {
			assert.equal(res.count, 12);
			assert.equal(res.list.length, 5);

			// query the last page
			data.page = 2;
			return crudAdminUnit.findMany(data);
		})
		.then(function(res) {
			assert.equal(res.count, 12);
			assert.equal(res.list.length, 2);

		});
	});

	/**
	 * Execute tests to check if profile data are received accordingly
	 */
	it('# profiles', function() {
		var qry = {
			profile: 'item',
			page: 0,
			rpp: 5
		};

		return crudAdminUnit.findMany(qry)
		.then(function(res) {
			assert.equal(res.list.length, 5);
			res.list.forEach(function(item) {
				assert.equal(Object.keys(item).length, 2);
				assert(item.id);
				assert(item.name);
			});

			qry.profile = 'default';
			return crudAdminUnit.findMany(qry);
		})
		.then(function(res) {
			assert.equal(res.list.length, 5);
			res.list.forEach(function(item) {
				assert.equal(Object.keys(item).length, 6);
				assert(item.id);
				assert(item.name);
				assert('parentId' in item);
				assert('parentName' in item);
				assert(item.csId);
				assert(item.csName);
			});

			// test detailed profile
			qry.profile = 'detailed';
			qry.rpp = 2;
			return crudAdminUnit.findMany(qry);
		})
		.then(function(res) {
			assert.equal(res.list.length, 2);
			res.list.forEach(function(item) {
				assert.equal(Object.keys(item).length, 9);
				assert(item.id);
				assert(item.name);
				assert('parentId' in item);
				assert('parentName' in item);
				assert(item.csId);
				assert(item.csName);
				assert('unitsCount' in item);
				assert(item.code);
				assert('customId' in item);
			});
		});
	});
});



/**
 * Search an item by its ID
 * @param  {[type]} lst [description]
 * @param  {[type]} id  [description]
 * @return {[type]}     [description]
 */
function findById(id, lst) {
	if (!lst) {
		lst = exports.model;
	}

	for (var i = 0; i < lst.length; i++) {
		var item = lst[i];
		if (item.data.id === id) {
			return item;
		}

		if (item.children) {
			var res = findById(id, item.children);
			if (res) {
				return res;
			}
		}
	}
}

/**
 * Check if the codes in the model are ok
 * @param  {[type]} lst     [description]
 * @param  {[type]} parcode [description]
 * @return {[type]}         [description]
 */
function assertCodes(lst, parcode) {
	if (!parcode) {
		parcode = '';
	}

	for (var i = 0; i < lst.length; i++) {
		var item = lst[i];

		assert.equal(item.data.code.length, parcode.length + 3);
		assert.equal(item.data.code.slice(0, parcode.length), parcode);
		if (item.children) {
			assertCodes(item.children, item.data.code);
		}
	}
}

function mountTree(lst) {
	var tree = [];
	for (var i = 0; i < lst.length; i++) {
		var it = lst[i];
		if (!it.parentId) {
			var res = createLeaf(it, lst);
			tree.push(res);
		}
	}
	return tree;
}

function createLeaf(it, lst) {
	var res = { data: it};
	var children = [];
	for (var i = 0; i < lst.length; i++) {
		var node = lst[i];
		if (node.parentId === it.id) {
			children.push(createLeaf(node, lst));
		}
	}
	if (children.length > 0) {
		res.children = children;
	}
	return res;
}
