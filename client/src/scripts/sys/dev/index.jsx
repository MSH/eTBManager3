
import React from 'react';
import { Grid, Row, Col, MenuItem } from 'react-bootstrap';
import { Profile, Card, Fluidbar } from '../../components/index';
import Popup from '../../components/popup';

import SelectionBox from '../../components/selection-box';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	constructor(props) {
		super(props);

		this._onChangeAdmUnit = this._onChangeAdmUnit.bind(this);
		this.state = {};
	}

	_onChangeAdmUnit(evt) {
		this.setState({ adminUnit: evt.value });
	}

	render() {
		const options = [
			{ value: 1, label: 'banana' },
			{ value: 2, label: 'apple' },
			{ value: 3, label: 'strawberry' },
			{ value: 4, label: 'orange' }
		];

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
							<Popup ref="pop1">
								<MenuItem>{'Test 1'}</MenuItem>
								<MenuItem>{'Test 2'}</MenuItem>
							</Popup>
							<Popup ref="pop2">
								<h3 onClick={this.itemClick} >{'Test 3'}</h3>
								<h3 onClick={this.itemClick} >{'Test 4'}</h3>
							</Popup>
							</div>
						</Card>
						</Col>
					</Row>
					<Row>
						<Col md={8} mdOffset={2}>
						<Card>
							<Row>
								<Col sm={12}>
									<SelectionBox label="Medicines:" options={options}/>
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
