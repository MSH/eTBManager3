import React from 'react';
import { MenuItem, FormGroup, FormControl, InputGroup } from 'react-bootstrap';
import { Popup, Profile, Fa, WaitIcon } from '../components';
import { server } from '../commons/server';
import { app } from '../core/app';
import SessionUtils from './session-utils';

import './search-box.less';


/**
 * Object map to convert the type returned by the server to the corresponding
 * in the Profile component
 * @type {Object}
 */
const profileMap = {
	WORKSPACE: 'ws',
	ADMINUNIT: 'place',
	TBUNIT: 'tbunit',
	LAB: 'lab',
	CASE_MAN: 'male',
	CASE_WOMAN: 'female'
};

export default class SearchBox extends React.Component {

	constructor(props) {
		super(props);

		this.keyPressed = this.keyPressed.bind(this);
		this.clearKey = this.clearKey.bind(this);
		this._popupHide = this._popupHide.bind(this);
		this.keyDown = this.keyDown.bind(this);
		this.select = this.select.bind(this);
		this.state = { };
	}

	clearKey() {
		this.setState({ key: null, sel: null });
		this.refs.popup.hide();
	}

	_popupHide() {
		this.setState({ key: null, sel: null });
	}

	keyPressed(evt) {
		const txt = evt.target.value;
		const searching = txt.length > 0;

		if (searching) {
			this.refs.popup.show();
		} else {
			this.refs.popup.hide();
		}

		const self = this;
		// check if there are more than 2 chars
		if (searching) {
			// call the server for items that match the key
			server.post('/api/session/search', { key: txt, maxResults: 10 })
			.then(res => {
				self.setState({
					items: res,
					fetching: false,
					sel: res.length > 0 ? res[0] : null
				});
			})
			.catch(() => self.clearKey());
		}

		this.setState({ key: txt, fetching: true });
	}


	/**
	 * Keyboard support for the search input box
	 * @param  {[type]} evt [description]
	 * @return {[type]}     [description]
	 */
	keyDown(evt) {
		const lst = this.state.items;
		if (!lst || lst.length === 0) {
			return;
		}

		const index = this.state.sel ? lst.indexOf(this.state.sel) : -1;

		switch (evt.keyCode) {
			// UP key
			case 38:
				if (index > 0) {
					this.setState({ sel: lst[index - 1] });
				}
				evt.preventDefault();
				return;
			// DOWN key
			case 40:
				if (index < lst.length - 1) {
					this.setState({ sel: lst[index + 1] });
				}
				evt.preventDefault();
				return;
			// ESC key
			case 27:
				this.clearKey();
				return;
			// Enter key
			case 13:
				this.select(this.state.sel);
				return;
			default: return;
		}
	}

	select(item) {
		window.location.hash = this.getHash(item);
		this.clearKey();
	}

	getHash(item) {
		switch (item.type) {
			case 'WORKSPACE': return SessionUtils.workspaceHash();
			case 'ADMINUNIT': return SessionUtils.adminUnitHash(item.id);
			case 'TBUNIT':
			case 'LAB': return SessionUtils.unitHash(item.id);
			case 'CASE_WOMAN':
			case 'CASE_MAN': return SessionUtils.caseHash(item.id);
			default: return null;
		}
	}

	/**
	 * Render the popup panel to display the autocomplete options
	 * @return {[type]} [description]
	 */
	renderPopup() {
		const items = this.state.items;

		if (!items) {
			if (this.state.fetching) {
				return <WaitIcon type="field" />;
			}
			return null;
		}

		if (items.length === 0) {
			return (
				<MenuItem header>
					<div className="text-warning">{__('form.norecordfound')}</div>
				</MenuItem>
				);
		}

		const lst = [];

		items.forEach((it, index) => {
			// check if should include a divider
			const caseType = it.type.startsWith('CASE');
			const prevCaseType = index > 0 && items[index - 1].type.startsWith('CASE');
			if (index > 0 && caseType !== prevCaseType) {
				lst.push(<MenuItem key={'s' + it.id} divider />);
			}

			// add menu item
			lst.push(
				<MenuItem key={it.id}
					eventKey={it}
					active={this.state.sel === it}
					onSelect={this.select}>
					<Profile size="small"
						type={profileMap[it.type]}
						title={it.title}
						subtitle={it.subtitle} />
				</MenuItem>);
		});

		return lst;
	}


	render() {
		const key = this.state.key ? this.state.key : '';

		return (
			<div className="tb-search">
			<FormGroup bsClass="form-group">
				<InputGroup>
					<FormControl type="text"
						value={key}
						placeholder={__('action.search') + '...'}
						onChange={this.keyPressed}
						onKeyDown={this.keyDown}
						/>
					<InputGroup.Addon>
					{
						key ?
						<a onClick={this.clearKey} className="clearbtn"><Fa icon="close"/></a> :
						<i className="fa fa-search" />
					}
					</InputGroup.Addon>
				</InputGroup>
				<Popup ref="popup" onHide={this._popupHide}>
					{this.renderPopup()}
				</Popup>
			</FormGroup>
			</div>
			);
	}
}
