import React from 'react';
import { Card, WaitIcon } from '../../../components';
import CasesList from './cases-list';
import SU from '../../session-utils';

import CrudController from '../../crud/crud-controller';
import FakeCRUD from '../../../commons/fake-crud';


/**
 * Display a card with the list of cases from the summary ID
 * contained in the URL
 */
export default class SummaryList extends React.Component {

	constructor(props) {
		super(props);

		const crud = new FakeCRUD('/api/cases/search');
		const controller = new CrudController(crud, {
			pageSize: 50
		});

		this._controller = controller;
		this.state = {};
	}

	componentWillMount() {
		this.updateList(this.props);
	}

	componentWillReceiveProps(nextProps) {
		const sum = nextProps.route.queryParam('sum');
		const changed = nextProps.scopeId !== this.props.scopeId || sum !== this.state.sum;

		if (changed) {
			this.updateList(nextProps);
		}
	}

	/**
	 * Update the list of cases based on the properties
	 */
	updateList(props) {
		const summaryId = props.route.queryParam('sum');

		const req = {
			pageSize: 50,
			filters: {
				summary: summaryId
			},
			scope: props.scope,
			scopeId: props.scopeId,
			addFilterDisplay: true
		};

		const self = this;
		this._controller.initList(req)
			.then(res => self.setState({
				data: res.serverData,
				sum: summaryId
			}));
	}

	caseClick(data) {
		window.location = SU.caseHash(data.id);
	}

	render() {
		const res = this.state.data;

		if (!res) {
			return <WaitIcon />;
		}

		return (
			<Card title={res.filters.summary.value}>
				<CasesList
					controller={this._controller}
					onCaseClick={this.caseClick} />
			</Card>
		);
	}
}

SummaryList.propTypes = {
	scope: React.PropTypes.string.isRequired,
	scopeId: React.PropTypes.string
};
