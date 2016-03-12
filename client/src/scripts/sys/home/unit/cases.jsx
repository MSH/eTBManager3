import React from 'react';
import { Button, ButtonGroup, Grid, Row, Col, OverlayTrigger, Popover, Nav, NavItem, Badge, Alert } from 'react-bootstrap';
import { Card, Profile, WaitIcon } from '../../../components';
import { app } from '../../../core/app';

import { generateName, generateCaseNumber } from '../../mock-data';


const tags = [
	{
		id: '123456-1',
		name: 'Not on treatment',
		type: 'userdef',
		count: 14
	},
	{
		id: '123456-2',
		name: 'On treatment',
		type: 'userdef',
		count: 120
	},
	{
		id: '123456-3',
		name: 'Closed cases',
		type: 'warn',
		count: 243
	},
	{
		id: '123456-4',
		name: 'DR-TB with no resistance with a long name that someone included',
		type: 'danger',
		count: 5
	},
	{
		id: '123456-5',
		name: 'TB with resistance',
		type: 'danger',
		count: 8
	}
];


const cases = [
	{
		name: 'James Hetfield',
		id: '123455',
		recordNumber: '123123-333',
		gender: 'MALE',
		regGroup: {
			id: 'AFTER_FAILURE',
			name: 'After failure of 1st treatment'
		},
		infectionSite: {
			id: 'PULMONARY',
			name: 'Pulmonary'
		},
		recordDate: 'jan 20th, 2016',
		iniTreatmentDate: 'Jan 31th, 2016',
		treatProgess: 35
	},
	{
		name: 'James Hetfield',
		id: '123456',
		recordNumber: '123123-333',
		gender: 'MALE',
		regGroup: {
			id: 'AFTER_FAILURE',
			name: 'After failure of 1st treatment'
		},
		infectionSite: {
			id: 'PULMONARY',
			name: 'Pulmonary'
		},
		recordDate: 'jan 20th, 2016',
		iniTreatmentDate: 'Jan 31th, 2016',
		treatProgess: 35
	},
	{
		name: 'James Hetfield',
		id: '123455',
		recordNumber: '123123-333',
		gender: 'MALE',
		regGroup: {
			id: 'AFTER_FAILURE',
			name: 'After failure of 1st treatment'
		},
		infectionSite: {
			id: 'PULMONARY',
			name: 'Pulmonary'
		},
		recordDate: 'jan 20th, 2016',
		iniTreatmentDate: 'Jan 31th, 2016',
		treatProgess: 35
	},
	{
		name: 'James Hetfield',
		id: '123455',
		recordNumber: '123123-333',
		gender: 'MALE',
		regGroup: {
			id: 'AFTER_FAILURE',
			name: 'After failure of 1st treatment'
		},
		infectionSite: {
			id: 'PULMONARY',
			name: 'Pulmonary'
		},
		recordDate: 'jan 20th, 2016',
		iniTreatmentDate: 'Jan 31th, 2016',
		treatProgess: 35
	}
];

export default class Cases extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			sel: 0,
			presumptives: null,
			tags: null
		};
		this.newPresumptive = this.newPresumptive.bind(this);
		this.tabSelect = this.tabSelect.bind(this);
	}

	componentWillMount() {
		this.loadCases();
	}

	/**
	 * Load the cases to be displayed
	 * @return {[type]} [description]
	 */
	loadCases() {
		const self = this;
		setTimeout(() => {
			const presumptives = [];
			for (var i = 0; i < 20; i++) {
				const res = generateName();
				presumptives.push({
					name: res.name,
					gender: res.gender,
					id: (123456 + i).toString(),
					recordNumber: '123456-789-01',
					recordDate: '21-jan-2016',
					xpertResult: {
						id: 'TB_DETECT',
						name: 'TB Detected'
					},
					micResult: {
						id: 'POSITIVE',
						name: 'Positive'
					}
				});
			}

			const drtb = [];
			for (var n = 0; n < 16; n++) {
				const res = generateName();
				drtb.push({
					name: res.name,
					gender: res.gender,
					id: (123456 + n).toString(),
					recordNumber: generateCaseNumber(),
					regGroup: {
						id: 'AFTER_FAILURE',
						name: 'After failure of 1st treatment'
					},
					infectionSite: {
						id: 'PULMONARY',
						name: 'Pulmonary'
					},
					recordDate: 'jan 20th, 2016',
					iniTreatmentDate: 'Jan 31th, 2016',
					treatProgess: 35
				});
			}

			self.setState({ presumptives: presumptives, tags: tags, drtbCases: drtb, tbCases: [] });
		}, 800);
	}

	/**
	 * Called when selection the 'New presumptive' command
	 * @return {[type]} [description]
	 */
	newPresumptive() {
		app.goto('/sys/home/cases/newnotif');
	}

	newTB() {
		app.goto('/sys/home/cases/newnotif');
	}

	newDRTB() {
		app.goto('/sys/home/cases/newnotif');
	}

	tabSelect(evt) {
		this.setState({ sel: evt });
	}

	/**
	 * Rendering of the cases tab
	 * @return {React.Element} [description]
	 */
	casesRender() {
		if (this.state.presumptives === null) {
			return <WaitIcon type="card"/>;
		}

		const tabs = (
			<Nav bsStyle="tabs" activeKey={this.state.sel} justified
				className="app-tabs2"
				onSelect={this.tabSelect}>
				<NavItem key={0} eventKey={0}>
					{this.listCount(this.state.presumptives)}
					{'Presumptive'}
				</NavItem>
				<NavItem key={1} eventKey={1}>
					{this.listCount(this.state.tbCases)}
					{'TB Cases'}
				</NavItem>
				<NavItem key={2} eventKey={2}>
					{this.listCount(this.state.drtbCases)}
					{'DR-TB Cases'}
				</NavItem>
			</Nav>
			);

		return (
			<Card padding="none">
				{tabs}
				{
					this.state.sel === 0 ? this.presumptiveRender() : null
				}
				{
					this.state.sel === 1 ? this.tbCasesRender() : null
				}
				{
					this.state.sel === 2 ? this.drtbCasesRender() : null
				}
			</Card>
			);
	}


	listCount(lst) {
		const count = lst.length > 0 ? lst.length : '-';
		return <div className="value-big text-primary">{count}</div>;
	}

	tbCasesRender() {
		const lst = this.state.tbCases;

		if (lst.length === 0) {
			return this.noRecFoundRender();
		}
		return null;
	}

	drtbCasesRender() {
		const lst = this.state.drtbCases;

		if (lst.length === 0) {
			return this.noRecFoundRender();
		}

		return (
			<Grid fluid>
				<Row className="tbl-title">
					<Col sm={4}>
						{'Patient'}
					</Col>
					<Col sm={2}>
						{'Registration date'}
					</Col>
					<Col sm={2}>
						{'Registration group'}
					</Col>
					<Col sm={2}>
						{'Start treatment date'}
					</Col>
					<Col sm={2}>
						{'Treatment progress'}
					</Col>
				</Row>
				{
					lst.map(item =>
						<Row key={item.id} className="tbl-row" style={{ padding: '10px 4px' }}>
							<Col sm={4}>
								<Profile type={item.gender.toLowerCase()} size="small"
									title={item.name} subtitle={item.recordNumber} />
							</Col>
							<Col sm={2}>
								<div>{'24-jan-2016'}</div>
								<div className="sub-text">{'30 days ago'}</div>
							</Col>
							<Col sm={2}>
								{'Positive'}
							</Col>
							<Col sm={2}>
								{'TB Positive'}
							</Col>
							<Col sm={2} className="col-center">
								<img src="images/small_pie2.png" style={{ width: '34px' }} />
							</Col>
						</Row>
						)
				}
			</Grid>
			);
	}

	presumptiveRender() {
		const lst = this.state.presumptives;

		if (lst.length === 0) {
			return this.noRecFoundRender();
		}

		return (
			<Grid fluid>
			<Row className="tbl-title">
				<Col sm={4}>
					{'Patient'}
				</Col>
				<Col sm={3}>
					{'Registration date'}
				</Col>
				<Col sm={2}>
					{'Xpert'}
				</Col>
				<Col sm={2}>
					{'Microscopy'}
				</Col>
			</Row>
			{
				lst.map(item =>
					<Row key={item.id} className="tbl-row" style={{ padding: '10px 4px' }}>
						<Col sm={4}>
							<Profile type={item.gender.toLowerCase()} size="small"
								title={item.name} subtitle={item.recordNumber} />
						</Col>
						<Col sm={3}>
							<div>{'24-jan-2016'}</div>
							<div className="sub-text">{'30 days ago'}</div>
						</Col>
						<Col sm={2}>
							{item.xpertResult.name}
						</Col>
						<Col sm={2}>
							{item.micResult.name}
						</Col>
					</Row>
				)
			}
			</Grid>
		);
	}

	noRecFoundRender() {
		return (
			<div className="card-default">
				<div className="card-content">
					<Alert bsStyle="warning">{__('form.norecordfound')}</Alert>
				</div>
			</div>
			);
	}

	render() {

		const popup = (
			<Popover id="ppmenu" title={'Notify'}>
				<ButtonGroup vertical style={{ minWidth: '200px' }}>
					<Button bsStyle="link" onClick={this.newPresumptive}>{'Presumptive'}</Button>
					<Button bsStyle="link" onClick={this.newTB}>{'TB Case'}</Button>
					<Button bsStyle="link" onClick={this.newDRTB}>{'DR-TB Case'}</Button>
				</ButtonGroup>
			</Popover>
			);

		return (
			<Grid fluid>
			<Row className="mtop">
				<Col sm={3}>
					<OverlayTrigger trigger="click" placement="right"
						overlay={popup} rootClose>
						<Button bsStyle="danger" style={{ padding: '8px 40px' }}>{'Notify'}</Button>
					</OverlayTrigger>

					<Card className="mtop" title="Tags">
						<div>
							{
								!this.state.tags ? <WaitIcon type="card" /> :
								this.state.tags.map(item => (
									<a key={item.id} className={'tag-link tag-' + item.type}>
										<Badge pullRight>{item.count}</Badge>
										<div className="tag-title">{item.name}</div>
									</a>
								))
							}
						</div>
					</Card>
				</Col>
				<Col sm={9}>
					{this.casesRender()}
				</Col>
			</Row>
			</Grid>
		);
	}
}
