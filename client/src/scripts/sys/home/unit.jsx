
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar } from '../../components/index';
import { app } from '../../core/app';
import SidebarContent from '../sidebar-content';


export default class Unit extends React.Component {

	constructor(props) {
		super(props);
	}


	render() {
		const session = app.getState().session;

		const lst = Object.keys(session.adminUnit)
			.map(k => <a href="/sys/home/admunit">{session.adminUnit[k].name}</a>);

		const subtitle = (
			<div>
				{lst.join(', ')}
			</div>
		);

		// create temporary cells
		var lst2 = [];
		for (var i = 0; i < 12; i++) {
			lst2.push(i);
		}

		return (
			<div>
				<Fluidbar>
					<Grid>
						<Row>
							<Col md={12}>
								<div className="margin-2x">
									<Profile title={session.unitName}
										subtitle={subtitle}
										type="tbunit"
										size="large"
										/>
								</div>
							</Col>
						</Row>
					</Grid>
				</Fluidbar>
			</div>
			);
	}
}

Unit.propTypes = {
	route: React.PropTypes.object
};
