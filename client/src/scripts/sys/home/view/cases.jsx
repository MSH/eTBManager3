import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { RouteView } from '../../../components/router';
import CasesSideView from '../cases/cases-side-view';

import CasesDistribution from '../commons/cases-distribution';

/**
 * The specific views of this page
 */
const views = [
    {
        title: __('cases.open'),
        icon: 'clone',
        path: '/active',
        default: true,
        view: CasesDistribution,
        sideView: true
    }
];

/**
 * Display the main case tab of the administrative unit or workspace view
 */
export default class Cases extends React.Component {

    render() {
        const scope = this.props.scope;

        const adminUnitId = scope === 'ADMINUNIT' ? this.props.route.queryParam('id') : null;

        const routes = CasesSideView.createRoutes(views);

        const sideViews = views.filter(v => v.sideView);

        return (
            <div className="mtop-2x">
            <Grid fluid>
                <Row>
                    <Col sm={3}>
                        <CasesSideView
                            scope={scope}
                            views={sideViews}
                            scopeId={adminUnitId}
                            route={this.props.route}
                            />
                    </Col>
                    <Col sm={9}>
                        <RouteView routes={routes}
                            viewProps={{ scope: this.props.scope, scopeId: adminUnitId }} />
                    </Col>
                </Row>
            </Grid>
            </div>
            );
    }
}

Cases.propTypes = {
    route: React.PropTypes.object,
    scope: React.PropTypes.oneOf(['WORKSPACE', 'ADMINUNIT']).isRequired,
    scopeId: React.PropTypes.string
};
