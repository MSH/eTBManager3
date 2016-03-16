import React from 'react';
import { Grid, Row, Col, Input, Button, ButtonToolbar } from 'react-bootstrap';
import { Profile, Card, AsyncButton, ReactTable } from '../../../components/index';
import { app } from '../../../core/app';


/**
 * TEMPORARY - JUST FOR PROTOTYPING
 */
import { generateName } from '../../mock-data';

/**
 * Component that displays and handle notification form
 */
export default class SearchPatient extends React.Component {

	constructor(props) {
		super(props);
		this.searchClick = this.searchClick.bind(this);
		this.state = {};
	}

	loadPatients() {
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


	searchClick() {
		this.loadPatients();
	}


	/**
	 * Called when user wants to cancel the notification
	 */
	cancelClick() {
		app.goto('/sys/home/unit/cases');
	}

	patientsRender() {
		const lst = this.state && this.state.patients;

		if (!lst) {
			return null;
		}

		return (
			<div>
				<Card className="mtop" title="Search result" >
					<ReactTable values={lst}
						className="mtop"
						onClick={this.props.onSelect}
						columns={[
							{
								title: 'Patient',
								size: { sm: 6, xs: 9 },
								content: item =>
									<Profile type={item.gender.toLowerCase()}
										size="small"
										title={item.name}
										/>
							},
							{
								title: 'Age',
								size: { sm: 2, xs: 3 },
								content: 'age'
							},
							{
								title: 'Status',
								size: { sm: 4, xs: 12 },
								content: () => 'TO BE DONE'
							}
						]} />
				</Card>
			</div>
			);
	}


	render() {
		return (
			<Grid fluid>
				<Row>
					<Col mdOffset={2} md={9}>
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
										<ButtonToolbar>
										<AsyncButton
											fetching={this.state.fetching}
											onClick={this.searchClick}
											bsStyle="primary">
											{'Search'}
										</AsyncButton>
										<Button bsStyle="link" onClick={this.cancelClick}>{__('action.cancel')}</Button>
										</ButtonToolbar>
									</Col>
								</Row>
							</div>
						</Card>
						{this.patientsRender()}
					</Col>
				</Row>
			</Grid>
			);
	}
}

SearchPatient.propTypes = {
	onSelect: React.PropTypes.func
};
