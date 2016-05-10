import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar } from '../../../components/index';
import { app } from '../../../core/app';


export default class UnitPanel extends React.Component {

	render() {
		const session = app.getState().session;

		const lst = [];
		const keys = Object.keys(session.adminUnit);
		keys.forEach((k, index) => {
				lst.push(<a key={index} href="/sys/home/admunit">{session.adminUnit[k].name}</a>);
				if (index < keys.length - 1) {
					lst.push(<span key={'s' + index}>{', '}</span>);
				}
			});

		const subtitle = (
			<div>
				{lst}
				<br/>
				<a>{session.workspaceName}</a>
			</div>
		);

		// create temporary cells
		var lst2 = [];
		for (var i = 0; i < 12; i++) {
			lst2.push(i);
		}

		return (
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
					{this.props.content}
				</Grid>
			</Fluidbar>
			);
	}
}

UnitPanel.propTypes = {
	content: React.PropTypes.node
};
