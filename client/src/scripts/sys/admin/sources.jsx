
import React from 'react';
import TableView from './tableview';
import CRUD from '../../commons/crud';

const crud = new CRUD('source');

/**
 * The page controller of the public module
 */
export class Sources extends React.Component {

	render() {
		const data = this.props.route.data;
		return <TableView title={data.title} crud={crud} pagging search />;
	}
}

Sources.propTypes = {
	route: React.PropTypes.object
};
