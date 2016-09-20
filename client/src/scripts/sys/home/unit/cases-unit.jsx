import React from 'react';
import { Nav, NavItem, Alert } from 'react-bootstrap';
import { Card, Profile, WaitIcon, ReactTable } from '../../../components';
import { server } from '../../../commons/server';
import moment from 'moment';
import SessionUtils from '../../session-utils';

/**
 * Display the active cases of the selected unit. The unit ID is in the URL
 */
export default class CasesUnit extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			sel: 0
		};
		this.tabSelect = this.tabSelect.bind(this);
	}

	componentWillMount() {
		this.updateState(this.props);
	}

	componentWillReceiveProps(nextProps) {
		this.updateState(nextProps);
	}

	updateState(props) {
		const newId = props.route.queryParam('id');
		const id = this.state.id;

		if (!id === newId) {
			return;
		}

		const self = this;

		server.post('/api/cases/unit/' + newId)
		.then(res => self.setState({
				cases: res,
				id: newId,
				sel: res.presumptives && res.presumptives.length > 0 ? 0 : 1
		}));

	}

	listCount(lst) {
		const count = lst.length > 0 ? lst.length : '-';
		return <div className="value-big text-success">{count}</div>;
	}

	tbCasesRender() {
		const lst = this.state.cases.tbCases;
		return this.confirmRender(lst);
	}

	drtbCasesRender() {
		const lst = this.state.cases.drtbCases;
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
								title={SessionUtils.nameDisplay(item.name)} subtitle={item.recordNumber} />
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
		const lst = this.state.cases.presumptives;

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
								title={SessionUtils.nameDisplay(item.name)} subtitle={item.recordNumber} />
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

    render() {
		const cases = this.state.cases;
		const sel = this.state.sel;

		if (!cases) {
			return <WaitIcon type="card"/>;
		}

		const tabs = (
			<Nav bsStyle="tabs" activeKey={this.state.sel} justified
				className="app-tabs2"
				onSelect={this.tabSelect}>
				{
					cases.presumptives.length > 0 &&
					<NavItem key={0} eventKey={0}>
						{this.listCount(cases.presumptives)}
						{__('cases.suspects')}
					</NavItem>
				}
				<NavItem key={1} eventKey={1}>
					{this.listCount(cases.tbCases)}
					{__('cases.tb')}
				</NavItem>
				<NavItem key={2} eventKey={2}>
					{this.listCount(cases.drtbCases)}
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
}

CasesUnit.propTypes = {
    route: React.PropTypes.object.isRequired
};
