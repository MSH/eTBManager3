import React from 'react';
import { Button, ButtonGroup, Grid, Row, Col, OverlayTrigger, Popover, Nav, NavItem, Badge, Alert } from 'react-bootstrap';
import { Card, Profile, WaitIcon, ReactTable, Fa, CommandBar } from '../../../components';
import AdvancedSearch from '../cases/advanced-search';
import { app } from '../../../core/app';
import { server } from '../../../commons/server';
import moment from 'moment';
import SessionUtils from '../../session-utils';


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
		this.toggleSearch = this.toggleSearch.bind(this);
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

		const unitId = this.props.route.queryParam('id');

		// get data from the server
		server.post('/api/cases/unit/' + unitId, {})
		.then(res => self.setState({
			presumptives: res.presumptives,
			drtbCases: res.drtbCases,
			tbCases: res.tbCases,
			tags: res.tags,
			sel: res.presumptives.length > 0 ? 0 : 1
		}));
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
		window.location.hash = SessionUtils.caseHash(item.id);
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
				{
					this.state.presumptives.length > 0 &&
					<NavItem key={0} eventKey={0}>
						{this.listCount(this.state.presumptives)}
						{__('cases.suspects')}
					</NavItem>
				}
				<NavItem key={1} eventKey={1}>
					{this.listCount(this.state.tbCases)}
					{__('cases.tb')}
				</NavItem>
				<NavItem key={2} eventKey={2}>
					{this.listCount(this.state.drtbCases)}
					{__('cases.drtb')}
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
		return <div className="value-big text-success">{count}</div>;
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
						content: item => <div>{item.registrationDate}<br/>
								<div className="sub-text">{'30 days ago'}</div></div>
					},
					{
						title: 'Registration group',
						size: { sm: 2 },
						content: item => <div>{item.registrationGroup.name}<br/>{item.infectionSite}</div>
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
					title: __('Patient'),
					size: { sm: 4 },
					content: item =>
							<Profile type={item.gender.toLowerCase()} size="small"
								title={item.name} subtitle={item.recordNumber} />
				},
				{
					title: __('TbCase.registrationDate'),
					size: { sm: 3 },
					content: item => {
						const dt = moment(item.registrationDate);
						return (<div>{dt.format('L')}
							<div className="sub-text">{dt.fromNow()}</div>
						</div>);
					},
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
					content: item => item.microscopyResult
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
			<div className="card-default mtop-2x">
				<div className="card-content">
					<Alert bsStyle="warning">{__('form.norecordfound')}</Alert>
				</div>
			</div>
			);
	}


	/**
	 * Called when user clicks on the advanced search button. Toggles the state
	 * of the search
	 */
	toggleSearch() {
		// toggle state of advanced search
		const search = !app.getState().caseSearch;
		app.setState({ caseSearch: search });
		this.forceUpdate();
	}

	render() {
		const caseSearch = app.getState().caseSearch;

		const popup = (
				<Popover id="ppmenu" title={'Notify'}>
					<ButtonGroup vertical style={{ minWidth: '200px' }}>
						<Button bsStyle="link" onClick={this.newPresumptive}>{'Presumptive'}</Button>
						<Button bsStyle="link" onClick={this.newTB}>{'TB Case'}</Button>
						<Button bsStyle="link" onClick={this.newDRTB}>{'DR-TB Case'}</Button>
					</ButtonGroup>
				</Popover>
			);

		const commands = [
			{
				node: (
					<OverlayTrigger key="pp" trigger="click" placement="right"
						overlay={popup} rootClose>
						<NavItem><Fa icon="coffee" />{'New notification'}</NavItem>
					</OverlayTrigger>
					)
			},
			{
				label: __('cases.advancedsearch'),
				icon: 'feed',
				onClick: this.toggleSearch
			}
		];

		return (
			<Grid fluid>
			<Row className="mtop">
				<Col sm={3}>
					<CommandBar commands={commands} />
					{
						this.state.tags &&
						<Card className="mtop" title="Tags">
							<div>
								{
									!this.state.tags ? <WaitIcon type="card" /> :
									this.state.tags.map(item => (
										<a key={item.id} className={'tag-link tag-' + item.type.toLowerCase()}>
											<Badge pullRight>{item.count}</Badge>
											<div className="tag-title">{item.name}</div>
										</a>
									))
								}
							</div>
						</Card>
					}
				</Col>
				<Col sm={9}>
				{
					caseSearch ?
					<AdvancedSearch onClose={this.toggleSearch}/> :
					this.casesRender()
				}
				</Col>
			</Row>
			</Grid>
		);
	}
}

Cases.propTypes = {
	route: React.PropTypes.object
};
