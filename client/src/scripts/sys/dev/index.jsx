
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Card, Fluidbar } from '../../components/index';
import Popup from '../../components/popup';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	constructor(props) {
		super(props);
	}

	render() {
		return (
			<div>
				<Fluidbar>
					<div className="margin-2x">
						<Profile size="large" title="Developers playgroud"
							subtitle="Your place to test new stuff"
							imgClass="prof-male"
							fa="laptop" />
					</div>
				</Fluidbar>
				<Grid className="mtop-2x">
					<Row>
						<Col md={8} mdOffset={2}>
						<Card>
							<Popup ref="pop1">
								<h3 onClick={this.itemClick} >{'Test 1'}</h3>
								<h3 onClick={this.itemClick} >{'Test 2'}</h3>
							</Popup>
							<Popup ref="pop2">
								<h3 onClick={this.itemClick} >{'Test 3'}</h3>
								<h3 onClick={this.itemClick} >{'Test 4'}</h3>
							</Popup>
						</Card>
						</Col>
					</Row>
				</Grid>
			</div>
			);

	}
}
