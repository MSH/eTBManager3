import React from 'react';
import FrontPage from '../front-page';
import { app } from '../../core/app';

import General from './general';
import Sync from './sync';
import Cases from '../packages/cases/cases';
// import Inventory from '../packages/inventory/inventory';


const views = [
    {
        title: __('general'),
        path: '/general',
        view: General,
        default: true
    },
    {
        title: __('cases'),
        path: '/cases',
        view: Cases
    },
    {
        title: 'Synchronize',
        path: '/sync',
        view: Sync
    }
    /*
    {
        title: __('meds.inventory'),
        path: '/inventory',
        view: Inventory
    }
    */
];

export default class Workspace extends React.Component {

    render() {
        const session = app.getState().session;

        return (
            <FrontPage
                title={session.workspaceName}
                type="ws"
                views={views}
                viewProps={{ scope: 'WORKSPACE' }}
                route={this.props.route}
            />
            );
    }
}

Workspace.propTypes = {
    route: React.PropTypes.object
};
