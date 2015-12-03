
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { RouteView, router } from '../../components/router';
import { Profile, Card, Fluidbar, Sidebar, WaitIcon } from '../../components/index';

import { initau } from './admunits';


const items = [
	{
		caption: __('admin.adminunits'),
		perm: 'ADMINUNITS',
		icon: 'sitemap',
        path: '/admunits',
        viewResolver: initau
	},
	{
		caption: __('admin.tbunits'),
		perm: 'UNITS',
		icon: 'hospital-o',
        path: '/units'
	},
	{
		caption: __('admin.labs'),
		perm: 'UNITS',
		icon: 'building',
        path: '/labs'
	},
	{
		caption: __('admin.sources'),
		perm: 'SOURCES',
		icon: 'dropbox',
        path: '/sources'
	},
	{
		caption: __('admin.substances'),
		perm: 'SUBSTANCES',
		icon: 'h-square',
        path: '/substances'
	},
	{
		caption: __('admin.products'),
		perm: 'PRODUCTS',
		icon: 'cube',
        path: '/products'
	},
	{
		caption: __('admin.regimens'),
		perm: 'REGIMENS',
		icon: 'medkit',
        path: '/regimens'
	},
	{
		separator: true
	},
	{
		caption: __('admin.weeklyfreq'),
		perm: 'WEEKFREQ',
		icon: 'calendar',
        path: '/weeklyfreq'
	},
	{
		caption: __('admin.ageranges'),
		perm: 'AGERANGES',
		icon: 'tasks',
        path: '/ageranges'
	},
	{
		caption: __('admin.tags'),
		perm: 'TAGS',
		icon: 'tags',
        path: '/tags'
	},
	{
		separator: true
	},
	{
		caption: __('admin.users'),
		perm: 'USERS',
		icon: 'user',
        path: '/users'
	},
	{
		caption: __('admin.profiles'),
		perm: 'PROFILES',
		icon: 'group',
        path: '/groups'
	}
];

/**
 * Create the route list from the list of items
 * @type {[type]}
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
		const selItem = selroute !== null ? selroute.data : null;

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
							<RouteView routes={routes} loadingIcon={<WaitIcon />} />
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
