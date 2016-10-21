import React from 'react';
import { Button } from 'react-bootstrap';
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
        server.post('/api/cases/report/query', req)
        .then(res => self.setState({ values: res.result }));

        this.setState({ values: null });
    }

    repClick(item) {
        const route = this.props.route;
        const path = route.path;
        const pg = route.data.path;
        const id = route.queryParam('id');

        const dest = path.slice(0, path.length - pg.length) + '/report?rep=' + item.id +
            (id ? '&id=' + id : '');
        location.hash = '#' + dest;
    }

    render() {
        const values = this.state.values;
        if (!values) {
            return <WaitIcon />;
        }

        const params = this.props.scopeId ? '?id=' + this.props.scopeId : '';

        const newPath = '#' + this.props.route.parentPath + '/reportedt' + params;

        const header = (
            <div className="class-header">
                <div className="pull-right">
                    <Button bsStyle="success" bsSize="small" href={newPath}>{__('reports.new')}</Button>
                </div>
                <h4>{__('reports')}</h4>
            </div>
        );

        return (
            <Card header={header}>
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
