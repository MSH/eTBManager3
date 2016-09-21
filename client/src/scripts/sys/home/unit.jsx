
import React from 'react';
import FrontPage from './commons/front-page';
import { server } from '../../commons/server';
import { WaitIcon } from '../../components';
import SessionUtils from '../session-utils';


import General from './unit/general';
import Cases from './unit/cases';
import Inventory from './unit/inventory';


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
        title: __('meds.inventory'),
        path: '/inventory',
        view: Inventory
    }
];


export default class Unit extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    componentWillMount() {
        const id = this.props.route.queryParam('id');
        this.fetchData(id);
    }

    fetchData(id) {
        const self = this;

        // get data of the unit
        server.get('/api/tbl/unit/' + id)
        .then(res => self.setState({ data: res }));
    }

    render() {
        const unit = this.state.data;

        if (!unit) {
            return <WaitIcon />;
        }

        return (
            <FrontPage
                title={unit.name}
                subtitle={SessionUtils.adminUnitLink(unit.address.adminUnit, true, true)}
                type={unit.type === 'TBUNIT' ? 'tbunit' : 'lab'}
                views={views}
                route={this.props.route}
                />
            );
    }
}

Unit.propTypes = {
    route: React.PropTypes.object
};
