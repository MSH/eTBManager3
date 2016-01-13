
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { RouteView, router } from '../../components/router';
import { Fluidbar, Sidebar, WaitIcon } from '../../components/index';

import { AdmUnits } from './admunits';
import { Sources } from './sources';
import Units from './units';
import { Substances } from './substances';
import Products from './products';
import Regimens from './regimens';
import Tags from './tags';
import AgeRanges from './ageranges';
import { hasPerm } from '../session';

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
		title: __('admin.units'),
		perm: 'UNITS',
		icon: 'hospital-o',
        path: '/units',
        view: Units
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
        path: '/substances',
        view: Substances
	},
	{
		title: __('admin.products'),
		perm: 'PRODUCTS',
		icon: 'cube',
        path: '/products',
        view: Products
	},
	{
		title: __('admin.regimens'),
		perm: 'REGIMENS',
		icon: 'medkit',
        path: '/regimens',
        view: Regimens
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
        path: '/ageranges',
        view: AgeRanges
	},
	{
		title: __('admin.tags'),
		perm: 'TAGS',
		icon: 'tags',
        path: '/tags',
        view: Tags
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

		// remove items with no permission
		const menu = items.filter(item => item.perm && !hasPerm(item.perm) ? null : item);


		return (
			<div>
				<Fluidbar>
					<h3>{__('admin') + ' - ' + __('admin.tables')}</h3>
				</Fluidbar>
				<Grid fluid>
					<Row>
						<Col sm={3}>
							<Sidebar items={menu} selected={selItem} onSelect={this.menuSelect} />
						</Col>
						<Col sm={9}>
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
