
import React from 'react';
import { Tabs, Tab, Grid, Row, Col, Button } from 'react-bootstrap';
import { Fluidbar, DatePicker, CollapseRow, SelectionBox } from '../../../components/index';
import DayPicker, { DateUtils } from 'react-day-picker';

	const options2 = [
		'Ricardo Memoria',
		'Mauricio Santos',
		'Gustavo Bastos',
		'Kyle Duarte'
	];

/**
 * The page controller of the public module
 */
export default class UserSession extends React.Component {

	constructor(props) {
		super(props);
		this.onDayClick = this.onDayClick.bind(this);
		this.dayPickerSelect = this.dayPickerSelect.bind(this);

		this.state = { };
	}

	allInformation() {
			const collapsedValue = (<div className="text-small">
										<Col md={3}><span className="bold">{'Login: '}</span>{'MSANTOS'}</Col>
										<Col md={3}><span className="bold">{'Workspace: '}</span>{'LMIS Demo'}</Col>
										<Col md={3}><span className="bold">{'Login time: '}</span>{'Feb 25, 2016 9:11:19 AM EST'}</Col>
										<Col md={3}><span className="bold">{'Logout time: '}</span>{'Feb 25, 2016 9:11:19 AM EST'}</Col>
										<Col md={12} className="text-small">{'179.180.91.94 - Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36'}</Col>
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

	render() {
		var detail = this.allInformation();

		const selday = this.state.day;

		const modifiers = {
			selected: day => selday ? DateUtils.isSameDay(day, selday) : false
		};

		return (
			<div className="mtop-2x">
				<h3>{'User Session Report'}</h3>
				<Fluidbar>
				<form>
					<Tabs defaultActiveKey={2} className="mtop" animation={false}>

						<Tab eventKey={1} title="Date" className="mtop">
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

						<Tab eventKey={2} title="Other Filters" className="mtop">
							<div>
								<Row>
									<Col md={4}>
										<DatePicker label="Start Date:" />
									</Col>

									<Col md={4}>
										<DatePicker label="End Date:" />
									</Col>

									<Col md={4}>
										<SelectionBox ref="selBox1"
											value={this.state.selBox1}
											mode="single"
											optionDisplay={this.itemDisplay}
											label="User:"
											onChange={this.onChange('selBox1')}
											options={options2}/>
									</Col>

									<Col md={12}><Button bsStyle="primary">Update</Button></Col>
								</Row>
							</div>
						</Tab>

					</Tabs>

					<Grid className="mtop-2x table">
						<Row className="bold">
							<Col md={4}>
								{'User'}
							</Col>

							<Col md={4}>
								{'Login In'}
							</Col>

							<Col md={4}>
								{'Session Time'}
							</Col>
						</Row>

						<CollapseRow collapsable={detail} className={'tbl-cell'}>
							<Col md={4}>
								{'Ricardo Memoria'}
							</Col>

							<Col md={4}>
								{'22/06/2015 12:52:60'}
							</Col>

							<Col md={4}>
								{'2 hours and 30 minutes'}
							</Col>
						</CollapseRow>

						<CollapseRow collapsable={detail} className={'tbl-cell'}>
							<Col md={4}>
								{'Ricardo Memoria'}
							</Col>

							<Col md={4}>
								{'22/06/2015 12:52:60'}
							</Col>

							<Col md={4}>
								{'2 hours and 30 minutes'}
							</Col>
						</CollapseRow>

					</Grid>
				</form>
				</Fluidbar>
			</div>
		);
	}
}

UserSession.propTypes = {
	route: React.PropTypes.object
};
