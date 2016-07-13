
import React from 'react';
import { Row, Col, Button, Badge, Alert } from 'react-bootstrap';
import { Card, WaitIcon, ReactTable } from '../../../components/index';
import { server } from '../../../commons/server';
import Form from '../../../forms/form';
import moment from 'moment';

const fschema = {
	layout: [
		{
			property: 'iniDate',
			required: true,
			type: 'date',
			label: __('Period.iniDate'),
			size: { md: 4 },
			defaultValue: new Date()
		},
		{
			property: 'endDate',
			required: false,
			type: 'date',
			label: __('Period.endDate'),
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

const detailSchema = {
	layout: [
		{
			property: 'userName',
			type: 'string',
			label: 'User Name',
			size: { sm: 4 }
		},
		{
			property: 'workspace',
			type: 'string',
			label: 'Workspace',
			size: { sm: 4 }
		},
		{
			property: 'exceptionMessage',
			type: 'string',
			label: 'Exception Message',
			size: { sm: 4 }
		},
		{
			property: 'request',
			type: 'string',
			label: 'Request',
			size: { sm: 12 }
		},
		{
			property: 'stackTrace',
			type: 'string',
			label: 'Stack Trace',
			size: { sm: 12 }
		}
	]
};


/**
 * The page controller of the public module
 */
export default class ErrorLog extends React.Component {

	constructor(props) {
		super(props);

		this.refresh = this.refresh.bind(this);
		this.state = { doc: {} };
	}

	componentWillMount() {
		this.refreshTodayRep();
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
			endDate: this.state.doc.endDate,
			searchKey: this.state.doc.searchKey
		};

		server.post('/api/admin/rep/errorlog', query)
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result });
		});
	}

	refreshTodayRep() {
		const self = this;
		server.post('/api/admin/rep/todayerrorlog')
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result });
		});
	}

	headerRender(count) {
		const countHTML = <Badge className="tbl-counter">{count}</Badge>;

		// create the header of the card
		return (
			<Row>
				<Col md={12}>
					<h4>{__('Permission.ERRORLOGREP')} {count === 0 ? '' : countHTML}</h4>
				</Col>
			</Row>
			);
	}

	collapseRender(item) {
		return (<Row><div className="margin-2x"><Form schema={detailSchema} doc={item} readOnly /></div></Row>);
	}

	profileSubtitleRender(item) {
		return (<div>{item.unitName} <br/> {item.adminUnitName} </div>);
	}

	renderTableResult() {
		if (!this.state.values || !this.state.values.list || this.state.values.list.length < 1) {
			return <Alert className="mtop" bsStyle="warning">{__('form.norecordfound')}</Alert>;
		}

		const tschema = [
				{
					title: __('datetime.date'),
					content: item => moment(item.errorDate).format('L LTS'),
					size: { md: 4 }
				},
				{
					title: 'Exception Class',
					content: 'exceptionClass',
					size: { md: 4 }
				},
				{
					title: 'URL',
					content: 'url',
					size: { md: 4 }
				}
			];

		return (<div>
					<Card>
						<Row>
							<Col md={12}>
								<ReactTable columns={tschema}
									values={this.state.values.list}
									onExpandRender={this.collapseRender} />
							</Col>
						</Row>
					</Card>
				</div>);

	}

	render() {
		const header = this.headerRender(!this.state || !this.state.values ? 0 : this.state.values.count);

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

				{!this.state || !this.state.values ? <WaitIcon type="card" /> : this.renderTableResult()}

			</Card>
			</div>
		);
	}
}

ErrorLog.propTypes = {
	route: React.PropTypes.object
};
