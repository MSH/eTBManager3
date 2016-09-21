
import React from 'react';
import PageContent from '../page-content';

import AdmUnits from './tables/admunits';
import Sources from './tables/sources';
import Units from './tables/units';
import Substances from './tables/substances';
import Products from './tables/products';
import Regimens from './tables/regimens';
import Tags from './tables/tags';
import UserProfiles from './tables/user-profiles';
import UsersWs from './tables/users-ws';
import AgeRanges from './tables/ageranges';

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
        title: __('admin.tags'),
        perm: 'TAGS',
        icon: 'tags',
        path: '/tags',
        view: Tags
    },
    {
        title: __('admin.ageranges'),
        perm: 'AGERANGES',
        icon: 'signal',
        path: '/ageranges',
        view: AgeRanges
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
 * The page controller of the public module
 */
export default class Tables extends React.Component {

    render() {
        return (
            <PageContent route={this.props.route}
                menu={menu}
                title={__('admin') + ' - ' + __('admin.tables')}
                path="/sys/admin/tables" />
            );
    }
}

Tables.propTypes = {
    app: React.PropTypes.object,
    route: React.PropTypes.object
};
