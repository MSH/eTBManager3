
import React from 'react';
import CRUD from '../../commons/crud';
import CrudView from './crud-view';

// import SubstanceEdt from './substance-edt';

const crud = new CRUD('substance');

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

// definition of the form fields to edit substances
const editorDef = {
	layout: [
		{
			property: 'shortName',
			required: true,
			type: 'string',
			max: 20,
			label: __('form.shortName'),
			size: { sm: 3 }
		},
		{
			property: 'name',
			required: true,
			type: 'string',
			max: 200,
			label: __('form.name'),
			size: { sm: 6 }
		}
	],
	title: doc => doc && doc.id ? __('admin.substances.edt') : __('admin.substances.new')
};

/**
 * The page controller of the public module
 */
export class Substances extends React.Component {

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		tableDef.title = data.title;

		return (
			<CrudView tableDef={tableDef} crud={crud}
				editorDef={editorDef}
				search perm={data.perm} />
			);
	}
}

Substances.propTypes = {
	route: React.PropTypes.object
};
