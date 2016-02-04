
import React from 'react';
import FormUtils from '../form-utils';


export default function fieldControlWrapper(Component) {

	class FieldControl extends React.Component {

		componentWillMount() {
			const isServerInitRequired = Component.isServerInitRequired ? Component.isServerInitRequired(this.props.schema) : false;

			// resources were informed to initialize the field ?
			if (!isServerInitRequired || this.props.resources) {
				return this.setState({ init: true });
			}

			// get parameters to initialize the field
			const params = Component.getInitParams ? Component.getInitParams(this.props.schema) : null;
			// no parameter ?
			if (!params) {
				// so field is initialized
				return this.setState({ init: true });
			}

			// property to be initialized by the parent was informed ?
			if (this.props.onInit) {
				// call parent to initialize the fields
				return this.props.onInit(this.props, params);
			}

			FormUtils.initFields({ id: 'v', type: this.props.schema.type })
			.then(res => console.log('TODO', res));
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

		static isServerInitRequired(schema) {
			return Component.isServerInitRequired ? Component.isServerInitRequired(schema) : false;
		}

		static getInitParams(schema) {
			return Component.getInitParams ? Component.getInitParams(schema) : null;
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
			if (!this.state || !this.state.init) {
				return null;
			}

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
		resources: React.PropTypes.any,
		onInit: React.PropTypes.func
	};

	return FieldControl;
}

