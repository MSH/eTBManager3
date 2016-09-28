import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import CasesSideView from '../cases/cases-side-view';
import { RouteView } from '../../../components/router';

import CasesUnit from './cases-unit';


const views = [
    {
        title: __('cases.open'),
        icon: 'clone',
        path: '/active',
        default: true,
        view: CasesUnit,
        sideView: true
    }
];


/**
 * Display the cases tab content of the unit page
 */
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

        const routes = CasesSideView.createRoutes(views);
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
