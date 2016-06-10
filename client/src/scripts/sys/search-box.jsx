import React from 'react';
import { MenuItem, FormGroup, FormControl, InputGroup } from 'react-bootstrap';
import { Popup, Profile, Fa } from '../components';

import { generateName, generateCaseNumber } from './mock-data';

import './search-box.less';

const mockItems = [
{
	type: 'ws',
	title: 'MSH Demo',
	id: '123344-1'
},
{
	id: '1231231-2',
	type: 'tbunit',
	title: 'Health Unit 1',
	subtitle: 'Region 1'
},
{
	id: '1231231-3',
	type: 'tbunit',
	title: 'Health Unit 2',
	subtitle: 'Region 1'
},
{
	id: '1231231-4',
	type: 'tbunit',
	title: 'NTP',
	subtitle: 'Region 2'
},
{
	id: '1231231-5',
	type: 'lab',
	title: 'Laboratory 1',
	subtitle: 'Region 1'
},
{
	id: '1231231-6',
	type: 'lab',
	title: 'Laboratory 2',
	subtitle: 'Region 2'
}
];

export default class SearchBox extends React.Component {

	constructor(props) {
		super(props);

		this.keyPressed = this.keyPressed.bind(this);
		this.clearKey = this.clearKey.bind(this);
		this.keyDown = this.keyDown.bind(this);
		this.select = this.select.bind(this);
		this.state = {};
	}

	componentWillMount() {
		const lst = mockItems.slice(0);

		// generate mock data
		for (var i = 0; i < 100; i++) {
			const res = generateName();
			lst.push({
				type: res.gender.toLowerCase(),
				title: res.name,
				id: '112233-2233-4444-' + i,
				subtitle: generateCaseNumber()
			});
		}

		this.setState({ db: lst, items: [] });
	}


	clearKey() {
		this.setState({ key: null, sel: null });
		this.refs.popup.hide();
	}

	keyPressed(evt) {
		const txt = evt.target.value;
		if (txt) {
			this.refs.popup.show();
		} else {
			this.refs.popup.hide();
		}

		const res = txt ?
			this.state.db.filter(it => it.title.toLowerCase().indexOf(txt.toLowerCase()) > -1).slice(0, 15) :
			[];

		this.setState({
			key: txt,
			items: res,
			sel: res.length > 0 ? res[0] : null
		});
	}


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
		console.log('search for ', item);
		this.clearKey();
	}

	render() {
		const key = this.state.key ? this.state.key : '';
		const items = this.state.items;

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
				<Popup ref="popup" >
					{
						items.length > 0 ?
						items.map(it =>
							<MenuItem key={it.id}
								eventKey={it}
								active={this.state.sel === it}
								onSelect={this.select}>
								<Profile size="small"
									type={it.type}
									title={it.title}
									subtitle={it.subtitle} />
							</MenuItem>) :
						<MenuItem disabled>
							<div className="text-warning">{__('form.norecordfound')}</div>
						</MenuItem>
					}
				</Popup>
			</FormGroup>
			</div>
			);
	}
}
