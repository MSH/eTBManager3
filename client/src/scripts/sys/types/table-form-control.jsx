
import React from 'react';
import Form from '../../forms/form';
import { TableForm } from '../../components/index';

/**
 * Used in the Form library. Provide input data of string and number types
 */
class TableFormControl extends React.Component {

	static typeName() {
		return 'tableForm';
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.state = { rowsQuantity: 1 };
		this.addRow = this.addRow.bind(this);
		this.remRow = this.remRow.bind(this);
	}

	componentWillMount() {
		this.setState({ rowsQuantity: (this.props.schema.iniRowsQtt ? this.props.schema.iniRowsQtt : 1) });
	}

	/**
	 * Called when user changes the value in the control
	 * @return {[type]} [description]
	 */
	onChange() {
		const sc = this.props.schema;
		const value = this.props.value;

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
		return ( // TODOMSR fundir o table form control e o table form
			<TableForm ctitles={this.props.ctitles}
				fschema={sc.fschema}
				rowsQuantity={this.state.rowsQuantity} //todomsr o array de docs ja sabe quantas linhas essa info é redundante
				addRow={this.addRow}
				remRow={this.remRow}
				docs={this.props.value}
				ref="tableform"
				nodetype={'div'}
				onChange={this.onChange} />
		);
	}

}

TableFormControl.propTypes = {
	value: React.PropTypes.array,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	resources: React.PropTypes.any,
	ctitles: React.PropTypes.array
};

export default Form.control(TableFormControl);
