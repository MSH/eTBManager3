
import React from 'react';
import { Tabs, Tab, Grid, Row, Col, Button, Badge } from 'react-bootstrap';
import { Card, CollapseRow, WaitIcon } from '../../../components/index';
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
			userId: this.state.doc.userId ? this.state.doc.userId : ''
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
					<h4>{__('admin.reports.usersession')} {count === 0 ? '' : countHTML}</h4>
				</Col>
			</Row>
			);
	}

	tableRender() {
		const rowList = this.state.values.list.map(item => ({ data: item, detailsHTML: this.parseDetails(item) }));

		return (<Grid className="mtop-2x table">
							<Row className="title">
								<Col md={4} className="nopadding">
									{__('User')}
								</Col>

								<Col md={4} className="nopadding">
									{__('UserLogin.loginDate')}
								</Col>

								<Col md={4} className="nopadding">
									{__('admin.websessions.sessiontime')}
								</Col>
							</Row>

							{
								rowList.map(item => (
								<CollapseRow collapsable={item.detailsHTML} className={'tbl-cell'}>
									<Col md={4}>
										{item.data.userName}
									</Col>

									<Col md={4}>
										{new Date(item.data.loginDate).toString()}
									</Col>

									<Col md={4}>
										{'TO DO: calcular baseado na hora de login e hra de logout'}
									</Col>
								</CollapseRow>
								))
							}
						</Grid>);
	}

	render() {
		const selday = this.state.iniDate;
		const modifiers = {
			selected: day => selday ? DateUtils.isSameDay(day, selday) : false
		};

		const header = this.headerRender(!this.state || !this.state.values ? 0 : this.state.values.count);

		return (
			<Card header={header}>
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
