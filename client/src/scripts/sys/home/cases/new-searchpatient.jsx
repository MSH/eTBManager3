import React from 'react';
import { Grid, Row, Col, Button, ButtonToolbar } from 'react-bootstrap';
import { Profile, Card, AsyncButton, ReactTable } from '../../../components/index';
import { app } from '../../../core/app';
import Form from '../../../forms/form';


/**
 * TEMPORARY - JUST FOR PROTOTYPING
 */
import { generateName } from '../../mock-data';

const fschema = {
			controls: [
				{
					property: 'firstName',
					required: true,
					type: 'string',
					max: 100,
					label: __('Patient.firstName'),
					size: { sm: 3 }
				},
				{
					property: 'middleName',
					required: false,
					type: 'string',
					max: 100,
					label: __('Patient.middleName'),
					size: { sm: 3 }
				},
				{
					property: 'lastName',
					required: false,
					type: 'string',
					max: 100,
					label: __('Patient.lastName'),
					size: { sm: 3 }
				},
				{
					property: 'birthDate',
					required: true,
					type: 'date',
					label: __('Patient.birthDate'),
					size: { md: 3 }
				}
			]
		};

/**
 * Component that displays and handle notification form
 */
export default class SearchPatient extends React.Component {

	constructor(props) {
		super(props);
		this.searchClick = this.searchClick.bind(this);

		this.state = { doc: {} };
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
		const errors = this.refs.form.validate();
		this.setState({ errors: errors });
		if (errors) {
			return;
		}

		this.loadPatients();
	}

	patientsRender() {
		const lst = this.state && this.state.patients;

		if (!lst) {
			return null;
		}

		return (
			<div>
				<Card className="mtop">
					<ReactTable values={lst}
						className="mtop"
						onClick={this.props.onSelect}
						columns={[
							{
								title: __('Patient'),
								size: { sm: 6, xs: 9 },
								content: item =>
									<Profile type={item.gender.toLowerCase()}
										size="small"
										title={item.name} />
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
						<Card title={__('cases.searchpatient')} className='mtop'>
							<div>
								<Form ref="form"
									schema={fschema}
									doc={this.state.doc}
									errors={this.state.errors} />
								<Row>
									<Col sm={12}>
										<ButtonToolbar>
										<AsyncButton
											fetching={this.state.fetching}
											onClick={this.searchClick}
											bsStyle="primary">
											{'Search'}
										</AsyncButton>
										<Button bsStyle="link" onClick={this.props.onCancel}>{__('action.cancel')}</Button>
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
	onSelect: React.PropTypes.func,
	onCancel: React.PropTypes.func
};
