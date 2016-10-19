import React from 'react';
import FormUtils from '../form-utils';
import { objEqual } from '../../commons/utils';


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
                'readOnly', 'visible', 'label', 'required', 'disabled', 'value'
            ];

            // create the snapshot object as a copy of the property
            const ss = Object.assign({}, schema);

            // evaluate the properties
            evalProps.forEach(prop => FormUtils.propEval(ss, prop, doc));

            if (!('visible' in ss)) {
                ss.visible = true;
            }

            // check if there are parameters to be evaluated
            if ('params' in ss) {
                ss.params = Object.assign({}, schema.params);
                for (var k in ss.params) {
                    FormUtils.propEval(ss.params, k, doc);
                }
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


        componentDidMount() {
            if (!this.refs.input) {
                return;
            }

            // check if child control must request the server
            this._checkServerRequest(this.props);
        }

        /**
         * Check if component must update based on the new properties
         * @param  {[type]} nextProps [description]
         * @return {[type]}           [description]
         */
        shouldComponentUpdate(nextProps) {
            // component should update only if element or doc is changed
            var update = !objEqual(nextProps.schema, this.props.schema) ||
                        nextProps.resources !== this.props.resources ||
                        nextProps.value !== this.props.value ||
                        nextProps.errors !== this.props.errors;

            if (update) {
                // just check server request if component changed (protection to avoid eternal loop)
                this._checkServerRequest(nextProps);
            }

            return update;
        }

        /**
         * Check if request must be performed to the server
         * @param {object} props The next properties
         * @return {[type]} [description]
         */
        _checkServerRequest(nextProps) {
            // get reference to the child function to create a possible server request
            const comp = this.refs.input;
            if (!comp || !comp.serverRequest) {
                return;
            }

            // get request data
            const req = comp.serverRequest(nextProps.schema, nextProps.value, nextProps.resources);
            // no request? so exit
            if (!req) {
                return;
            }

            // ask the form to request the server
            this.props.onRequest(this.props.schema, req);
        }

        /**
         * Set the focus to the control, if available
         * @return {boolean} True if focus was set, or false is focus is not available for the control
         */
        focus() {
            const comp = this.refs.input;
            return comp.focus ? comp.focus() : false;
        }

        /**
         * Validate the control. In case of failure, return a string or a complex error structure
         * @return {String} If validation fails, return the message in string or object format, otherwise return null
         */
        validate(snapshot, value, doc) {
            const comp = this.refs.input;
            return comp.validate ? comp.validate(snapshot, value, doc) : null;
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
        resources: React.PropTypes.any,
        onRequest: React.PropTypes.func
    };

    return FormControl;
}
