import React from 'react';
import Form from '../../../../forms/form';


export default class SelectFilter extends React.Component {

	constructor(props) {
		super(props);
		this._onChange = this._onChange.bind(this);
	}

	_onChange(evt) {
		if (this.props.onChange) {
			this.props.onChange(evt.value);
		}
	}

	render() {
		const filter = this.props.filter;
		const options = filter.resources && filter.resources.options;

		const schema = { };

		const Control = filter.type === 'multi-select' ?
			Form.types.multiSelect : Form.types.select;

		return (
			<Control
				schema={schema}
				value={this.props.value}
				onChange={this._onChange}
				resources={options} />
		);
	}
}

SelectFilter.propTypes = {
	filter: React.PropTypes.object,
	value: React.PropTypes.any,
	onChange: React.PropTypes.func
};
