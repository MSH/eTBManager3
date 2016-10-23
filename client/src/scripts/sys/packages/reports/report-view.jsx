import React from 'react';
import { Card, WaitIcon } from '../../../components';
import Report from './report';
import IndicatorView from './indicator-view';
import { arrangeGrid } from '../../../commons/grid-utils';

import './report.less';


/**
 * Display a report with its indicators
 */
export default class ReportView extends React.Component {

    componentWillMount() {
        const id = this.props.route.queryParam('rep');
        const self = this;

        Report.load(id, this.props.scope, this.props.scopeId)
            .then(res => self.setState({ report: res }));

        this.setState({ report: null });
    }

    render() {
        const rep = this.state.report;

        if (!rep) {
            return <WaitIcon />;
        }

        const lst = rep.indicators.map((ind, index) => ({
            size: { sm: ind.schema.size },
            content: <IndicatorView key={index} indicator={ind} />
        }));

        const inds = arrangeGrid(lst);

        return (
            <div>
                <Card title={rep.schema.title} />
                {
                    inds
                }
            </div>
            );
    }
}

ReportView.propTypes = {
    scope: React.PropTypes.oneOf(['WORKSPACE', 'ADMINUNIT', 'UNIT']).isRequired,
    scopeId: React.PropTypes.string,
    route: React.PropTypes.object
};
