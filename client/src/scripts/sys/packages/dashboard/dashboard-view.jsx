import React from 'react';
import { WaitIcon } from '../../../components';
import { server } from '../../../commons/server';
import Indicator from '../reports/indicator';
import IndicatorView from '../reports/indicator-view';
import { arrangeGrid } from '../../../commons/grid-utils';


export default class DashboardView extends React.Component {
    componentWillMount() {
        this.fetch(this.props.scope, this.props.scopeId);
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.scopeId !== nextProps.scopeId) {
            this.fetch(nextProps.scope, nextProps.scopeId);
        }
    }

    /**
     * Fetch the indicator from the server
     */
    fetch(scope, scopeId) {
        const self = this;

        server.post('/api/dashboard/generate', {
            scope: scope,
            scopeId: scopeId
        })
        .then(res => {
            const data = res.result;
            const caseIndicators = data.caseIndicators.map(ind => new Indicator(ind.schema, ind.data));
            self.setState({ caseIndicators: caseIndicators });
        });

        // trigger the wait icon
        this.setState({ caseIndicators: null });
    }

    render() {
        const caseIndicators = this.state.caseIndicators;

        if (!caseIndicators) {
            return <WaitIcon />;
        }

        const lst = caseIndicators.map((ind, index) => ({
            size: { sm: ind.schema.size },
            content: <IndicatorView key={index} indicator={ind} />
        }));

        const inds = arrangeGrid(lst);

        return (
            <div>
                <h2>{__('Dashboard')}</h2>
                {
                    inds
                }
            </div>
            );
    }
}

DashboardView.propTypes = {
    scope: React.PropTypes.oneOf(['WORKSPACE', 'ADMINUNIT', 'UNIT']).isRequired,
    scopeId: React.PropTypes.string
};
