
import React from 'react';
import { Table, Button, Row, Col, Input } from 'react-bootstrap';
import { Card, WaitIcon } from '../../components/index';

import './tableview.less';

/**
 * Table view for standard CRUD operations
 */
export default class TableView extends React.Component {

	fetchData(crud) {
		const self = this;
		crud.query({ rootUnits: true })
		.then(result => self.setState({ result: result }));

	}

	render() {
		const crud = this.props.crud;
		const title = this.props.title;
		const res = this.state ? this.state.result : null;

		let tbl;

		if (!res) {
			this.fetchData(crud);
			tbl = <tr><td><WaitIcon /></td></tr>;
		}
		else {
			tbl = res.list.map(item => (
					<tr key={item.id}>
						<td>{item.name}</td>
					</tr>
				));
		}

		const btnSearch = {
			width: '120px'
		};

		return (
			<div className="card">
				<div className="card-header">
					<Row>
						<Col xs={12} md={7}>
							<h4>{title}</h4>
						</Col>
						<Col xs={8} sm={6} md={3}>
							<div className="input-group">
								<input type="text" placeholder="Search" className="input-sm form-control" />
								<span className="input-group-btn">
                                	<button type="button" className="btn btn-sm btn-default"> Go!</button>
                                </span>
                            </div>
						</Col>
						<Col xs={4} sm={6} md={2}>
							<div className="pull-right">
								<Button bsStyle="default" bsSize="small"><i className="fa fa-plus"/>{'New'}</Button>
							</div>
						</Col>
					</Row>
				</div>
				<div className="card-content">
					<Table responsive>
						<thead>
							<tr>
								<th>
									{'Name'}
								</th>
							</tr>
						</thead>
						<tbody>
							{tbl}
						</tbody>
					</Table>
				</div>
			</div>
			);
	}
}

TableView.propTypes = {
	crud: React.PropTypes.object,
	title: React.PropTypes.string,
	paging: React.PropTypes.bool,
	search: React.PropTypes.bool,
	filterView: React.PropTypes.func,
	editForm: React.PropTypes.func
};
