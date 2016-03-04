import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Card, Profile } from '../../components/index';
import { app } from '../../core/app';
import { router } from '../../components/router';


export default class IndexDashboard extends React.Component {

	render() {
		const session = app.getState().session;

		const aulist = Object.keys(session.adminUnit).map(k => session.adminUnit[k].name);

		return (
			<div>
			<h1>{'Dashboard'}</h1>
			<Card>
				<div>
					<Profile title={session.unitName}
						subtitle={aulist.join(', ')}
						size="small"
						type="tbunit"
						/>
					<Row className="mtop-2x">
						<Col sm={6}>
							<Card title="Cases" className="card-indicator collapse-card"
								onClick={() => router.goto('/sys/home/unit/cases')}>
								<Row>
									<Col xs={6}>
										<div className="ind-value text-primary">{121}</div>
										<div className="ind-label">{'Presumptives'}</div>
									</Col>
									<Col xs={6}>
										<div className="ind-value text-primary">{78}</div>
										<div className="ind-label">{'Cases on treatment'}</div>
									</Col>
								</Row>
							</Card>
						</Col>
						<Col sm={6}>
							<Card title="Inventory" className="card-indicator collapse-card"
								onClick={() => router.goto('/sys/home/unit/cases')}>
								<Row>
									<Col xs={6}>
										<div className="ind-value text-danger">{'28 days'}</div>
										<div className="ind-label">{'Estimated stock-out'}</div>
									</Col>
									<Col xs={6}>
										<div className="ind-value text-primary">{'84 days'}</div>
										<div className="ind-label">{'First batch to expire'}</div>
									</Col>
								</Row>
							</Card>
						</Col>
					</Row>
				</div>
			</Card>
			</div>
		);
	}
}
