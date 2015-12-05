
import React from 'react';
import TableView from './tableview';
import CRUD from '../../commons/crud';

const crud = new CRUD('unit');

// definition of the table that will display the list of sources
const tableDef = {
	columns: [
		{
			title: __('form.name'),
			property: 'name'
		},
		{
			title: __('admin.adminunits'),
			property: 'adminUnitName'
		}
	]
};

/**
 * The page controller of the public module
 */
export class Tbunits extends React.Component {

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		return (
			<TableView title={data.title} crud={crud}
				search perm={data.perm} tableDef={tableDef} />
			);
	}
}

Tbunits.propTypes = {
	route: React.PropTypes.object
};
