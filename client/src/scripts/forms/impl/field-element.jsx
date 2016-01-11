
import React from 'react';
import Types from '../types';


export default class FieldElement extends React.Component {

	shouldComponentUpdate(nextProps) {
		// component should update only if element or doc is changed
		var update = nextProps.schema !== this.props.schema ||
					nextProps.value !== this.props.value ||
					nextProps.errors !== this.props.errors;

		return update;
	}


	render() {
		const el = this.props.schema;

		// checks done in development time
		if (__DEV__) {
			if (!el.property) {
				throw new Error('Property not defined in schema');
			}

			if (!el.type) {
				throw new Error('Type not defined. Property ' + el.property);
			}
		}

		const th = Types.list[el.type];

		const Comp = th.formComponent(el);

		if (Comp === null) {
			if (__DEV__) {
				return (
					<div>
						<label>{'Undefined component'}</label>
						<div className="text-muted">{'type = ' + el.type}</div>
					</div>
					);
			}

			return null;
		}

		return (
			<Comp schema={el}
				onChange={this.props.onChange}
				value={this.props.value}
				errors={this.props.errors} />
			);
	}
}

FieldElement.propTypes = {
	schema: React.PropTypes.object,
	value: React.PropTypes.any,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.object
};
