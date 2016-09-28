import React from 'react';
import { Card, ReactTable, WaitIcon, Fa } from '../../../components';
import { server } from '../../../commons/server';

/**
 * Display the list of reports available for the case module of the
 * specified scope
 */
export default class CasesReports extends React.Component {

    constructor(props) {
        super(props);
        this.repClick = this.repClick.bind(this);
    }

    componentWillMount() {
        const self = this;

        const req = {
            scope: this.props.scope,
            id: this.props.scopeId
        };

        // get the list of reports from the server
        server.post('/api/cases/reports', req)
        .then(res => self.setState({ values: res }));

        this.setState({ values: null });
    }

    repClick(item) {
        const route = this.props.route;
        const path = route.path;
        const pg = route.data.path;
        const dest = path.slice(0, path.length - pg.length) + '/report?rep=' + item.id +
            '&id=' + route.queryParam('id');
        location.hash = '#' + dest;
    }

    render() {
        const values = this.state.values;
        if (!values) {
            return <WaitIcon />;
        }

        return (
            <Card title={__('reports')}>
                <ReactTable
                    columns={[
                        {
                            size: { sm: 12 },
                            content: item => (
                                <div>
                                    <Fa icon="line-chart" size={1} className="cmd-icon"/>
                                    {item.name}
                                </div>
                                )
                        }
                    ]}
                    values={values}
                    onClick={this.repClick}
                    />
            </Card>
        );
    }
}

CasesReports.propTypes = {
    scope: React.PropTypes.string.isRequired,
    scopeId: React.PropTypes.string,
    route: React.PropTypes.object
};
