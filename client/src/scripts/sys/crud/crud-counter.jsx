import React from 'react';
import { Alert } from 'react-bootstrap';
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

		// any result found ?
		if (!controller.getCount()) {
			return <Alert bsStyle="warning">{__('form.norecordfound')}</Alert>;
		}

		// is paging enabled ?
		if (controller.isPaging()) {
			return (
				<div className="text-muted">
					<b>{controller.getPageIni() + 1}</b>{' - '}<b>{controller.getPageEnd() + 1}</b>{' of '}
					<b>{controller.getCount()}</b>{' records found'}
				</div>
				);
		}

		// render simple counter
		return (
				<span className="text-muted">
					<b>{controller.getCount()}</b>{' records found'}
				</span>
			);
	}
}

CrudCounter.propTypes = {
	controller: React.PropTypes.object.isRequired
};

export default controlWrapper(CrudCounter);
