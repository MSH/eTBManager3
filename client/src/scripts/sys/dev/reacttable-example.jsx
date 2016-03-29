
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Card, ReactTable, ReactGrid, Profile } from '../../components/index';

import { generateName } from '../mock-data';

/**
 * Initial page that declare all routes of the module
 */
export default class ReacttableExample extends React.Component {

	constructor(props) {
		super(props);

		this.state = { };
	}

	componentWillMount() {
		const lst = [];
		for (var i = 0; i < 15; i++) {
			const res = generateName();
			lst.push({
				name: res.name,
				gender: res.gender,
				status: 'Status of ' + res.name,
				quantity: Math.random() * 1000 + 1000
			});
		}
		this.setState({ values: lst });
	}

	expandRender(item) {
		return (
			<div>
				<dl className="dl-horizontal">
					<dt>{'Patient: '}</dt>
					<dd>{item.name}</dd>
					<dt>{'Status: '}</dt>
					<dd>{item.status}</dd>
					<dt>{'Quantity: '}</dt>
					<dd>{item.quantity.toLocaleString('en', { maximumFractionDigits: 2 })}</dd>
				</dl>
			</div>);
	}

	gridCellRender(item) {
		return <Profile size="small" title={item.name} type="user" />;
	}

	render() {

		// the columns of the table
		const columns = [
			{
				title: 'Patient',
				content: item => <Profile size="small" title={item.name} type="user" />,
				size: { sm: 6 }
			},
			{
				title: 'Status (center alignment)',
				content: 'status',
				size: { sm: 3 },
				align: 'center'
			},
			{
				title: 'Quantity',
				content: item => item.quantity.toLocaleString('en', { maximumFractionDigits: 2 }),
				size: { sm: 3 },
				align: 'right'
			}
		];

		return (
			<div>
				<Card title="Reactive table">
					<Row>
						<Col md={12}>
							<ReactTable columns={columns}
								values={this.state.values}
								onExpandRender={this.expandRender} />
						</Col>
					</Row>
				</Card>

				<Card title="Reactive grid">
					<ReactGrid values={this.state.values}
						onCollapseRender={this.gridCellRender}
						onExpandRender={this.expandRender} />
				</Card>
			</div>
			);
	}
}
