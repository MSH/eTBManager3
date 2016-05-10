import React from 'react';
import { Grid, Col, Row, Input } from 'react-bootstrap';
import { Card, SelectionBox, LinkTooltip, AsyncButton, ReactTable, Profile } from '../../../components';
import { app } from '../../../core/app';

import { generateName, generateCaseNumber } from '../../mock-data';


const filtersMock = [
	{
		id: 'name',
		name: 'Patient name/number',
		type: 'text'
	},
	{
		id: 'classification',
		name: 'Case classification',
		type: 'select',
		options: [
			{ id: 'TB', name: 'TB cases' },
			{ id: 'DRTB', name: 'DR-TB cases' }
		]
	},
	{
		id: 'diagType',
		name: 'Type of diagnosis',
		type: 'select',
		options: [
			{ id: 'PRESUMPTIVE', name: 'Presumptive' },
			{ id: 'CONFIRMED', name: 'Confirmed case' }
		]
	},
	{
		id: 'case-status',
		name: 'Case status',
		type: 'select',
		options: [
			{ id: 'NOT_ONTREATMENT', name: 'Not on treatment' },
			{ id: 'ONTREATMENT', name: 'On treatment' },
			{ id: 'CLOSED', name: 'Closed' }
		]
	},
	{
		id: 'case-outcome',
		name: 'Case outcome',
		type: 'select',
		options: [
			{ id: 'CURED', name: 'Cured' },
			{ id: 'TREAT_COMPLETED', name: 'Treatment completed' },
			{ id: 'DIED', name: 'Died' },
			{ id: 'LOST_FOLLOWUP', name: 'Lost follow-up' },
			{ id: 'TREAT_INTERRUPTED', name: 'Treatment interrupted' }
		]
	},
	{
		id: 'reg-date',
		name: 'Registration date',
		type: 'period'
	},
	{
		id: 'diag-date',
		name: 'Diagnosis date',
		type: 'period'
	}
];

export default class AdvancedSearch extends React.Component {

	constructor(props) {
		super(props);
		this.addFilter = this.addFilter.bind(this);
		this.searchCases = this.searchCases.bind(this);
		this.removeFilter = this.removeFilter.bind(this);
		this.changeFilter = this.changeFilter.bind(this);

		this.state = {};
	}

	componentWillMount() {
		const filterValues = [];
		filterValues.push({
			filter: filtersMock[0],
			value: null
		});

		app.setState({ filters: filtersMock,  filterValues: filterValues });
	}


	/**
	 * Add a new filter to the filter panel. Called when user clicks on the 'add filter' button
	 */
	addFilter() {
		const lst = app.getState().filterValues;
		const filters = app.getState().filters;

		// add a new filter value
		const fv = {
			filter: filters[lst.length],
			value: null
		};

		lst.push(fv);
		app.setState({ filterValues: lst });
		this.forceUpdate();
	}

	/**
	 * Remove a filter from the filter panel.
	 * @param  {object} item The object containing the filter and value to remove
	 * @return {function}      Function to be called to remove the filter value
	 */
	removeFilter(item) {
		const self = this;
		return () => {
			const lst = app.getState().filterValues;
			const index = lst.indexOf(item);
			lst.splice(index, 1);
			self.forceUpdate();
		};
	}

	/**
	 * Rend the control to display and change the filter value
	 * @param  {object} fval object containing the filter and its value
	 * @return {React.Component}      The control to be displayed
	 */
	filterValueRender(fval) {
		switch (fval.filter.type) {
			case 'text':
				return <Input type="text" defaultValue={fval.value} />;
			case 'select':
				return <SelectionBox mode="multiple" options={fval.filter.options} optionDisplay="name" />;
			case 'period':
				return <Input type="text" defaultValue="TO BE IMPLEMENTED" />;
			default: throw new Error('Not supported ' + fval.type);
		}
	}

	/**
	 * Rend the case search result, if there is any case available
	 */
	casesRender() {
		const lst = this.state.cases;

		if (!lst) {
			return null;
		}

		return (
			<Card title="Search result">
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
			</Card>
			);
	}

	/**
	 * Called when user clicks on the search button. Search the cases based on the filter values
	 */
	searchCases() {
		this.setState({ fetching: true });
		setTimeout(() => {
			// generate mock data of the cases to display
			const lst = [];
			for (var i = 0; i < 20; i++) {
				const res = generateName();
				lst.push({
					name: res.name,
					gender: res.gender,
					id: (123456 + i).toString(),
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
			this.setState({ fetching: false, cases: lst });
		}, 500);
	}

	/**
	 * Called when the user changes the filter to a new filter in the filter panel
	 * @param  {object} fval the filter value
	 */
	changeFilter(fval) {
		const self = this;
		return (evt, filter) => {
			fval.filter = filter;
			self.forceUpdate();
		};
	}

	render() {
		const filters = app.getState().filters;
		const values = app.getState().filterValues;

		if (!filters) {
			return null;
		}

		return (
			<div>
			<Card title={__('cases.advancedsearch')} closeBtn onClose={this.props.onClose}>
				<div>
					<Grid fluid>
					{
						values.map(f => (
							<Row key={f.filter.id}>
								<Col sm={5} className="filter">
									<SelectionBox value={f.filter}
										options={filters}
										optionDisplay="name"
										onChange={this.changeFilter(f)} />
									<LinkTooltip toolTip="Remove filter"
										onClick={this.removeFilter(f)}
										icon="minus"
										className="remove-link" />
								</Col>
								<Col sm={7}>
								{
									this.filterValueRender(f)
								}
								</Col>
							</Row>
							))
					}
					<Row>
						<Col sm={12}>
							<AsyncButton className="pull-right"
								onClick={this.searchCases}
								bsStyle="primary"
								fetching={this.state.fetching}>
								{__('action.search')}
							</AsyncButton>
							<LinkTooltip toolTip="Add filter" onClick={this.addFilter} icon="plus"/>
						</Col>
					</Row>
					</Grid>
				</div>
			</Card>
			{
				this.casesRender()
			}
			</div>
			);
	}
}

AdvancedSearch.propTypes = {
	onClose: React.PropTypes.func
};
