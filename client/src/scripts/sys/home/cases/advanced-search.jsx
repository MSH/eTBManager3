import React from 'react';
import { Card, ReactTable, Profile } from '../../../components';
import FiltersCard from './filters/filters-card';
import { app } from '../../../core/app';
import { server } from '../../../commons/server';

import { generateName, generateCaseNumber } from '../../mock-data';



export default class AdvancedSearch extends React.Component {

	constructor(props) {
		super(props);
		this.searchCases = this.searchCases.bind(this);

		this.state = {};
	}

	componentWillMount() {
		const self = this;

		// get list of filters from the server
		server.post('/api/cases/search/init')
		.then(res => self.setState({ filters: res.filters }));
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
	searchCases(filters) {
		const req = {
			pageSize: 50,
			filters: {}
		};

		// check if filters were declared
		if (filters) {
			filters.forEach(it => {
				req.filters[it.filter.id] = it.value;
			});
		}

		const self = this;

		// get list of filters from the server
		return server.post('/api/cases/search', req)
		.then(res => {
			console.log(res);
			self.setState({ cases: res });
		});

		// return new Promise(resolve => {
		// 	setTimeout(() => {
		// 		// generate mock data of the cases to display
		// 		const lst = [];
		// 		for (var i = 0; i < 20; i++) {
		// 			const res = generateName();
		// 			lst.push({
		// 				name: res.name,
		// 				gender: res.gender,
		// 				id: (123456 + i).toString(),
		// 				recordNumber: generateCaseNumber(),
		// 				regGroup: {
		// 					id: 'AFTER_FAILURE',
		// 					name: 'After failure of 1st treatment'
		// 				},
		// 				infectionSite: {
		// 					id: 'PULMONARY',
		// 					name: 'Pulmonary'
		// 				},
		// 				recordDate: 'jan 20th, 2016',
		// 				iniTreatmentDate: 'Jan 31th, 2016',
		// 				treatProgess: 35
		// 			});
		// 		}
		// 		resolve(true);
		// 		this.setState({ fetching: false, cases: lst });
		// 	}, 500);

		// });
	}

	/**
	 * Called when the user changes the filter to a new filter in the filter panel
	 * @param  {object} fval the filter value
	 */
	changeFilter(fval) {
		const self = this;
		return filter => {
			fval.filter = filter;
			self.forceUpdate();
		};
	}

	render() {
		const filters = this.state.filters;
		const values = app.getState().filterValues;

		return (
			<div>
				<FiltersCard title={__('cases.advancedsearch')}
					btnLabel={__('action.search')}
					filters={filters}
					onSubmit={this.searchCases}
					onClose={this.props.onClose} />

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
