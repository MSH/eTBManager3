
import React from 'react';
import { Pagination } from 'react-bootstrap';
import controlWrapper from './crud-control-wrapper';


class CrudPagination extends React.Component {

	constructor(props) {
		super(props);
		this.changePage = this.changePage.bind(this);
	}

	eventHandler(evt) {
		if (evt === 'page' || evt === 'fetching-list' || evt === 'list') {
			this.forceUpdate();
		}
	}

	changePage(data) {
		this.props.controller.gotoPage(data - 1);
	}


	render() {
		const controller = this.props.controller;

		if (!controller.isPaging() || controller.getCount() < controller.options.pageSize) {
			return null;
		}

		if (controller.isFetching()) {
			return null;
		}

		return (
			<Pagination activePage={controller.getPage() + 1}
				prev next first last ellipsis
				items={controller.getPageCount()}
				maxButtons={5}
				boundaryLinks
				onSelect={this.changePage} className={this.props.className}/>
			);
	}
}

CrudPagination.propTypes = {
	controller: React.PropTypes.object.isRequired,
	className: React.PropTypes.string
};

export default controlWrapper(CrudPagination);

