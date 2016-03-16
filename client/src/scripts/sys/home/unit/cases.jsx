import React from 'react';
import { Button, ButtonGroup, Grid, Row, Col, OverlayTrigger, Popover, Nav, NavItem, Badge, Alert } from 'react-bootstrap';
import { Card, Profile, WaitIcon, ReactTable } from '../../../components';
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

	/**
	 * Called when user clicks on a case
	 * @param  {[type]} id [description]
	 * @return {[type]}    [description]
	 */
	caseClick(item) {
		app.goto('/sys/home/cases/details/' + item.id);
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
		return this.confirmRender(lst);
	}

	drtbCasesRender() {
		const lst = this.state.drtbCases;
		return this.confirmRender(lst);
	}

	confirmRender(lst) {
		if (lst.length === 0) {
			return this.noRecFoundRender();
		}

		return (
			<ReactTable className="mtop-2x"
				columns={[
					{
						title: 'Patient',
						size: { sm: 4 },
						content: item =>
							<Profile type={item.gender.toLowerCase()} size="small"
								title={item.name} subtitle={item.recordNumber} />
					},
					{
						title: 'Registration date',
						size: { sm: 2 },
						content: item => <div>{item.recordDate}<br/>
								<div className="sub-text">{'30 days ago'}</div></div>
					},
					{
						title: 'Registration group',
						size: { sm: 2 },
						content: item => <div>{item.regGroup.name}<br/>{item.infectionSite.name}</div>
					},
					{
						title: 'Start treatment date',
						size: { sm: 2 },
						content: 'iniTreatmentDate'
					},
					{
						title: 'Progress',
						size: { sm: 2 },
						align: 'center',
						content: () => <img src="images/small_pie2.png" style={{ width: '36px' }} />
					}
				]} values={lst} onClick={this.caseClick}/>
			);
	}

	/**
	 * Return the list of cases to be displayed
	 * @return {React.Component} Component displaying the cases
	 */
	presumptiveRender() {
		const lst = this.state.presumptives;

		// is there any case to display ?
		if (lst.length === 0) {
			return this.noRecFoundRender();
		}

		return (
			<ReactTable columns={
				[{
					title: 'Patient',
					size: { sm: 4 },
					content: item =>
							<Profile type={item.gender.toLowerCase()} size="small"
								title={item.name} subtitle={item.recordNumber} />
				},
				{
					title: 'Registration date',
					size: { sm: 3 },
					content: item =>
						<div>{item.recordDate}
							<div className="sub-text">{'30 days ago'}</div>
						</div>,
					align: 'center'
				},
				{
					title: 'Xpert',
					size: { sm: 2 },
					content: item => item.xpertResult.name
				},
				{
					title: 'Microscopy',
					size: { sm: 2 },
					content: item => item.micResult.name
				}
				]} values={lst} className="mtop-2x" onClick={this.caseClick} />
		);
	}

	/**
	 * Return a message displaying 'No record found'
	 * @return {[type]} [description]
	 */
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
						<Button bsStyle="danger" block style={{ padding: '8px 40px' }}>{'Notify'}</Button>
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
