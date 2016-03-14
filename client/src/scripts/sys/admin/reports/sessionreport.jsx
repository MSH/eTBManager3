
import React from 'react';
import { Tabs, Tab, Grid, Row, Col, Button, Badge } from 'react-bootstrap';
import { Card, DatePicker, CollapseRow, SelectionBox, WaitIcon } from '../../../components/index';
import DayPicker, { DateUtils } from 'react-day-picker';
import { server } from '../../../commons/server';

const userList = [
	'Ricardo Memoria',
	'Mauricio Santos',
	'Gustavo Bastos',
	'Kyle Duarte'
];

/**
 * The page controller of the public module
 */
export default class SessionReport extends React.Component {

	constructor(props) {
		super(props);
		this.onDayClick = this.onDayClick.bind(this);
		this.dayPickerSelect = this.dayPickerSelect.bind(this);

		this.state = { day: new Date() };
	}

	componentWillMount() {
		this.refreshTableByDay(this.state.day);
	}

	/**
	 * Called when the report wants to update its content
	 * @return {[type]} [description]
	 */
	refreshTableByDay(day) {
		const self = this;
		const query = {
			iniDate: day ? day.getTime() : ''
		};

		return server.post('/api/admin/rep/dailysessionreport', query)
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result, day: day });
			// return to the promise
			return result;
		});
	}

	onDayClick(e, day) {
		this.refreshTableByDay(day);
	}

	onChange(ref) {
		const self = this;
		return (evt, val) => {
			const obj = {};
			obj[ref] = val;
			self.setState(obj);
		};
	}

	dayPickerSelect(e, day) {
		this.setState({ dayPicker: day.toString() });
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
					<h4>{__('admin.reports.usersession')}{count === 0 ? '' : countHTML}</h4>
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
										{item.data.loginDate}
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
		const selday = this.state.day;

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
										<DayPicker modifiers={modifiers} onDayClick={this.onDayClick} />
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
									<DatePicker label={__('Period.iniDate')} />
								</Col>

								<Col md={4}>
									<DatePicker label={__('Period.endDate')} />
								</Col>

								<Col md={4}>
									<SelectionBox ref="selBox1"
										value={this.state.selBox1}
										mode="single"
										optionDisplay={this.itemDisplay}
										label={__('User')}
										onChange={this.onChange('selBox1')}
										options={userList}/>
								</Col>

								<Col md={12}><Button bsStyle="primary">{'Update'}</Button></Col>
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

SessionReport.propTypes = {
	route: React.PropTypes.object
};
