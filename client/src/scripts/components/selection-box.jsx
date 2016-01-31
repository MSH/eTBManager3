
import React from 'react';
import { MenuItem } from 'react-bootstrap';
import Popup from './popup';

export default class SelectionBox extends React.Component {

	constructor(props) {
		super(props);
		this.controlClick = this.controlClick.bind(this);
		this.itemClick = this.itemClick.bind(this);
		this.btnCloseClick = this.btnCloseClick.bind(this);
		this.notifyChange = this.notifyChange.bind(this);

		// initialize an empty list of values
		this.state = { };
	}

	notifyChange(value, evt) {
		let val;
		if (this.props.mode === 'single') {
			val = this.props.options.indexOf(value) >= 0 ? value : null;
		}
		else {
			val = value;
		}

		this._value = val;

		this.setState({ value: val });
		if (this.props.onChange) {
			this.props.onChange(evt, val);
		}
	}

	getValue() {
		return this._value;
	}

	/**
	 * Return the rendered component for the label
	 * @return {React.Component} The label component, or null if there is no label
	 */
	labelRender() {
		const label = this.props.label;
		return label ? <div className="control-label">{label}</div> : null;
	}

	/**
	 * Return the item to be displayed
	 * @param  {Object} item The item to be displaye
	 * @return {[type]}      [description]
	 */
	getOptionDisplay(item) {
		const idisp = this.props.optionDisplay;
		if (!idisp) {
			return item;
		}

		if (typeof idisp === 'function') {
			return idisp(item);
		}

		if (typeof idisp === 'string') {
			return item[idisp];
		}

		return item;
	}

	/**
	 * Return the options to be displayed in the popup
	 * @return {[type]} [description]
	 */
	getOptions() {
		const options = this.props.options;
		if (!options) {
			return null;
		}

		const values = this.state.value;
		if (this.props.mode === 'single' || !values) {
			if (this.props.noOption) {
				const lst = options.slice(0);
				lst.unshift('-');
				return lst;
			}
			return options;
		}

		// filter the items to display just the not selected options
		return options.filter(item => values.indexOf(item) === -1);
	}

	/**
	 * Create the popup component to be displayed based on the options
	 * @return {React.Component} Popup component, or null if no option is found
	 */
	createPopup() {
		const options = this.getOptions();
		if (options === null) {
			return null;
		}

		// create the components
		const opts = options
			.map(item => {
				return (
					<MenuItem key={this.props.options.indexOf(item)}
						onSelect={this.itemClick(item)}>
						{this.getOptionDisplay(item)}
					</MenuItem>
				);
			});

		return opts.length > 0 ? <Popup ref="popup">{opts}</Popup> : null;
	}

	/**
	 * Called when user clicks on the close button of the item
	 * @param  {object} item The item to be removed
	 */
	btnCloseClick(item) {
		const self = this;
		return evt => {
			const values = self.state.value;
			const index = values.indexOf(item);
			values.splice(index, 1);
			this.notifyChange(values, evt);
			evt.stopPropagation();
		};
	}

	/**
	* Called when user clicks on the control
	**/
	controlClick() {
		if (!this.refs.popup) {
			return;
		}

		this.refs.popup.show();
	}

	/**
	 * Called when user clicks on an item in the drop down
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	itemClick(item) {
		const self = this;
		return evt => {
			if (self.props.mode === 'single') {
				return self.notifyChange(item, evt);
			}

			const values = this.state.value ? this.state.value : [];
			values.push(item);
			self.notifyChange(values.slice(0), evt);
			self.refs.popup.preventHide();
		};
	}

	/**
	 * Rendering of the control content
	 * @return {React.Component} The component to be displayed inside the control
	 */
	contentRender() {
		const value = this.state.value;
		if (!value) {
			return null;
		}

		// is a single value selection ?
		if (this.props.mode === 'single') {
			// display the single value
			return this.getOptionDisplay(value);
		}

		const lst = this.state.value;
		// create the list of selected values
		const items = lst.map(item =>
			<span key={lst.indexOf(item)} className="sel-box-item">
				<a className="btn-close" onClick={this.btnCloseClick(item)}>
					<i className="fa fa-close"/>
				</a>
				{this.getOptionDisplay(item)}
			</span>
		);

		return <div className="sel-box-items">{items}</div>;
	}

	/**
	 * Component rendering
	 * @return {React.Component} Component to display
	 */
	render() {
		return (
			<div className="sel-box form-group">
				{this.labelRender()}
				<div className="form-control" onClick={this.controlClick}>
					<div className="btn-dd">
						<i className="fa fa-chevron-down" />
					</div>
					{this.contentRender()}
				</div>
				{this.createPopup()}
			</div>
			);
	}
}

SelectionBox.propTypes = {
	label: React.PropTypes.string,
	optionDisplay: React.PropTypes.any,
	options: React.PropTypes.array,
	onChange: React.PropTypes.func,
	mode: React.PropTypes.oneOf(['single', 'multiple']),
	noOption: React.PropTypes.bool
};

SelectionBox.defaultProps = {
	mode: 'single'
};
