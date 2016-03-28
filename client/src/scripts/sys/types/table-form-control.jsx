
import React from 'react';
import Form from '../../forms/form';
import { TableForm } from '../../components/index';

/**
 * Used in the Form library. Provide input data of string and number types
 */
class TableFormControl extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.state = { rowsQuantity: (props.iniRowsQtt ? props.iniRowsQtt : 1), docs: [] };
		this.addRow = this.addRow.bind(this);
		this.remRow = this.remRow.bind(this);
	}

	componentWillMount() {

	}

	/**
	 * Called when user changes the value in the control
	 * @return {[type]} [description]
	 */
	onChange() {
		const sc = this.props.schema;
		const value = this.state.docs;

		this.props.onChange({ schema: sc, value: value });
	}

	/**
	 * Set the component focus
	 * @return {[type]} [description]
	 */
	focus() {

	}


	addRow() {
		var quantity = this.state.rowsQuantity + 1;
		this.setState({ rowsQuantity: quantity });
	}

	remRow() {
		if (this.state.rowsQuantity > 1) {
			var quantity = this.state.rowsQuantity - 1;
			this.setState({ rowsQuantity: quantity });
		}
	}

	validate() {
		return this.refs.tableform.validate();
	}

	render() {
		const sc = this.props.schema;

		if (sc.readOnly) {
			return <div>{'TODOMS: usar reactTable'}</div>;
		}

		if (!sc.fschema) {
			return null;
		}

		// rend the selection box
		return (
			<TableForm ctitles={this.props.ctitles}
				fschema={sc.fschema}
				rowsQuantity={this.state.rowsQuantity}
				addRow={this.addRow}
				remRow={this.remRow}
				docs={this.state.docs}
				ref="tableform"
				nodetype={'fluid'}
				onChange={this.onChange} />
		);
	}

}

TableFormControl.propTypes = {
	value: React.PropTypes.any,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	resources: React.PropTypes.any,
	ctitles: React.PropTypes.array,
	iniRowsQtt: React.PropTypes.number
};

TableFormControl.options = {
	supportedTypes: 'tableform'
};

export default Form.typeWrapper(TableFormControl);
