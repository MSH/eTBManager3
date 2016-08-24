import React from 'react';
import { Card } from '../../../components';
import FiltersCard from './filters/filters-card';
import { server } from '../../../commons/server';
import CasesList from '../commons/cases-list';

import CrudController from '../../crud/crud-controller';
import FakeCRUD from '../../../commons/fake-crud';


export default class AdvancedSearch extends React.Component {

	constructor(props) {
		super(props);
		this.searchCases = this.searchCases.bind(this);

		this.state = { };
	}

	componentWillMount() {
		const self = this;

		// get list of filters from the server
		server.post('/api/cases/search/init')
		.then(res => self.setState({ filters: res.filters }));
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

		const crud = new FakeCRUD('/api/cases/search');
		const controller = new CrudController(crud, {
			pageSize: 50
		});
		controller.initList(req);

		this.setState({ controller: controller });
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

		return (
			<div>
				<FiltersCard title={__('cases.advancedsearch')}
					btnLabel={__('action.search')}
					filters={filters}
					onSubmit={this.searchCases}
					onClose={this.props.onClose} />

				{
					this.state.controller &&
					<Card>
						<CasesList controller={this.state.controller}/>
					</Card>
				}
			</div>
			);
	}
}

AdvancedSearch.propTypes = {
	onClose: React.PropTypes.func
};
