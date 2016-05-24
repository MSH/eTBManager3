import React from 'react';
import { Grid, Col, Row } from 'react-bootstrap';
import { Card, WaitIcon } from '../../../components';
import Form from '../../../forms/form';
import { server } from '../../../commons/server';
import TreatProgress from './treat-progress';


export default class CaseTreatment extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			sc1: {
				layout: [
					{
						property: 'regimen.name',
						type: 'string',
						label: 'Regimen',
						visible: doc => doc.regimen.id === doc.iniRegimen.id,
						size: { md: 12 }
					},
					{
						type: 'text',
						property: 'Started as {iniRegimen.name}\n{regimen.name}',
						label: 'Regimen',
						visible: doc => doc.regimen.id !== doc.iniRegimen.id,
						size: { md: 12 }
					},
					{
						type: 'text',
						property: '{period.ini} to {period.end}',
						label: __('cases.treat'),
						size: { md: 12 }
					}
				]
			}
		};
	}

	componentWillMount() {
		const self = this;
		server.get('/api/cases/treatment/c0a80169-54de-12c2-8154-de6e55500002')
		.then(res => {
			console.log('treatment = ', res);
			self.setState({ data: res });
		});
	}

	render() {
		const data = this.state.data;

		if (!data) {
			return <WaitIcon type="card" />;
		}

		return (
			<div>
			<Card title={__('cases.details.treatment')}>
				<Grid fluid>
					<Row>
						<Col md={6}>
							<Form doc={data} schema={this.state.sc1} readOnly />
						</Col>
						<Col md={6}>
							<TreatProgress value={data.progress}/>
						</Col>
					</Row>
				</Grid>
			</Card>

			<Card title="Prescribed medicines">
			</Card>

			<Card title={__('cases.details.treatment.medintake')}>
			</Card>
			</div>
			);
	}
}

