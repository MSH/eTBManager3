import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { RouteView } from '../../../components/router';
import CasesSideView from '../cases/cases-side-view';

import CasesDistribution from '../commons/cases-distribution';
import AdvancedSearch from '../cases/advanced-search';
import TagCasesList from '../cases/tag-cases-list';
import SummaryList from '../commons/summary-list';


const views = [
	{
		title: 'Active cases',
		icon: 'clone',
		path: '/active',
		default: true,
		view: CasesDistribution,
		sideView: true
	},
	{
		title: 'Advanced search',
		icon: 'search',
		path: '/search',
		view: AdvancedSearch,
		sideView: true
	},
	{
		title: __('admin.tags'),
		path: '/tag',
		view: TagCasesList
	},
	{
		title: __('global.summary'),
		path: '/summary',
		view: SummaryList
	}
];

/**
 * Display the main case tab of the administrative unit or workspace view
 */
export default class Cases extends React.Component {

	render() {
		const scope = this.props.scope;

		const adminUnitId = scope === 'ADMINUNIT' ? this.props.route.queryParam('id') : null;

		const routes = RouteView.createRoutes(views);

		const sideViews = views.filter(v => v.sideView);

		return (
			<div className="mtop-2x">
			<Grid fluid>
				<Row>
					<Col sm={3}>
						<CasesSideView
							scope={scope}
							views={sideViews}
							scopeId={adminUnitId}
							route={this.props.route}
							/>
					</Col>
					<Col sm={9}>
						<RouteView routes={routes}
							viewProps={{ scope: this.props.scope, scopeId: adminUnitId }} />
					</Col>
				</Row>
			</Grid>
			</div>
			);
	}
}

Cases.propTypes = {
	route: React.PropTypes.object,
	scope: React.PropTypes.oneOf(['WORKSPACE', 'ADMINUNIT']).isRequired,
	scopeId: React.PropTypes.string
};
