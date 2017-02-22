import React from 'react';
import FrontPage from '../front-page';
import { server } from '../../commons/server';
import { WaitIcon } from '../../components';
import SessionUtils from '../session-utils';

import General from './general';
import Cases from '../packages/cases/cases';

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
    }
    /*
    {
        title: __('meds.inventory'),
        path: '/inventory',
        view: UnderConstruction
    }
    */
];

export default class AdminUnit extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    componentWillMount() {
        const id = this.props.route.queryParam('id');
        this.fetchData(id);
    }

    componentWillReceiveProps(nextProps) {
        // check if page must be updated
        const id = nextProps.route.queryParam('id');
        const oldId = this.state.data ? this.state.data.id : null;
        if (id !== oldId) {
            this.fetchData(id);
        }
    }

    fetchData(id) {
        const self = this;
        self.setState({ data: null });

        // get data of the admin unit
        server.get('/api/tbl/adminunit/' + id)
        .then(res => {
            self.setState({ data: res });
        });
    }

    render() {
        const au = this.state.data;

        if (!au) {
            return <WaitIcon />;
        }

        return (
            <FrontPage
                title={au.name}
                subtitle={SessionUtils.adminUnitLink(au, true, false)}
                type="place"
                views={views}
                viewProps={{ scope: 'ADMINUNIT', scopeId: au.id }}
                route={this.props.route}
            />
        );
    }
}

AdminUnit.propTypes = {
    route: React.PropTypes.object
};
