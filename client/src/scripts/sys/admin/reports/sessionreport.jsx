
import React from 'react';
import { Tabs, Tab, Grid, Row, Col, Button, Badge } from 'react-bootstrap';
import { Card, DatePicker, CollapseRow, SelectionBox } from '../../../components/index';
import DayPicker, { DateUtils } from 'react-day-picker';

const userSessions = [
	{
		name: 'Mauricio Santos1',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS1',
		workspace: 'LMIS Demo',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		logoutDate: 'Feb 25, 2016 9:11:19 AM EST',
		IpAddress: '127.0.0.1',
		Application: 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36'
	},
	{
		name: 'Mauricio Santos2',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS2',
		workspace: 'LMIS Demo',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		logoutDate: 'Feb 25, 2016 9:11:19 AM EST',
		IpAddress: '127.0.0.1',
		Application: 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36'
	},
	{
		name: 'Mauricio Santos3',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS3',
		workspace: 'LMIS Demo',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		logoutDate: 'Feb 25, 2016 9:11:19 AM EST',
		IpAddress: '127.0.0.1',
		Application: 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36'
	},
	{
		name: 'Mauricio Santos4',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS4',
		workspace: 'LMIS Demo',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		logoutDate: 'Feb 25, 2016 9:11:19 AM EST',
		IpAddress: '127.0.0.1',
		Application: 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36'
	},
	{
		name: 'Mauricio Santos5',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS5',
		workspace: 'LMIS Demo',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		logoutDate: 'Feb 25, 2016 9:11:19 AM EST',
		IpAddress: '127.0.0.1',
		Application: 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36'
	}
];

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

		this.state = { };
	}

	allInformation(item) {
		const collapsedValue = (<div className="text-small">
									<dl>
										<Col sm={4}>
											<dt>{__('User.login') + ':'}</dt>
											<dd>{item.login}</dd>
										</Col>
										<Col sm={4}>
											<dt>{__('UserLogin.logoutDate') + ':'}</dt>
											<dd>{item.logoutDate}</dd>
										</Col>
										<Col sm={4}>
											<dt>{__('UserLogin.ipAddress') + ':'}</dt>
											<dd>{item.IpAddress}</dd>
										</Col>
									</dl>
									<Col md={12} className="text-small">{item.Application}</Col>
								</div>);
		return (collapsedValue);
	}

	onDayClick(e, day) {
		this.setState({ day: day });
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

	headerRender() {
		const countHTML = <Badge className="tbl-counter">{500}</Badge>;

		// create the header of the card
		return (
			<Row>
				<Col md={12}>
					<h4>{__('admin.reports.usersession')}{countHTML}</h4>
				</Col>
			</Row>
			);
	}

	render() {
		const selday = this.state.day;

		const list = userSessions.map(item => ({ data: item, detailsHTML: this.allInformation(item) }));

		const modifiers = {
			selected: day => selday ? DateUtils.isSameDay(day, selday) : false
		};

		const header = this.headerRender();

		return (
			<Card header={header}>
				<form>
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

					<Grid className="mtop-2x table">
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
							list.map(item => (
							<CollapseRow collapsable={item.detailsHTML} className={'tbl-cell'}>
								<Col md={4}>
									{item.data.name}
								</Col>

								<Col md={4}>
									{item.data.loginDate}
								</Col>

								<Col md={4}>
									{item.data.sessionTime}
								</Col>
							</CollapseRow>
							))
						}

					</Grid>
				</form>
			</Card>
		);
	}
}

SessionReport.propTypes = {
	route: React.PropTypes.object
};
