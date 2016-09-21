import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { RouteView } from '../../../components/router';
import CasesSideView from '../cases/cases-side-view';

import CasesDistribution from '../commons/cases-distribution';
import AdvancedSearch from '../cases/advanced-search';
import TagCasesList from '../cases/tag-cases-list';


const views = [
	{
		title: 'Active cases',
		icon: 'clone',
		path: '/active',
		default: true,
		view: CasesDistribution
	},
	{
		title: 'Advanced search',
		icon: 'search',
		path: '/search',
		view: AdvancedSearch
	},
	{
		title: __('admin.tags'),
		path: '/tag',
		view: TagCasesList
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

		return (
			<div className="mtop-2x">
			<Grid fluid>
				<Row>
					<Col sm={3}>
						<CasesSideView
							scope={scope}
							views={views}
							scopeId={adminUnitId}
							route={this.props.route}
							/>
					</Col>
					<Col sm={9}>
						<RouteView routes={routes}
							viewProps={{ scope: this.props.scope }} />
					</Col>
				</Row>
			</Grid>
			</div>
			);

						// {
						// 	this.state.selectedTag ? <TagCasesList onClose={this.closeTagCasesList} tag={this.state.selectedTag} adminUnitId={adminUnitId}/> : null
						// }
						// {
						// 	!this.state.selectedTag ?
						// 		<Grid fluid>
						// 			<CasesDistribution
						// 				root={data.places}
						// 				onGetChildren={this.getChildren} />
						// 		</Grid> : null
						// }

	}
}

Cases.propTypes = {
	route: React.PropTypes.object,
	scope: React.PropTypes.oneOf(['WORKSPACE', 'ADMINUNIT']).isRequired
};
