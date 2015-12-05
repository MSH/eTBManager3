
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { RouteView, router } from '../../components/router';
import { Fluidbar, Sidebar, WaitIcon } from '../../components/index';

import { AdmUnits } from './admunits';
import { Sources } from './sources';
import { Tbunits } from './tbunits';

/**
 * List of all items displayed in the left side box
 */
const items = [
	{
		title: __('admin.adminunits'),
		perm: 'ADMINUNITS',
		icon: 'sitemap',
        path: '/admunits',
        view: AdmUnits
	},
	{
		title: __('admin.tbunits'),
		perm: 'UNITS',
		icon: 'hospital-o',
        path: '/units',
        view: Tbunits
	},
	{
		title: __('admin.labs'),
		perm: 'UNITS',
		icon: 'building',
        path: '/labs'
	},
	{
		title: __('admin.sources'),
		perm: 'SOURCES',
		icon: 'dropbox',
        path: '/sources',
        view: Sources
	},
	{
		title: __('admin.substances'),
		perm: 'SUBSTANCES',
		icon: 'h-square',
        path: '/substances'
	},
	{
		title: __('admin.products'),
		perm: 'PRODUCTS',
		icon: 'cube',
        path: '/products'
	},
	{
		title: __('admin.regimens'),
		perm: 'REGIMENS',
		icon: 'medkit',
        path: '/regimens'
	},
	{
		separator: true
	},
	{
		title: __('admin.weeklyfreq'),
		perm: 'WEEKFREQ',
		icon: 'calendar',
        path: '/weeklyfreq'
	},
	{
		title: __('admin.ageranges'),
		perm: 'AGERANGES',
		icon: 'tasks',
        path: '/ageranges'
	},
	{
		title: __('admin.tags'),
		perm: 'TAGS',
		icon: 'tags',
        path: '/tags'
	},
	{
		separator: true
	},
	{
		title: __('admin.users'),
		perm: 'USERS',
		icon: 'user',
        path: '/users'
	},
	{
		title: __('admin.profiles'),
		perm: 'PROFILES',
		icon: 'group',
        path: '/groups'
	}
];

/**
 * Create the route list from the list of items
 */
const routes = RouteView.createRoutes(items.filter(item => !item.separator));

/**
 * The page controller of the public module
 */
export default class Tables extends React.Component {

	menuSelect(data) {
		router.goto('/sys/admin/tables' + data.path);
	}

	render() {
		// get information about the route being rendered
		const route = this.props.route;
		// get forward path
		const forpath = route.forpath;
		// get route to be rendered
		const selroute = routes.find(forpath);

		// calc selected item
		const selItem = selroute ? selroute.data : null;

		return (
			<div>
				<Fluidbar>
					<h3>{__('admin') + ' - ' + __('admin.tables')}</h3>
				</Fluidbar>
				<Grid fluid>
					<Row>
						<Col md={3}>
							<Sidebar items={items} selected={selItem} onSelect={this.menuSelect} />
						</Col>
						<Col md={9}>
							<div className="mtop-2x">
								<RouteView routes={routes} loadingIcon={<WaitIcon />} />
							</div>
						</Col>
					</Row>
				</Grid>
			</div>

			);
	}
}

Tables.propTypes = {
	app: React.PropTypes.object,
	route: React.PropTypes.object
};
