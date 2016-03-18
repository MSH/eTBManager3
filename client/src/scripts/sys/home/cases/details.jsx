import React from 'react';
import { Grid, Row, Col, DropdownButton, MenuItem, Nav, NavItem } from 'react-bootstrap';
import { Card, WaitIcon } from '../../../components';
import PatientPanel from '../commons/patient-panel';

import CaseData from './case-data';
import CaseExams from './case-exams';
import CaseTreatment from './case-treatment';


const tags = [
	{
		id: '123456-1',
		name: 'Not on treatment',
		type: 'userdef'
	},
	{
		id: '123456-2',
		name: 'On treatment',
		type: 'userdef'
	},
	{
		id: '123456-3',
		name: 'Closed cases',
		type: 'warn'
	},
	{
		id: '123456-4',
		name: 'DR-TB with no resistance',
		type: 'danger'
	},
	{
		id: '123456-5',
		name: 'TB with resistance',
		type: 'danger'
	}
];

export default class Details extends React.Component {

	constructor(props) {
		super(props);
		this.selectTab = this.selectTab.bind(this);

		this.state = { selTab: 0 };
	}

	componentWillMount() {
		setTimeout(() => {
			this.setState({
				tbcase: {
					patient: {
						name: 'Jim Morrison',
						gender: 'MALE',
						birthDate: new Date(1970, 1, 1)
					},
					recordNumber: '12345-2'
				},
				tags: tags
			});
		}, 100);
	}

	tagsRender() {
		return (
			<div>
				{
					!this.state.tags ? <WaitIcon type="card" /> :
					this.state.tags.map(item => (
						<a key={item.id} className={'tag-link tag-' + item.type}>
							<div className="tag-title">{item.name}</div>
						</a>
					))
				}
			</div>
			);
	}

	selectTab(key) {
		this.setState({ selTab: key });
	}

	render() {
		const tbcase = this.state.tbcase;

		if (!tbcase) {
			return <WaitIcon type="page" />;
		}

		const seltab = this.state.selTab;

		const tabs = (
			<Nav bsStyle="tabs" activeKey={seltab}
				onSelect={this.selectTab}
				className="app-tabs">
				<NavItem key={0} eventKey={0}>{'Data'}</NavItem>
				<NavItem key={1} eventKey={1}>{'Exams'}</NavItem>
				<NavItem key={2} eventKey={2}>{'Treatment'}</NavItem>
			</Nav>
			);

		return (
			<div>
				<PatientPanel patient={tbcase.patient} recordNumber={tbcase.recordNumber} />
				<Grid fluid>
					<Row className="mtop">
						<Col sm={3}>
							<DropdownButton id="ddcase" bsStyle="danger" title={__('form.options')} >
								<MenuItem eventKey={1}>{__('cases.delete')}</MenuItem>
								<MenuItem eventKey={1}>{__('cases.close')}</MenuItem>
								<MenuItem eventKey={1}>{__('cases.move')}</MenuItem>
							</DropdownButton>
							<Card className="mtop" title="Tags">
							{
								this.tagsRender()
							}
							</Card>
							<Card title="Other cases of this patient" />
						</Col>
						<Col sm={9}>
							<Card >
							<div>
								{tabs}
								{seltab === 0 && <CaseData tbcase={tbcase} />}
								{seltab === 1 && <CaseExams tbcase={tbcase} />}
								{seltab === 2 && <CaseTreatment tbcase={tbcase} />}
							</div>
							</Card>
						</Col>
					</Row>
				</Grid>
			</div>
			);
	}
}
