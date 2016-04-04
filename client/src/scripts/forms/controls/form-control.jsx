import React from 'react';
import FormUtils from '../form-utils';


export default function formControl(Component) {

	class FormControl extends React.Component {

		static controlClass() {
			return Component;
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
			return Component.serverRequest ? Component.serverRequest(snapshot, val, prevSnapshot) : null;
		}

		/**
		 * Return the type name (or list of type names) supported by the form. It is the
		 * name in the schema type
		 * @return {String} The type name (or an array indicating the list of names) supported by the form schema
		 */
		static typeName() {
			if (__DEV__) {
				if (!Component.typeName) {
					throw new Error('function typeName() not implemented in form control ' + Component.name);
				}
			}
			return Component.typeName();
		}

		/**
		 * Return the default value of the field being edited. This default value is defined by
		 * the control. This value will be used only if no default value is defined in the document
		 * schema.
		 * @return {any} The default value of the field to be edited
		 */
		static defaultValue() {
			return Component.defaultValue ? Component.defaultValue() : null;
		}

		/**
		 * Create an object representing a snapshot of the current schema.
		 * A snapshot is the state of the schema based on the values of the document
		 * @param  {Object} schema The schema of the control
		 * @param  {Object} doc    The document being handled by the form
		 * @return {Object}        The snapshot of the schema for the given document
		 */
		static snapshot(schema, doc) {
			// properties to be evaluated
			const evalProps = [
				'readOnly', 'visible', 'label', 'required', 'disabled'
			];

			// create the snapshot object as a copy of the property
			const ss = Object.assign({}, schema);

			// evaluate the properties
			evalProps.forEach(prop => FormUtils.propEval(ss, prop, doc));

			if (!('visible' in ss)) {
				ss.visible = true;
			}

			// check if component defined a snapshot function
			return Component.snapshot ? Component.snapshot(ss, doc) : ss;
		}

		/**
		 * Return a list of elements that will share a common feature, for example,
		 * visible or read-only controls. Initially created for the 'group' control
		 * @return {Array} An array of schemas
		 */
		static children(schema) {
			return Component.children ? Component.children(schema) : null;
		}

		// constructor(props) {
		// 	super('FieldControl', props);
		// }

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

		/**
		 * Check if component must update based on the new properties
		 * @param  {[type]} nextProps [description]
		 * @return {[type]}           [description]
		 */
		shouldComponentUpdate(nextProps) {
			// component should update only if element or doc is changed
			var update = nextProps.schema !== this.props.schema ||
						nextProps.resources !== this.props.resources ||
						nextProps.value !== this.props.value ||
						nextProps.errors !== this.props.errors;

			return update;
		}


		/**
		 * Validate the control. In case of failure, return a string or a complex error structure
		 * @return {String} If validation fails, return the message in string or object format, otherwise return null
		 */
		validate() {
			const comp = this.refs.input;
			return comp.validate ? comp.validate() : null;
		}

		/**
		 * Render the control
		 * @return {[React.Component} React component
		 */
		render() {
			const sc = this.props.schema;

			// if component is not visible, doesn't render it
			if (sc && 'visible' in sc && !sc.visible) {
				return null;
			}

			return <Component ref="input" {...this.props} />;
		}
	}

	FormControl.propTypes = {
		value: React.PropTypes.any,
		schema: React.PropTypes.object,
		onChange: React.PropTypes.func,
		errors: React.PropTypes.any,
		resources: React.PropTypes.any
	};

	return FormControl;
}
