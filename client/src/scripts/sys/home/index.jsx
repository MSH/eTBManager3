
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Card, Fluidbar } from '../../components/index';
import { app } from '../../core/app';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	render() {
		const state = app.getState();

		const lst = Object.keys(state.session.adminUnit);

		const aus = [];
		let count = 0;
		lst.forEach(key => {
			count++;
			let s = state.session.adminUnit[key].name;
			if (count < lst.length) {
				s += ', ';
			}
			aus.push(<a href="#" key={key}>{s}</a>);
		});

		const subtitle = (
			<div><a href="#">{state.session.unitName}</a>
			<div>{aus}</div>
			<a href="#">{state.session.workspaceName}</a>
			</div>
		);

		const unitName = 'Centro de Referência Professor Hélio Fraga';

		return (
			<div>
				<Fluidbar>
					<Grid>
						<Row>
							<Col md={12}>
								<div className="margin-2x">
									<Profile title={state.session.userName} subtitle={subtitle} fa="user" size="large"/>
								</div>
							</Col>
						</Row>
					</Grid>
				</Fluidbar>
				<Grid>
					<Row>
						<Col md={8} mdOffset={2}>
						<Card>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="large"/>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="medium"/>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="small"/>
						</Card>
						</Col>
					</Row>
				</Grid>
			</div>
			);
	}
}
