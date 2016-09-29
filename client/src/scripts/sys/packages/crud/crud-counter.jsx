import React from 'react';
import { Alert, Badge } from 'react-bootstrap';
import controlWrapper from './crud-control-wrapper';

/**
 * Simple crud component to display the number of records in the controller.
 * It also supports pagination
 */
class CrudCounter extends React.Component {

    eventHandler(evt) {
        if (evt === 'page' || evt === 'fetching-list' || evt === 'list') {
            this.forceUpdate();
        }
    }

    render() {
        const controller = this.props.controller;

        if (!controller.getList()) {
            return null;
        }

        if (this.props.counterOnly) {
            return <Badge className="tbl-counter">{controller.getCount() > 0 ? controller.getCount() : '-'}</Badge>;
        }

        // any result found ?
        if (!controller.getCount()) {
            return <Alert bsStyle="warning">{__('form.norecordfound')}</Alert>;
        }

        const msg = ' ' + __('form.resultlist');
        const className = this.props.className ? this.props.className : 'text-muted';

        // is paging enabled ?
        if (controller.isPaging() && controller.getCount() > controller.options.pageSize) {
            return (
                <div className={className}>
                    <b>{controller.getPageIni() + 1}</b>{' - '}<b>{controller.getPageEnd() + 1}</b>{' of '}
                    <b>{controller.getCount()}</b>{msg}
                </div>
                );
        }

        // render simple counter
        return (
                <span className={className}>
                    <b>{controller.getCount()}</b>{msg}
                </span>
            );
    }
}

CrudCounter.propTypes = {
    controller: React.PropTypes.object.isRequired,
    counterOnly: React.PropTypes.bool,
    className: React.PropTypes.string
};

export default controlWrapper(CrudCounter);
