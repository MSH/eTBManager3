
import React from 'react';
import PageContent from './page-content';

import { AdmUnits } from './admunits';
import { Sources } from './sources';
import Units from './units';
import { Substances } from './substances';
import Products from './products';
import Regimens from './regimens';
import Tags from './tags';
import AgeRanges from './ageranges';
import UserProfiles from './user-profiles';
import UsersWs from './users-ws';

/**
 * List of all items displayed in the left side box
 */
const menu = [
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
        path: '/users',
        view: UsersWs
	},
	{
		title: __('admin.profiles'),
		perm: 'PROFILES',
		icon: 'group',
        path: '/groups',
        view: UserProfiles
	}
];

/**
 * Create the route list from the list of items
 */
// const routes = RouteView.createRoutes(items.filter(item => !item.separator));

/**
 * The page controller of the public module
 */
export default class Tables extends React.Component {

	render() {
		return (
			<PageContent route={this.props.route}
				menu={menu}
				title={__('admin.tables')}
				path="/sys/admin/tables" />
			);
	}
}

Tables.propTypes = {
	app: React.PropTypes.object,
	route: React.PropTypes.object
};
