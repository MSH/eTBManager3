
import React from 'react';
import CrudView from '../crud-view';
import CRUD from '../../../commons/crud';

const crud = new CRUD('source');

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
		},
		{
			property: 'customId',
			type: 'string',
			max: 20,
			label: __('form.customId'),
			size: { sm: 3 }
		},
		{
			property: 'active',
			type: 'yesNo',
			label: __('EntityState.ACTIVE'),
			size: { sm: 5 },
			defaultValue: true
		}
	],
	title: doc => doc && doc.id ? __('admin.sources.edit') : __('admin.sources.new')
};

/**
 * The page controller of the public module
 */
export default class Sources extends React.Component {

	cellRender(item) {
		return (
			<div>
				<b>{item.shortName}</b>
				<div className="text-muted">{item.name}</div>
			</div>
			);
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

//		tableDef.title = data.title;

		return (
			<CrudView crud={crud}
				title={data.title}
				editorDef={editorDef}
				onCellRender={this.cellRender}
				perm={data.perm} />
			);
	}
}

Sources.propTypes = {
	route: React.PropTypes.object
};
