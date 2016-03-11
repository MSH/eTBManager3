
import React from 'react';
import { Grid, Row, Col, Input } from 'react-bootstrap';
import { Profile, Fluidbar, Card, AsyncButton } from '../../../components/index';
import { app } from '../../../core/app';

import { generateName } from '../../mock-data';


export default class NewNotif extends React.Component {

	constructor(props) {
		super(props);
		this.searchClick = this.searchClick.bind(this);
		this.state = {};
	}

	searchClick() {
		this.loadCases();
	}

	loadCases() {
		this.setState({ fetching: true });

		setTimeout(() => {
			const lst = [];
			for (var i = 0; i < 10; i++) {
				const res = generateName();
				lst.push({
					name: res.name,
					id: '123456-' + i,
					gender: res.gender,
					age: Math.round(Math.random() * 10) + 20
				});
			}
			this.setState({ patients: lst, fetching: false });
		}, 1000);
	}

	patientsRender() {
		const lst = this.state.patients;

		if (!lst) {
			return null;
		}

		return (
				<Card className="mtop" title="Search result" >
					<div>
					<Row className="tbl-title">
						<Col sm={6} xs={9}>
							{'Patient'}
						</Col>
						<Col sm={2} xs={3}>
							{'Age'}
						</Col>
						<Col sm={4} xs={12}>
							{'Status'}
						</Col>
					</Row>
					{
						lst.map(item =>
							<Row className="tbl-row" key={item.id}>
								<Col sm={6} xs={9}>
									<Profile type={item.gender.toLowerCase()}
										size="small"
										title={item.name}
										/>
								</Col>
								<Col sm={2} xs={3}>
									{item.age}
								</Col>
								<Col sm={4} xs={12}>
									{'TO BE DONE'}
								</Col>
							</Row>
							)
					}
					</div>
				</Card>
			);
	}

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
				<Grid>
					<Row>
						<Col mdOffset={2} md={8}>
							<h1>{'New notification'} </h1>
							<Card title="Search patient">
								<div>
									<Row>
										<Col sm={3}>
											<Input type="text" label="First name:" />
										</Col>
										<Col sm={3}>
											<Input type="text" label="Middle name:" />
										</Col>
										<Col sm={3}>
											<Input type="text" label="Last name:" />
										</Col>
										<Col sm={3}>
											<Input type="text" label="Age:" />
										</Col>
									</Row>
									<Row>
										<Col sm={12}>
											<AsyncButton
												fetching={this.state.fetching}
												onClick={this.searchClick}
												bsStyle="primary">
												{'Search'}
											</AsyncButton>
										</Col>
									</Row>
								</div>
							</Card>
							{this.patientsRender()}
						</Col>
					</Row>
				</Grid>
			</div>
			);
	}
}
