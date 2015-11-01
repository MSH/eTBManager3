'use strict';

var crud = require('../common/crud'),
	assert = require('assert'),
	uuid = require('node-uuid'),
	Promise = require('bluebird');


// list of country structures used in the test
var cslist = [
	{
		name: 'Region ' + uuid.v1(),
		level: 1
	},
	{
		name: 'City ' + uuid.v1(),
		level: 2
	},
	{
		name: 'Locality ' + uuid.v1(),
		level: 3
	}
];

// administrative units used in the test
var items = [
	{
		data: {
			name: 'ROOT-1'
		},
		children: [
			{
				data: { name: 'City 1'},
				children: [
					{data: {name: 'Locality 1'}},
					{data: {name: 'Locality 2'}}
				]
			},
			{
				data: { name: 'City 2' }
			}
		]
	},
	{
		data: {
			name: 'ROOT-2'
		},
		children: [
			{
				data: {name: 'City 3'}
			}
		]
	}
];


var ids = [];

// function printItems(lst) {
// 	if (!lst) {
// 		lst = items;
// 		console.log('--- PRINTING ---');
// 	}
// 	for (var i = 0; i < lst.length; i++) {
// 		var item = lst[i];
// 		console.log(item.data);

// 		if (item.children) {
// 			printItems(item.children);
// 		}
// 	}
// }
/**
 * Search an item by its ID
 * @param  {[type]} lst [description]
 * @param  {[type]} id  [description]
 * @return {[type]}     [description]
 */
function findById(id, lst) {
	if (!lst) {
		lst = items;
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

function assertCodes(lst, parcode) {
	if (!lst) {
		lst = items;
	}

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

/** Update codes in the memory model and check if they are correct */
function updateAndAssertCodes(res) {
	for (var i = 0; i < res.list.length; i++) {
		var obj = res.list[i];
		var au = findById(obj.id);
		assert(au);
		if (au) {
			au.data.code = obj.code;
		}
	}

	assertCodes();
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


/**
 * TEST STARTING POINT
 */
describe('admin-unit', function() {

	var crudCs = crud('countrystructure');
	var crudAdminUnit = crud('adminunit');

	this.timeout(5000);

	/**
	 * Create the country structures used along the test
	 */
	it('# Initialize country structure', function() {

		return crudCs.create(cslist)
		.then(function() {
			var updateCsid = function(lst, level) {
				lst.forEach(function(item) {
					// set the country structure ID of the admin unit
					item.data.csId = cslist[level].id;
					item.data.name = item.data.name + ' ' + uuid.v1();
					if (item.children) {
						updateCsid(item.children, level + 1);
					}
				});
			};
			updateCsid(items, 0);
		});
	});


	/**
	 * Create the administrative units
	 */
	it('# create', function() {
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
 						if (!item.children) {
 							return;
 						}
 						return createPromises(item.children, item.data);
 					});
 			});
 			return Promise.all(proms);
 		};

 		// this promise will conclude when all aus are created
 		return createPromises(items)
 			// load all units created
 			.then(function() {
 				var req = {
 					ids: ids,
 					profile: 'ext'
 				};
 				return crudAdminUnit.findMany(req);
 			})
 			// check if codes are ok
 			.then(function(res) {
 				updateAndAssertCodes(res);
 			});
	});

	/**
	 * Find an administrative unit by its ID
	 */
	it('# findOne', function() {
		var au = items[0].data;
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
				au = items[0].children[0].data;
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
		var au = items[0].children[0].data;

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
		var au = items[0].children[0].data;
		var newparent = items[1].data;

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

				return crudAdminUnit.findMany({ids: ids, profile: 'ext'});
			})
			.then(function(res) {
				// check if codes are correctly adjusted
				var tree = mountTree(res.list);
				assertCodes(tree);
			});
	});


	it('# query roots', function() {
		return crudAdminUnit.findMany({rootUnits: true, profile: 'ext' })
			.then(function(res) {
				assert(res.count >= 2);
				assert(res.list);
				assert.equal(res.count, res.list.length);

				res.list.forEach( function(item) {
					assert(!item.parentId);
					assert(!item.parentName);
					assert.equal(item.code.length, 3);
				});
			});
	});

// 	it('# query child', function() {
// 		// query REGION-2
// // UPDATE NEEDED
// 		var pid = items[0].data.id;
// 		return crudAdminUnit.findMany({parentId: pid,  })
// 			.then(function(res) {
// 				assert(res.list);
// 				assert(res.count);
// 				assert.equal(res.count, 2);

// 				var pcode = items[0].data.code;
// 				res.list.forEach( function(item) {
// 					assert(item.parentId);
// 					assert(item.parentName);
// 					assert.equal(item.parentId, pid);
// 				});
// 			});
// 	});


	it('# query key', function() {
		var key = 'City ';
		return crudAdminUnit.findMany({key: key })
			.then(function(res) {
				assert(res.list);
				assert(res.count);
				// at least 3 items
				assert(res.count >= 3);

				res.list.forEach(function(item) {
					assert.equal(item.name.slice(0, key.length), key);
				});
			});
	});



	it('# query all children', function() {
		// query all children of ROOT-2
		var pid = items[1].data.id;

		return crudAdminUnit.findMany({parentId: pid, includeChildren: true, profile: 'ext'})
			.then(function(res) {
				// including 2 cities and 2 localities
				assert.equal(res.count, 4);
			});
	});


	it ('# query direct children', function() {
		// Get children of City-1
		var au = items[0].children[0].data;

		return crudAdminUnit.findMany({parentId: au.id, profile: 'ext'})
			.then(function(res) {
				// return 2 localities
				assert.equal(res.count, res.list.length);
				assert.equal(res.list.length, 2);

				// get children of REGION-1
				return crudAdminUnit.findMany({parentId: items[0].data.id});
			})
			.then(function(res) {
				assert.equal(res.count, res.list.length);
				assert.equal(res.list.length, 1);

				// get the children of REGION-2
				return crudAdminUnit.findMany({parentId: items[1].data.id});
			})
			.then(function(res) {
				assert.equal(res.count, res.list.length);
				assert.equal(res.list.length, 2);
			});
	});


	it('# clean up', function() {
		return crudCs.delete(cslist);
	});
});