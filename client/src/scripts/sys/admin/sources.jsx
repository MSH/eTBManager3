
import React from 'react';
import TableView from './tableview';
import CRUD from '../../commons/crud';
import SourceEdt from './source-edt';

const crud = new CRUD('source');

// definition of the table that will display the list of sources
const tableDef = {
	columns: [
		{
			title: __('form.shortName'),
			property: 'shortName'
		},
		{
			title: __('form.name'),
			property: 'name'
		}
	]
};

/**
 * The page controller of the public module
 */
export class Sources extends React.Component {

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		return (
			<TableView title={data.title} crud={crud}
				editForm={SourceEdt}
				search perm={data.perm} tableDef={tableDef} />
			);
	}
}

Sources.propTypes = {
	route: React.PropTypes.object
};
