import React from 'react';
import { MenuItem } from 'react-bootstrap';
import { Popup, Profile } from '../components';

const items = [
{
	type: 'ws',
	title: 'MSH Demo'
},
{
	type: 'tbunit',
	title: 'Health Unit 1',
	subtitle: 'Region 1'
},
{
	type: 'tbunit',
	title: 'Health Unit 2',
	subtitle: 'Region 1'
},
{
	type: 'tbunit',
	title: 'NTP',
	subtitle: 'Region 2'
},
{
	type: 'lab',
	title: 'Laboratory 1',
	subtitle: 'Region 1'
},
{
	type: 'lab',
	title: 'Laboratory 2',
	subtitle: 'Region 2'
},
{
	type: 'male',
	title: 'Bruce Willis',
	subtitle: '2342390'
},
{
	type: 'male',
	title: 'William Smith',
	subtitle: 'TB-123123'
},
{
	type: 'female',
	title: 'Angelina Joulie',
	subtitle: '4758493'
},
{
	type: 'female',
	title: 'Julia Roberts',
	subtitle: '544-3'
},
{
	type: 'male',
	title: 'Jim Morrison',
	subtitle: '69-1'
},
{
	type: 'male',
	title: 'Ricardo Lima',
	subtitle: '292929'
}
];

export default class SearchBox extends React.Component {

	constructor(props) {
		super(props);

		this.keyPressed = this.keyPressed.bind(this);
		this.clearKey = this.clearKey.bind(this);
		this.state = {};
	}


	clearKey() {
		this.setState({ key: null });
		this.refs.popup.hide();
	}

	keyPressed() {
		const txt = this.refs.input.value;
		if (txt) {
			this.refs.popup.show();
		} else {
			this.refs.popup.hide();
		}

		this.setState({ key: txt });
	}

	render() {
		let index = 0;

		const key = this.state.key ? this.state.key.toLowerCase() : null;
		const res = key ? items.filter(it => it.title.toLowerCase().indexOf(key) > -1) : items;

		return (
			<div className="header-search">
				<div className="search-input">
					<input ref="input" type="search"
						value={this.state.key}
						placeholder="Search..." onChange={this.keyPressed} />
					<button onClick={this.clearKey}><i className="fa fa-remove"></i></button>
				</div>
				<Popup ref="popup" >
					{
						res.map(it =>
							<MenuItem key={++index}>
								<Profile size="small"
									type={it.type}
									title={it.title}
									subtitle={it.subtitle} />
							</MenuItem>)
					}
				</Popup>
			</div>
			);
	}
}
