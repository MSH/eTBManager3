import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import CasesSideView from '../cases/cases-side-view';
import { RouteView } from '../../../components/router';

import CasesUnit from './cases-unit';
import AdvancedSearch from '../cases/advanced-search';
import TagCasesList from '../cases/tag-cases-list';
import SummaryList from '../commons/summary-list';
import CasesReports from '../commons/cases-reports';
import ReportView from '../cases/report-view';


const views = [
    {
        title: 'Active cases',
        icon: 'clone',
        path: '/active',
        default: true,
        view: CasesUnit,
        sideView: true
    },
    {
        title: 'Advanced search',
        icon: 'search',
        path: '/search',
        view: AdvancedSearch,
        sideView: true
    },
    {
        title: __('reports'),
        icon: 'table',
        path: '/reports',
        view: CasesReports,
        sideView: true
    },
    {
        title: __('admin.tags'),
        path: '/tag',
        view: TagCasesList
    },
    {
        title: __('global.summary'),
        path: '/summary',
        view: SummaryList
    },
    {
        title: __('reports'),
        path: '/report',
        view: ReportView
    }
];

export default class Cases extends React.Component {

    constructor(props) {
        super(props);
        this.state = { };
        this.selTag = this.selTag.bind(this);
    }


    selTag(tagId) {
        return () => this.setState({ selectedTag: tagId });
    }

    viewProps(view) {
        switch (view.path) {
            case '/active':
                return { cases: this.state.cases };
            case '/search':
                return { unitId: this.props.route.queryParam('id') };
            default: return { tag: this.state.selectedTag };
        }
    }

    render() {
        const unitId = this.props.route.queryParam('id');

        const routes = RouteView.createRoutes(views);
        const sideViews = views.filter(v => v.sideView);

        return (
            <Grid fluid>
            <Row className="mtop">
                <Col sm={3}>
                    <CasesSideView route={this.props.route}
                        scope="UNIT"
                        views={sideViews}
                        scopeId={unitId}/>
                </Col>
                <Col sm={9}>
                    <RouteView routes={routes} viewProps={{ scope: 'UNIT', scopeId: unitId }} />
                </Col>
            </Row>
            </Grid>
        );
    }


}

Cases.propTypes = {
    route: React.PropTypes.object
};
