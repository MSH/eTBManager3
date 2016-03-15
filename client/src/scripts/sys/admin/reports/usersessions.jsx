
import React from 'react';
import { Tabs, Tab, Grid, Row, Col, Button, Badge } from 'react-bootstrap';
import { Card, DatePicker, CollapseRow, SelectionBox, WaitIcon } from '../../../components/index';
import DayPicker, { DateUtils } from 'react-day-picker';
import { server } from '../../../commons/server';
import CRUD from '../../../commons/crud';

/**
 * The page controller of the public module
 */
export default class UserSessions extends React.Component {

	constructor(props) {
		super(props);
		this.onDayClickCalendar = this.onDayClickCalendar.bind(this);
		this.refreshTableAllFilters = this.refreshTableAllFilters.bind(this);

		this.state = { iniDate: new Date() };
	}

	componentWillMount() {
		this.refreshTableByDay(this.state.iniDate);
		this.loadUsersList();
	}

	loadUsersList() {
		const crud = new CRUD('userws');
		const qry = { profile: 'item' };
		const self = this;

		crud.query(qry)
		.then(res => {
			self.setState({ users: res.list });
		});
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
		// TODO: VALIDATE BEFORE THIS

		const self = this;
		const query = {
			iniDate: this.state.iniDate,
			endDate: this.state.endDate,
			userId: this.state.user ? this.state.user.userId : ''
		};

		return server.post('/api/admin/rep/usersession', query)
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result });
			// return to the promise
			return result;
		});
	}

	onDayClickCalendar(e, day) {
		this.refreshTableByDay(day);
	}

	onValueChange(ref) {
		const self = this;
		return (evt, val) => {
			const obj = {};
			obj[ref] = val;
			self.setState(obj);
		};
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
				<div>
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
						<div>
							<Row>
								<Col md={4}>
									<DatePicker label={__('Period.iniDate')} onChange={this.onValueChange('iniDate')} />
								</Col>

								<Col md={4}>
									<DatePicker label={__('Period.endDate')} onChange={this.onValueChange('endDate')} />
								</Col>

								<Col md={4}>
								{!this.state || !this.state.users ? <WaitIcon type="field" /> : 
									<SelectionBox ref="selBox1"
										value={this.state.selBox1}
										mode="single"
										optionDisplay="name"
										label={__('User')}
										onChange={this.onValueChange('user')}
										options={this.state.users} />}
								</Col>

								<Col md={12}><Button onClick={this.refreshTableAllFilters} bsStyle="primary">{'Update'}</Button></Col>
							</Row>
						</div>
					</Tab>
				</Tabs>

				{!this.state || !this.state.values ? <WaitIcon type="card" /> : this.tableRender()}

				</div>
			</Card>
		);
	}
}

UserSessions.propTypes = {
	route: React.PropTypes.object
};
