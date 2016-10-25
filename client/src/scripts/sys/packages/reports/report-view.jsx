import React from 'react';
import { Button } from 'react-bootstrap';
import { Card, WaitIcon } from '../../../components';
import { app } from '../../../core/app';
import Report from './report';
import IndicatorView from './indicator-view';
import { arrangeGrid } from '../../../commons/grid-utils';

import './report.less';


/**
 * Display a report with its indicators
 */
export default class ReportView extends React.Component {

    constructor(props) {
        super(props);
        this.delete = this.delete.bind(this);
    }

    componentWillMount() {
        const id = this.props.route.queryParam('rep');
        const self = this;

        Report.load(id, this.props.scope, this.props.scopeId)
            .then(res => self.setState({ report: res }));

        this.setState({ report: null });
    }

    /**
     * Called when user clicks on the delete button to delete a report
     */
    delete() {
        const self = this;

        app.messageDlg({
            title: __('action.delete'),
            message: __('form.confirm_remove'),
            type: 'YesNo'
        })
        .then(res => {
            if (res === 'yes') {
                // delete the report
                self.state.report.delete()
                .then(() => {
                    location.hash = self.props.route.parentPath + '/reports';
                });
            }
        });
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

        const header = (
            <div>
                <div className="pull-right">
                <Button href={'#' + this.props.route.parentPath + '/reportedt?id=' + rep.id} bsSize="sm">{__('action.edit')}</Button>
                <Button bsSize="sm" onClick={this.delete}>{__('action.delete')}</Button>
                </div>
                <div className="title">
                    {rep.schema.title}
                </div>
            </div>
        );

        return (
            <div>
                <Card header={header} />
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
