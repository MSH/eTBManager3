
import React from 'react';
import { Row, Col, Button, Badge, Label } from 'react-bootstrap';
import { Card, WaitIcon, ReactTable, Profile, Fa } from '../../../components/index';
import { server } from '../../../commons/server';
import Form from '../../../forms/form';
import { app } from '../../../core/app';
import moment from 'moment';

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
					type: 'string',
					max: 50,
					label: __('admin.reports.cmdhistory.cmdevent'),
					size: { sm: 4 }
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
			endDate: this.state.doc.endDate,
			action: this.state.doc.action,
			userId: this.state.doc.userId,
			type: this.state.doc.type,
			adminUnitId: this.state.doc.adminUnitId,
			searchKey: this.state.doc.searchKey
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

	collapseRender(item) {

		const text = item.detail.text ?
							<Col sm={12}>
								<dl className="dl-horizontal text-muted">
								<dt>{__('global.comments')}<Fa icon="comment-o"/>{':'}</dt>
								<dd>{item.detail.text}</dd>
								</dl>
							</Col> : null;

		var vals = null;
		if (item.detail.items || item.detail.diffs) {

		vals = (<Col sm={12}>
					<dl className="dl-horizontal text-muted">
					{item.detail.diffs && item.detail.diffs.map(diff => 	<div>
																				<dt>
																					{diff.title}<Fa icon="pencil-square-o"/>{':'}
																				</dt>
																				<dd>
																					{diff.prevValue ? diff.prevValue : __('global.notdef')}
																					<Fa icon="caret-right"/>
																					{diff.newValue ? diff.newValue : __('global.notdef')}
																				</dd>
																			</div>)}

					{item.detail.items && item.detail.items.map(i => 	<div>
																			<dt>
																				{i.title.substr(0, 1) === '-' || i.title.substr(0, 1) === '+' ? i.title.substr(1, i.length) : i.title}
																				{i.title.substr(0, 1) === '+' ? <Fa icon="plus-square-o"/> : null}
																				{i.title.substr(0, 1) === '-' ? <Fa icon="minus-square-o"/> : null}
																				{':'}
																			</dt>
																			<dd>{i.value}</dd>
																		</div>)}
					</dl>
				</Col>);
		}

		return (<Row><div className="margin-2x">{text} {vals}</div></Row>);
	}

	profileSubtitleRender(item) {
		return (<div>{item.unitName} <br/> {item.adminUnitName} </div>);
	}

	render() {
		const header = this.headerRender(!this.state || !this.state.values ? 0 : this.state.values.count);

		const tschema = [
		{
			title: __('User'),
			content: item => <Profile size="small" title={item.userName} type="user" subtitle={this.profileSubtitleRender(item)} />,
			size: { sm: 3 }
		},
		{
			title: __('datetime.date'),
			content: item => moment(item.execDate).format('L LTS'),
			size: { sm: 2 }
		},
		{
			title: __('admin.reports.cmdhistory.cmdevent'),
			content: 'type',
			size: { sm: 2 }
		},
		{
			title: __('form.action'),
			content: item => {
				var c;
				switch (item.action.id) {
				case 'EXEC':
					c = 'default';
					break;
				case 'CREATE':
					c = 'success';
					break;
				case 'UPDATE':
					c = 'warning';
					break;
				case 'DELETE':
					c = 'danger';
					break;
				default:
					c = 'default';
				}
				return (<Label bsStyle={c}>{item.action.name}</Label>);
			},
			size: { sm: 1 }
		},
		{
			title: 'entityName',
			content: 'entityName',
			size: { sm: 4 }
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
									values={this.state.values.list}
									onExpandRender={this.collapseRender} />
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
