
import React from 'react';
import { Grid, Row, Col, MenuItem, Button } from 'react-bootstrap';
import { Profile, Card, Fluidbar } from '../../components/index';
import Popup from '../../components/popup';

import SelectionBox from '../../components/selection-box';

const options = [
	{ value: 1, label: 'banana', color: '#395BFF' },
	{ value: 2, label: 'apple', color: '#FF3A54' },
	{ value: 3, label: 'strawberry', color: '#3B873B' },
	{ value: 4, label: 'orange', color: '#348787' },
	{ value: 5, label: 'Rio de Janeiro', color: '#EB0030' },
	{ value: 6, label: 'São Paulo', color: '#ABCCA4' },
	{ value: 7, label: 'Arlington', color: '#06987B' }
];

const options2 = [
	'Rio de Janeiro',
	'Arlington',
	'São Paulo',
	'Boa Vista',
	'Caxambu',
	'Búzios',
	'New Orleans'
];

/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	constructor(props) {
		super(props);

		this.popupClick = this.popupClick.bind(this);
		this.onChange = this.onChange.bind(this);
		this.state = { selBox1: [options[1], options[2], options[5]] };
	}

	popupClick() {
		this.refs.pop1.show();
	}

	onChange(ref) {
		const self = this;
		return (evt, val) => {
			const obj = {};
			obj[ref] = val;
			self.setState(obj);
		};
	}


	itemDisplay(item) {
		return <span style={{ color: item.color }}>{item.label}</span>;
	}

	render() {
		return (
			<div>
				<Fluidbar>
					<div className="margin-2x">
						<Profile size="large" title="Developers playground"
							subtitle="Your place to test new stuff"
							imgClass="prof-male"
							fa="laptop" />
					</div>
				</Fluidbar>
				<Grid className="mtop-2x">
					<Row>
						<Col md={8} mdOffset={2}>
						<Card>
							<div>
							<Button onClick={this.popupClick}>{'Open'}</Button>
							<Popup ref="pop1">
								<MenuItem>{'Test 1'}</MenuItem>
								<MenuItem>{'Test 2'}</MenuItem>
							</Popup>
							</div>
						</Card>
						</Col>
					</Row>
					<Row>
						<Col md={8} mdOffset={2}>
						<Card>
							<Row>
								<Col sm={6}>
									<SelectionBox ref="selBox1"
										value={this.state.selBox1}
										mode="multiple"
										optionDisplay={this.itemDisplay}
										label="Items:"
										onChange={this.onChange('selBox1')}
										options={options}/>
								</Col>
								<Col sm={6}>
									<SelectionBox ref="selBox2"
										mode="single"
										label="Items:"
										onChange={this.onChange('selBox2')}
										options={options2}
										noOption />
								</Col>
							</Row>
							<Row>
								<Col sm={6}>
									<ul>
										{this.state.selBox1 &&
											this.state.selBox1.map(item => <li key={item.value}>{item.label}</li>)
										}
									</ul>
								</Col>
								<Col sm={6}>
									{this.state.selBox2 && this.state.selBox2}
								</Col>
							</Row>
						</Card>
						</Col>
					</Row>
				</Grid>
			</div>
			);
	}
}
