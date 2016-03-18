
import React from 'react';
import { Tabs, Tab, Row, Col, Button, Badge } from 'react-bootstrap';
import { Card, ReactTable, WaitIcon, Profile } from '../../../components/index';
import DayPicker, { DateUtils } from 'react-day-picker';
import { server } from '../../../commons/server';
import Form from '../../../forms/form';

const fschema = {
			layout: [
				{
					property: 'iniDate',
					required: true,
					type: 'date',
					label: __('Period.iniDate'),
					size: { sm: 4 }
				},
				{
					property: 'endDate',
					required: false,
					type: 'date',
					label: __('Period.endDate'),
					size: { sm: 4 }
				},
				{
					property: 'userId',
					required: false,
					type: 'select',
					label: __('User'),
					options: 'users',
					size: { sm: 4 }
				}
			]
		};

/**
 * The page controller of the public module
 */
export default class UserSessions extends React.Component {

	constructor(props) {
		super(props);
		this.onDayClickCalendar = this.onDayClickCalendar.bind(this);
		this.refreshTableAllFilters = this.refreshTableAllFilters.bind(this);
		this.onChangeDoc = this.onChangeDoc.bind(this);

		this.state = { iniDate: new Date(), doc: {} };
	}

	componentWillMount() {
		this.refreshTableByDay(this.state.iniDate);
	}

	onChangeDoc() {
		this.forceUpdate();
	}

	/**
	 * Called when the report wants to update its content
	 * @return {[type]} [description]
	 */
	refreshTableByDay(day) {
		const self = this;

		const query = {
			iniDate: day.getTime()
		};

		return server.post('/api/admin/rep/dailyusersession', query)
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result, iniDate: day });
			// return to the promise
			return result;
		});
	}

	refreshTableAllFilters() {
		const self = this;

		const errors = this.refs.form.validate();
		this.setState({ errors: errors });
		if (errors) {
			return;
		}

		const query = {
			iniDate: this.state.doc.iniDate,
			endDate: this.state.doc.endDate,
			userId: this.state.doc.userId
		};

		server.post('/api/admin/rep/usersession', query)
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result });
		});
	}

	onDayClickCalendar(e, day) {
		this.refreshTableByDay(day);
	}

	headerRender(count) {
		const countHTML = <Badge className="tbl-counter">{count}</Badge>;

		// create the header of the card
		return (
			<Row>
				<Col md={12}>
					<h4>{__('admin.reports.usersession')} {count === 0 ? '' : countHTML}</h4>
				</Col>
			</Row>
			);
	}

	tableRender() {
		const colschema = [
			{
				title: __('User'),
				content: item => <Profile size="small" title={item.userName} type="user" />,
				size: { sm: 4 }
			},
			{
				title: __('UserLogin.loginDate'),
				content: item => new Date(item.loginDate).toString(),
				size: { sm: 4 }
			},
			{
				title: __('admin.websessions.idletime') + ' TODOMS: CALC with momentsjs',
				content: item => new Date(item.loginDate).toString(),
				size: { sm: 4 }
			}
		];

		return (
				<div>
					<Row>
						<Col md={12}>
							<ReactTable columns={colschema}
								values={this.state.values.list}
								collapseRender={this.collapseRender} />
						</Col>
					</Row>
				</div>
			);
	}

	collapseRender(item) {
		return (<div className="text-small">
					<dl>
						<Col sm={4}>
							<dt>{__('User.login') + ':'}</dt>
							<dd>{item.userLogin}</dd>
						</Col>
						<Col sm={4}>
							<dt>{__('UserLogin.logoutDate') + ':'}</dt>
							<dd>{new Date(item.logoutDate).toString()}</dd>
						</Col>
						<Col sm={4}>
							<dt>{__('UserLogin.ipAddress') + ':'}</dt>
							<dd>{item.ipAddress}</dd>
						</Col>
					</dl>
					<Col md={12} className="text-small">{item.Application}</Col>
				</div>);
	}

	render() {
		const selday = this.state.iniDate;
		const modifiers = {
			selected: day => selday ? DateUtils.isSameDay(day, selday) : false
		};

		return (
			<Card header={this.headerRender(!this.state || !this.state.values ? 0 : this.state.values.count)}>
				<Tabs defaultActiveKey={1} className="mtop" animation={false}>

					<Tab eventKey={1} title={__('datetime.date')} className="mtop">
						<div>
							<Row>
								<Col md={4} />
								<Col md={4}>
									<div>
										<DayPicker modifiers={modifiers} onDayClick={this.onDayClickCalendar} />
									</div>
								</Col>
								<Col md={4}/>
							</Row>
						</div>
					</Tab>

					<Tab eventKey={2} title={__('form.otherfilters')} className="mtop">
						<Row>
							<Col md={12}>
								<Form ref="form"
									schema={fschema}
									doc={this.state.doc}
									onChange={this.onChangeDoc}
									errors={this.state.errors} />
							</Col>
							<Col md={12}><Button onClick={this.refreshTableAllFilters} bsStyle="primary">{'Update'}</Button></Col>
						</Row>
					</Tab>
				</Tabs>

				{!this.state || !this.state.values ? <WaitIcon type="card" /> : this.tableRender()}
			</Card>
		);
	}
}

UserSessions.propTypes = {
	route: React.PropTypes.object
};
