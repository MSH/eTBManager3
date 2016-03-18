
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Card, ReactTable, Profile } from '../../components/index';

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
		setTimeout(() => {
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
		}, 800);
	}

	rowClick(item) {
		alert(item.name);
	}

	collapseRender(item) {
		return (<div>
					<hr/>
						<dl className="text-small dl-horizontal text-muted">
							<dt>{__('Patient') + ':'}</dt>
							<dd>{item.name}</dd>
							<dt>{__('Status') + ':'}</dt>
							<dd>{item.status}</dd>
							<dt>{__('Quantity') + ':'}</dt>
							<dd>{item.quantity.toLocaleString('en', { maximumFractionDigits: 2 })}</dd>
						</dl>
					<hr/>
				</div>);
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
								onClick={this.rowClick}
								collapseRender={this.collapseRender} />
						</Col>
					</Row>
				</Card>
			</div>
			);
	}
}
