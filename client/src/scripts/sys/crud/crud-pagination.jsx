
import React from 'react';
import { Pagination } from 'react-bootstrap';

export default class CrudPagination extends React.Component {

	constructor(props) {
		super(props);
		this.changePage = this.changePage.bind(this);

		const self = this;
		this.props.controller.on(evt => {
			if (evt === 'page' || evt === 'fetching-list') {
				self.forceUpdate();
			}
		});
	}

	changePage(evt, data) {
		this.props.controller.gotoPage(data.eventKey - 1);
	}

	counterRender(controller) {
		if (!this.props.showCounter || !controller.getCount()) {
			return null;
		}

		return (
			<div className="pagination pull-right text-muted">
				<b>{controller.getPageIni() + 1}</b>{' - '}<b>{controller.getPageEnd() + 1}</b>{' of '}
				<b>{controller.getCount()}</b>{' records found'}
			</div>
			);
	}

	render() {
		const controller = this.props.controller;

		if (controller.isFetching()) {
			return null;
		}

		return (
			<div>
				{
					this.counterRender(controller)
				}
				<Pagination activePage={controller.getPage() + 1}
					prev next first last ellipsis
					items={controller.getPageCount()}
					maxButtons={5}
					boundaryLinks
					onSelect={this.changePage} />
			</div>
			);
	}
}

CrudPagination.propTypes = {
	controller: React.PropTypes.object.isRequired,
	showCounter: React.PropTypes.bool
};
