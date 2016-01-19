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
import Types from './types';
import { server } from '../commons/server';
import WaitIcon from '../components/wait-icon';


/**
 * Create a form based on a given json layout and a data model
 */
export default class Form extends React.Component {

	constructor(props) {
		super(props);
		this._onChange = this._onChange.bind(this);
		this.state = {};
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

		this.initForm(elems);
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
	 * Request the server to initialize the given fields
	 * @param  {Array} req list of field objects with id, type and value
	 * @return {Promise}   Promise to be resolved when server answers back
	 */
	static initFields(req) {
		return server.post('/api/form/initfields', req);
	}

	/**
	 * Generate a new label component to be displayed as the label of an input control.
	 * Includes a red icon on the right side to indicate required fields
	 * @param  {[type]} schema [description]
	 * @return {[type]}        [description]
	 */
	static labelRender(label, required) {
		if (!label) {
			return null;
		}

		const txt = label + ':';

		return required ?
			<span>{txt}<i className="fa fa-exclamation-circle app-required"/></span> :
			txt;
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
	createElemState(elem, index) {
		// create a new state
		const state = Object.assign({ el: 'field' }, elem, { options: null });

		if (state.el === 'field') {
			state.options = this.fieldOptions(elem);
			state.id = elem.property + index;
		}

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
		const lst = this.props.layout.map((elem, index) => this.createElemState(elem, index));

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
		return Object.keys(res).length === 0 ? null : res;
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
		if (!this.state.resources) {
			return <WaitIcon type="card" />;
		}

		const doc = this.props.doc;
		let errors = this.props.errors;

		if (!this.props.layout || !doc) {
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
			const value = elem.el === 'field' ? getValue(this.props.doc, elem.property) : null;

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
		if (schema.el === 'subtitle') {
			return <div className="subtitle">{schema.label}</div>;
		}

		// get any resource that came from the object
		const res = this.state.resources[schema.id];

		// BY NOW JUST THE FIELD ELEMENT IS SUPPORTED
		return (
			<FieldElement schema={schema} value={value} resources={res}
				onChange={this._onChange} errors={errors} />
			);
	}

	/**
	 * Initialize form. Called once when form is included in the DOM to check
	 * if any field requires initialization from server side.
	 * If so, sends a request to server to get field resources
	 * @return {Promise} Null if no initialization is required, otherwise returns a promise that will be
	 * resolved when resources are returned from server
	 */
	initForm(elems) {
		const doc = this.props.doc;

		// create query to be sent to the server
		const req = elems
			.filter(elem => {
				if (elem.el !== 'field') {
					return false;
				}

				const th = Types.list[elem.type];
				return th.requireServerInit(elem);
			})
			.map(elem => ({ id: elem.id, type: elem.type, value: getValue(doc, elem.property) }));

		// is there anything to be sent ?
		if (req.length === 0) {
			return this.notifyReady({});
		}

		// request to the server initialization data of the fields
		const self = this;

//		self.notifyReady([]);
		Form.initFields(req)
			.then(res => self.notifyReady(res));
	}

	notifyReady(resources) {
		this.setState({ resources: resources });
		if (this.props.onReady) {
			this.props.onReady();
		}
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
	errors: React.PropTypes.object,
	onReady: React.PropTypes.func
};


