
import React from 'react';
import { Row, Col, MenuItem, Button } from 'react-bootstrap';
import { Card, Popup, SelectionBox, ListBox, MaskedInput } from '../../components/index';
import FormModalExample from './form-modal-example';


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
	'New Orleans',
	'Finlandia',
	'Duque de Caxias',
	'Blumenau',
	'Dhaka',
	'Namibia',
	'Nigeria',
	'Russia',
	'Caxias',
	'Jd. 25 de Agosto',
	'Santa Cruz da Serra'
];

const options3 = [
	'Yes',
	'No'
];


/**
 * Initial page that declare all routes of the module
 */
export default class StuffExamples extends React.Component {

	constructor(props) {
		super(props);

		this.popupClick = this.popupClick.bind(this);
		this.onChange = this.onChange.bind(this);

		this.state = { selBox1: [options[1], options[2], options[5]] };
	}

	popupClick() {
		this.refs.pop1.show();
	}

	itemDisplay(item) {
		return (
			<span style={{ color: item.color }} >{item.label}
			</span>
		);
	}

	onChange(ref) {
		const self = this;
		return (val) => {
			const obj = {};
			obj[ref] = val;
			self.setState(obj);
		};
	}

	onChange2(ref) {
		const self = this;
		return (evt, val) => {
			const obj = {};
			obj[ref] = val;
			self.setState(obj);
		};
	}

	show(cpt, show) {
		const self = this;
		return () => {
			const obj = {};
			obj[cpt] = show;
			self.setState(obj);
		};
	}


	render() {
		return (
			<div>
				<Card>
					<div>
					<Button onClick={this.popupClick}>{'Open'}</Button>
					<Popup ref="pop1">
						<MenuItem>{'Test 1'}</MenuItem>
						<MenuItem>{'Test 2'}</MenuItem>
					</Popup>
					</div>
				</Card>

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
								help="This is a simple help message"
								onChange={this.onChange('selBox2')}
								options={options2}
								noSelectionLabel="-" />
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
							{this.state.selBox1 && this.state.selBox2}
						</Col>
					</Row>
				</Card>

				<Card>
					<Row>
						<Col sm={6}>
							<ListBox ref="selBox3"
								mode="single"
								label="Items:"
								help="This is a simple help message"
								onChange={this.onChange2('selBox3')}
								options={options2}
								vertical />
						</Col>
						<Col sm={6}>
							<ListBox ref="selBox4" bsStyle="error"
								mode="single"
								label="Items:"
								help="This is a simple help message"
								onChange={this.onChange2('selBox4')}
								options={options3} />
						</Col>
					</Row>
					<Row>
						<Col sm={6}>
							{this.state.selBox3}
						</Col>
						<Col sm={6}>
							{this.state.selBox4}
						</Col>
					</Row>
				</Card>

				<Card>
					<Row>
						<Col sm={6}>
							<ListBox ref="selBox5"
								mode="multiple"
								label="Items:"
								help="This is a simple help message"
								onChange={this.onChange('selBox5')}
								options={options2}
								vertical
								maxHeight={130} />
						</Col>
						<Col sm={6}>
							{this.state.selBox5 &&
									this.state.selBox5.map(item => <li key={item}>{item}</li>)
							}
						</Col>
					</Row>
				</Card>

				<Card>
					<Row>
						<Col sm={12}>
							<Button onClick={this.show('showFormModal', true)}>{'Show Form Modal'}</Button>
							<FormModalExample show={this.state.showFormModal} onClose={this.show('showFormModal', false)} />
						</Col>
					</Row>
				</Card>

				<Card>
					<Row>
						<Col sm={6}>
							<MaskedInput ref="maskedInput1"
								label="Masked Input:"
								help="This is a simple help message"
								onChange={this.onChange('selBox3')}
								bsStyle="error" />
						</Col>
					</Row>
					<Row>
						<Col sm={6}>
							{this.state.selBox3}
						</Col>
					</Row>
				</Card>
			</div>
			);
	}
}
