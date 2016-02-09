
import React from 'react';
import CrudView from '../crud-view';
import CRUD from '../../../commons/crud';

const crud = new CRUD('agerange');

// definition of the form fields to edit substances
const editorDef = {
	layout: [
		{
			property: 'iniAge',
			required: true,
			type: 'int',
			label: __('AgeRange.iniAge'),
			size: { sm: 3 }
		},
		{
			property: 'endAge',
			required: true,
			type: 'int',
			label: __('AgeRange.endAge'),
			size: { sm: 6 }
		}
	],
	title: doc => doc && doc.id ? __('admin.ageranges.edt') : __('admin.ageranges.new')
};


/**
 * The page controller of the public module
 */
export default class AgeRanges extends React.Component {

	cellRender(item) {
		return item.name;
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		return (
			<CrudView crud={crud}
				title={data.title}
				editorDef={editorDef}
				onCellRender={this.cellRender}
				cellSize={{ md: 12 }}
				perm={data.perm} />
			);
	}
}

AgeRanges.propTypes = {
	route: React.PropTypes.object
};
