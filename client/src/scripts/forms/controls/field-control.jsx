
import React from 'react';
import FormUtils from '../form-utils';


export default function fieldControlWrapper(Component) {

	class FieldControl extends React.Component {

		static displayText(value) {
			return Component.displayText ? Component.displayText(value) : value;
		}

		/**
		 * Create the request that will be sent to the server. This function is called when the
		 * form is initialized, or when the field must be updated by a change in the form.
		 * @param  {Object} snapshot     The current control schema snapshot
		 * @param  {[type]} val          The field value
		 * @param  {[type]} prevSnapshot The previous snapshot, if available
		 * @return {[type]}              The server request data, or null if no request is necessary
		 */
		static serverRequest(snapshot, val, prevSnapshot) {
			return Component.serverRequest ? Component.serverRequest(snapshot, val, prevSnapshot) : false;
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

		/**
		 * Return the default value of the field being edited. This default value is defined by
		 * the control. This value will be used only if no default value is defined in the document
		 * schema.
		 * @return {any} The default value of the field to be edited
		 */
		static defaultValue() {
			return Component.options.defaultValue;
		}

		/**
		 * Create an object representing a snapshot of the current schema.
		 * A snapshot is the state of the schema based on the values of the document
		 * @param  {[type]} schema [description]
		 * @param  {[type]} doc    [description]
		 * @return {[type]}        [description]
		 */
		static snapshot(schema, doc) {
			// properties to be evaluated
			const evalProps = [
				'readOnly', 'visible', 'label', 'required', 'disabled'
			];

			// evaluate the properties
			evalProps.forEach(prop => FormUtils.propEval(schema, prop, doc));

			// check if component defined a snapshot function
			if (Component.snapshot) {
				Component.snapshot(schema, doc);
			}
		}

		constructor(props) {
			super('FieldControl', props);
		}

		componentDidMount() {
			if (!this.refs.input) {
				return;
			}

			const sc = this.props.schema;
			if (sc && sc.autoFocus && !sc.readOnly) {
				// set the focus
				// it seems that, because form is displayed through an animation, the focus must
				// be set after a while
				setTimeout(() => {
					const focusFunc = this.refs.input.focus;
					if (__DEV__) {
						if (!focusFunc) {
							/* eslint no-console: 0 */
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
						nextProps.resources !== this.props.resources ||
						nextProps.value !== this.props.value ||
						nextProps.errors !== this.props.errors;

			return update;
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

