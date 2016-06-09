import React from 'react';
import { MenuItem, FormGroup, FormControl, InputGroup, Button } from 'react-bootstrap';
import { Popup, Profile, Fa } from '../components';

import { generateName, generateCaseNumber } from './mock-data';

import './search-box.less';

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
}
];

export default class SearchBox extends React.Component {

	constructor(props) {
		super(props);

		this.keyPressed = this.keyPressed.bind(this);
		this.clearKey = this.clearKey.bind(this);
		this.state = {};
	}

	componentWillMount() {
		const lst = items.slice(0);

		// generate mock data
		for (var i = 0; i < 100; i++) {
			const res = generateName();
			lst.push({
				type: res.gender.toLowerCase(),
				title: res.name,
				subtitle: generateCaseNumber()
			});
		}

		this.setState({ items: lst });
	}


	clearKey() {
		this.setState({ key: null });
		this.refs.popup.hide();
	}

	keyPressed(evt) {
		const txt = evt.target.value;
		if (txt) {
			this.refs.popup.show();
		} else {
			this.refs.popup.hide();
		}

		this.setState({ key: txt });
	}

	render() {
		let index = 0;

		const key = this.state.key ? this.state.key.toLowerCase() : '';
		const res = key ? this.state.items.filter(it => it.title.toLowerCase().indexOf(key) > -1).slice(0, 15) : items;

		return (
			<div className="tb-search">
			<FormGroup bsClass="form-group">
				<InputGroup>
					<FormControl type="text"
						value={key}
						placeholder={__('action.search') + '...'}
						onChange={this.keyPressed}/>
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
						res.map(it =>
							<MenuItem key={++index}>
								<Profile size="small"
									type={it.type}
									title={it.title}
									subtitle={it.subtitle} />
							</MenuItem>)
					}
				</Popup>
			</FormGroup>
			</div>
			);
	}
}
