/**
 * Generate and maintain a form based on a given layout (in object structure) and a data model
 */
import React from 'react';
import { Alert } from 'react-bootstrap';
import FieldElement from './impl/field-element';
import { validateForm } from './impl/validator';
import { getValue, setValue, objEqual } from '../commons/utils';
import { arrangeGrid } from '../commons/grid-utils';
import { app } from '../core/app';


/**
 * Create a form based on a given json layout and a data model
 */
export default class Form extends React.Component {

	constructor(props) {
		super(props);
		this._onChange = this._onChange.bind(this);
	}

	componentWillMount() {
		const elems = this.updateElementStates();

		// check if there is any element to set focus on
		if (!elems.find(item => item.autoFocus)) {
			// find first field element to set autofocus
			const el = elems.find(item => item.el === 'field');
			if (el) {
				el.autoFocus = true;
			}
		}

		this.setState({ elements: elems });
	}

	/**
	 * Create a new instance of the document to be edited in the form based on default values
	 * of the form layout
	 * @param  {[type]} layout [description]
	 * @return {[type]}        [description]
	 */
	static newInstance(layout) {
		const doc = {};
		layout.forEach(elem => {
			if (elem.property) {
				setValue(doc, elem.property, elem.defaultValue);
			}
		});
		return doc;
	}

	/**
	 * Validate the form and return validation messages, if any erro is found
	 * @param  {[type]} layout    [description]
	 * @param  {[type]} datamodel [description]
	 * @return {[type]}           [description]
	 */
	validate() {
		return validateForm(this.state.elements, this.props.doc);
	}

	/**
	 * Generate a new state of the element. This state will be sent to the element component
	 * @param  {[type]} elem [description]
	 * @return {[type]}      [description]
	 */
	createElemState(elem) {
		// create a new state
		const state = Object.assign({ el: 'field' }, elem, { options: null });

		state.options = this.fieldOptions(elem);

		// replace functions by properties
		for (var key in state) {
			const val = state[key];
			if (typeof val === 'function') {
				const res = val.call(this.props.doc);
				state[key] = res;
			}
		}
		return state;
	}

	/**
	 * Update element state list. Element state is just update if it has changed
	 * @return {Array} List of element states
	 */
	updateElementStates() {
		const lst = this.props.layout.map(elem => this.createElemState(elem));

		const oldlst = this.state ? this.state.elements : null;

		if (!oldlst) {
			return lst;
		}

		if (oldlst.length !== lst.length) {
			return lst;
		}

		for (var i = 0; i < lst.length; i++) {
			const old = oldlst[i];
			const cur = lst[i];
			if (!objEqual(old, cur)) {
				oldlst[i] = cur;
			}
		}

		return oldlst;
	}

	/**
	 * Called every time a component value is changed
	 */
	_onChange(evt) {
		// update value in the data model
		const el = evt.element;
		const value = evt.value;
		setValue(this.props.doc, el.property, value);

		const lst = this.updateElementStates();
		this.setState({ elements: lst });
	}


	/**
	 * Create a list of global messages based on unhandled messages by the fields
	 * @return {[type]} [description]
	 */
	createGlobalMsgs(msg) {
		const errors = this.props.errors;

		if (!errors) {
			return null;
		}

		const keys = Object.keys(errors);
		const lst = [];

		if (msg) {
			lst.push(msg);
		}

		keys.forEach(key => {
			if (this.handledErrors.indexOf(key) < 0) {
				const err = errors[key];
				lst.push(<li key={key}>{key + ': ' + (err.msg ? err.msg : err)}</li>);
			}
		});

		return lst.length > 0 ? <ul>{lst}</ul> : null;
	}

	/**
	 * Return a list of errors of a specific field
	 * @param  {[type]} propname [description]
	 * @param  {[type]} errors   [description]
	 * @return {[type]}          [description]
	 */
	propertyErrors(propname, errors) {
		if (!errors) {
			return null;
		}

		const keys = Object.keys(errors);
		const res = {};
		keys.forEach(key => {
			if (key.startsWith(propname)) {
				const error = errors[key];
				res[key] = error.msg ? error.msg : error;
				// add error messages that are handled
				this.handledErrors.push(key);
			}
		});
		return res;
	}


	/**
	 * Return the list of options
	 * @return {[type]} [description]
	 */
	fieldOptions(elem) {
		const options = elem.options;

		if (!options) {
			return null;
		}

		const lst = typeof options === 'string' ? app.getState().app.lists[options] : options;

		if (!lst) {
			return null;
		}

		let opts = [];
		opts.push({ id: '-', name: '-' });
		if (lst.constructor !== Array && typeof lst === 'object') {
			const keys = Object.keys(lst);
			if (keys.length === 2 && lst.from && lst.to) {
				for (var i = lst.from; i <= lst.to; i++) {
					opts.push({ id: i.toString(), name: i.toString() });
				}
			}
			else {
				keys.forEach(key => opts.push({ id: key, name: lst[key] }));
			}
		}
		else {
			opts = opts.concat(lst);
		}

		this._options = opts;
		return opts;
	}


	createForm() {
		const layout = this.props.layout;
		const doc = this.props.doc;
		let errors = this.props.errors;

		if (!layout || !doc) {
			return null;
		}

		this.handledErrors = [];

		// check if there is any global error message
		let globalMsg = errors instanceof Error ? errors.message : null;

		// is not a list of error messages ?
		if (errors instanceof Error) {
			errors = null;
		}

		const items = this.state.elements.map(elem => {
			const compErrors = elem.property ? this.propertyErrors(elem.property, errors) : null;
			const value = getValue(this.props.doc, elem.property);

			const comp = this.createComponent(elem, value, compErrors);

			const size = elem.size ? elem.size : { sm: 12 };
			return { size: size, content: comp };
		});

		const lst = arrangeGrid(items);

		// called after the elements loop to search for unhandled messages
		if (!globalMsg) {
			globalMsg = this.createGlobalMsgs(globalMsg);
		}

		// is there a global message
		if (globalMsg) {
			return (
				<div>
					<Alert bsStyle="danger">{globalMsg}</Alert>
					{lst}
				</div>
				);
		}

		return lst;
	}

	createComponent(schema, value, errors) {
		// BY NOW JUST THE FIELD ELEMENT IS SUPPORTED
		return <FieldElement schema={schema} value={value} onChange={this._onChange} errors={errors} />;
	}

	/**
	 * Rend form
	 * @return {[type]} [description]
	 */
	render() {
		const form = this.createForm();
		return <div>{form}</div>;
	}
}

Form.propTypes = {
	layout: React.PropTypes.array,
	doc: React.PropTypes.object,
	errors: React.PropTypes.object
};


