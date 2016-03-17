
import React from 'react';
import { Row, Col, Button, Badge } from 'react-bootstrap';
import { Card, Grid, CollapseRow, WaitIcon, ReactTable } from '../../../components/index';
import { server } from '../../../commons/server';
import Form from '../../../forms/form';
import { app } from '../../../core/app';

const fschema = {
			layout: [
				{
					property: 'iniDate',
					required: true,
					type: 'date',
					label: __('Period.iniDate'),
					size: { md: 3 },
					defaultValue: new Date()
				},
				{
					property: 'endDate',
					required: false,
					type: 'date',
					label: __('Period.endDate'),
					size: { md: 3 }
				},
				{
					property: 'action',
					required: false,
					type: 'select',
					label: __('form.action'),
					options: app.getState().app.lists.CommandAction,
					size: { md: 3 }
				},
				{
					property: 'userId',
					required: false,
					type: 'select',
					label: __('User'),
					options: 'users',
					size: { md: 3 }
				},
				{
					property: 'type',
					required: false,
					type: 'select',
					label: 'TODOMS - Type', // __('admin.reports.cmdhistory.type'),
					options: 'users', // TODOMS
					size: { md: 4 }
				},
				{
					property: 'adminUnitId',
					type: 'adminUnit',
					label: __('AdministrativeUnit'),
					size: { md: 4 }
				},
				{
					property: 'searchKey',
					required: false,
					type: 'string',
					max: 50,
					label: __('form.searchkey'),
					size: { sm: 4 }
				}
			]
		};

/**
 * The page controller of the public module
 */
export default class CommandHistory extends React.Component {

	constructor(props) {
		super(props);
		this.refresh = this.refresh.bind(this);
		this.onChangeDoc = this.onChangeDoc.bind(this);
		this.state = { doc: {} };
	}

	componentWillMount() {
		this.refreshTodayRep();
	}

	onChangeDoc() {
		this.forceUpdate();
	}

	refresh() {
		const self = this;

		const errors = self.refs.form.validate();
		this.setState({ errors: errors });
		if (errors) {
			return;
		}

		const query = {
			iniDate: this.state.doc.iniDate,
			endDate: this.state.doc.endDate ? this.state.doc.endDate : null,
			action: this.state.doc.action ? this.state.doc.action : null,
			userId: this.state.doc.userId ? this.state.doc.userId : null,
			type: this.state.doc.type ? this.state.doc.type.toString() : null, // TODOMS
			adminUnitId: this.state.doc.adminUnitId ? this.state.doc.adminUnitId : null,
			searchKey: this.state.doc.searchKey ? this.state.doc.searchKey : null
		};

		server.post('/api/admin/rep/cmdhistory', query)
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result });
		});
	}

	refreshTodayRep() {
		const self = this;
		server.post('/api/admin/rep/todaycmdhistory')
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result });
		});
	}

	parseDetails(item) {
		const collapsedValue = (<div className="text-small">
									<dl>
										<Col sm={4}>
											<dt>{__('User.login') + ':'}</dt>
											<dd>{item.userLogin}</dd>
										</Col>
										<Col sm={4}>
											<dt>{__('UserLogin.logoutDate') + ':'}</dt>
											<dd>{item.logoutDate}</dd>
										</Col>
										<Col sm={4}>
											<dt>{__('UserLogin.ipAddress') + ':'}</dt>
											<dd>{item.ipAddress}</dd>
										</Col>
									</dl>
									<Col md={12} className="text-small">{item.Application}</Col>
								</div>);
		return (collapsedValue);
	}

	headerRender(count) {
		const countHTML = <Badge className="tbl-counter">{count}</Badge>;

		// create the header of the card
		return (
			<Row>
				<Col md={12}>
					<h4>{'Command History'} {count === 0 ? '' : countHTML}</h4>
				</Col>
			</Row>
			);
	}

	render() {

		const header = this.headerRender(!this.state || !this.state.values ? 0 : this.state.values.count);


		const tschema = [
		{
			title: 'Type',
			content: 'type',
			size: { sm: 2 }
		},
		{
			title: 'Action',
			content: 'action',
			size: { sm: 1 },
			align: 'center'
		},
		{
			title: 'ExecDate',
			content: 'execDate',
			size: { sm: 1 },
			align: 'center'
		},
		{
			title: 'entityName',
			content: 'entityName',
			size: { sm: 1 },
			align: 'center'
		},
		{
			title: 'userName',
			content: 'userName',
			size: { sm: 1 },
			align: 'center'
		},
		{
			title: 'unitName',
			content: 'unitName',
			size: { sm: 1 },
			align: 'center'
		},
		{
			title: 'adminUnitName',
			content: 'adminUnitName',
			size: { sm: 1 },
			align: 'center'
		},
		{
			title: 'data',
			content: 'data',
			size: { sm: 4 },
			align: 'center'
		}
		];

		return (
			<div>
			<Card header={header}>
				<Row>
					<Col md={12}>
						<Form ref="form"
							schema={fschema}
							doc={this.state.doc}
							onChange={this.onChangeDoc}
							errors={this.state.errors} />
					</Col>
					<Col md={12}><Button onClick={this.refresh} bsStyle="primary">{'Update'}</Button></Col>
				</Row>

				{!this.state || !this.state.values ? <WaitIcon type="card" /> :
				<div>
					<Card>
						<Row>
							<Col md={12}>
								<ReactTable columns={tschema}
									values={this.state.values.list} />
							</Col>
						</Row>
					</Card>
				</div>}

			</Card>
			</div>
		);
	}
}

CommandHistory.propTypes = {
	route: React.PropTypes.object
};
