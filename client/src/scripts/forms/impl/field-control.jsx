
import React from 'react';
import FormUtils from '../form-utils';


export default function fieldControlWrapper(Component) {

	class FieldControl extends React.Component {

		componentWillMount() {
			// const request = Component.getServerRequest ? Component.getServerRequest(this.props.schema) : null;

			// // resources were informed to initialize the field ?
			// if (!request || this.props.resources) {
			// 	return this.setState({ init: true });
			// }

			// const self = this;

			// // request the server
			// FormUtils.initFields(request)
			// .then(res => self.setState({ resources: res }));
		}


		componentDidMount() {
			if (!this.refs.input) {
				return;
			}

			const sc = this.props.schema;
			if (sc && sc.autoFocus) {
				// set the focus
				// it seems that, because form is displayed through an animation, the focus must
				// be set after a while
				setTimeout(() => {
					const focusFunc = this.refs.input.focus;
					if (__DEV__) {
						if (!focusFunc) {
							console.warn('No focus function implemented for component ' + this.refs.input);
						}
					}
					focusFunc();
				}, 50);
			}
		}

		shouldComponentUpdate(nextProps) {
			// component should update only if element or doc is changed
			var update = nextProps.schema !== this.props.schema ||
						nextProps.value !== this.props.value ||
						nextProps.errors !== this.props.errors;

			return update;
		}

		static displayText(value) {
			return Component.displayText ? Component.displayText(value) : value;
		}

		static getServerRequest(schema, val, doc) {
			return Component.getServerRequest ? Component.getServerRequest(schema, val, doc) : false;
		}

		/**
		 * Return the type name (or list of type names) supported by the form. It is the
		 * name in the schema type
		 * @return {String} The type name (or an array indicating the list of names) supported by the form schema
		 */
		static supportedTypes() {
			if (__DEV__) {
				if (!(Component.options && Component.options.supportedTypes)) {
					console.warn('options.supportedTypes not implemented in component ' + Component.name);
				}
			}
			return Component.options.supportedTypes;
		}

		static defaultValue() {
			return Component.options.defaultValue;
		}


		validate() {
			const comp = this.refs.input;
			return comp.validate ? comp.validate() : null;
		}


		render() {
			// if (!this.state || !this.state.init) {
			// 	return null;
			// }

			const props = Object.assign({}, this.props);
			delete props.onInit;

			const sc = this.props.schema;

			if (sc && 'visible' in sc && !sc.visible) {
				return null;
			}

			return <Component ref="input" {...props} />;
		}

	}

	FieldControl.propTypes = {
		value: React.PropTypes.any,
		schema: React.PropTypes.object,
		onChange: React.PropTypes.func,
		errors: React.PropTypes.any,
		resources: React.PropTypes.any
	};

	return FieldControl;
}

