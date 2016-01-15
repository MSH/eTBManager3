
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
			// check if property was defined
			if (!el.property) {
				throw new Error('Property not defined in schema');
			}

			// check if type was defined
			if (!el.type) {
				throw new Error('Type not defined. Property ' + el.property);
			}
		}

		const th = Types.list[el.type];

		if (__DEV__) {
			// check if type was not found
			if (!th) {
				return <div>{'Type not found: ' + el.type}</div>;
			}
		}

		const Comp = th.formComponent(el);

		if (Comp === null) {
			if (__DEV__) {
				// return nice message to the developer
				return (
					<div>
						<label className="text-danger">{'Undefined component'}</label>
						<div className="text-muted">{'type = ' + el.type}</div>
					</div>
					);
			}

			return null;
		}

		// simplify error handling, sending just a string if there is
		// just one single error for the property
		let errors = this.props.errors;
		if (errors && Object.keys(errors).length === 1 && el && errors[el.property]) {
			errors = errors[el.property];
		}

		return (
			<Comp schema={el}
				onChange={this.props.onChange}
				value={this.props.value}
				errors={errors} />
			);
	}
}

FieldElement.propTypes = {
	schema: React.PropTypes.object,
	value: React.PropTypes.any,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.object
};
