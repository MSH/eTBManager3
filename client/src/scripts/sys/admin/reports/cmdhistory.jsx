
import React from 'react';
import { Tabs, Tab, Grid, Row, Col, Button, Badge, Input } from 'react-bootstrap';
import { Card, DatePicker, CollapseRow, SelectionBox, WaitIcon } from '../../../components/index';
import DayPicker, { DateUtils } from 'react-day-picker';
import { server } from '../../../commons/server';
import CRUD from '../../../commons/crud';
import { validateForm } from '../../../commons/validator';
import { AdminUnitControl } from '../../types/admin-unit-control';

/**
 * Form validation model
 */
const form = {
	inidate: {
		required: true
	}
};

/**
 * The page controller of the public module
 */
export default class CommandHistory extends React.Component {

	constructor(props) {
		super(props);
		this.refresh = this.refresh.bind(this);
	}

	componentWillMount() {
		this.loadUsersList();
		this.loadActionList();
		this.loadTypeList();
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

	loadActionList() {
		const ac = [
			{ code: 1, name: 'Exec' },
			{ code: 2, name: 'New' },
			{ code: 3, name: 'Update' },
			{ code: 4, name: 'delete' }
		];

		this.setState({ actions: ac });
	}

	loadTypeList() {
		const ty = [
			{ code: 1, name: 'Event1' },
			{ code: 2, name: 'Event2' },
			{ code: 3, name: 'Event3' },
			{ code: 4, name: 'Event event event event event event' }
		];

		this.setState({ types: ty });
	}

	refresh() {
		const self = this;

		const vals = validateForm(self, form);

        // there is any validation error ?
        if (vals.errors) {
            this.setState({ errors: vals.errors });
            return;
        }
        /*amigo estou aqui*/
		const query = {
			iniDate: this.state.iniDate,
			endDate: this.state.endDate ? this.state.endDate : null,
			action: this.state.action ? this.state.action.code : null,
			userId: this.state.user ? this.state.user.userId : null,
			type: this.state.type ? this.state.type.name : null,
			adminUnitId: this.state.adminunit ? this.state.adminunit.id : null,
			searchKey: this.refs.skey.value ? this.refs.skey.getValue : null
		};

		server.post('/api/admin/rep/cmdhistory', query)
		.then(res => {
			// generate new result
			const result = { count: res.count, list: res.list };
			// set state
			self.setState({ values: result });
		});
	}

	onValueChange(ref) {
		const self = this;
		return (evt, val) => {
			const obj = {};
			obj[ref] = val;
			self.setState(obj);
		};
	}

	/*parseDetails(item) {
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
	}*/

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

	/*tableRender() {
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
	}*/

	render() {

		const header = this.headerRender(!this.state || !this.state.values ? 0 : this.state.values.count);

		const err = this.state.errors || {};

		return (
			<Card header={header}>
				<div>
				<Row>
					<Col sm={6} md={3}>
						<DatePicker ref="inidate" label={__('Period.iniDate')} onChange={this.onValueChange('iniDate')}
							help={err.inidate} bsStyle={err.inidate ? 'error' : undefined} />
					</Col>

					<Col sm={6} md={3}>
						<DatePicker ref="enddate" label={__('Period.endDate')} onChange={this.onValueChange('endDate')} />
					</Col>

					<Col sm={4} md={2}>
					{!this.state || !this.state.actions ? <WaitIcon type="field" /> :
						<SelectionBox ref="acSelBox"
							value={this.state.acSelBox}
							mode="single"
							optionDisplay="name"
							label={'Action'}
							onChange={this.onValueChange('action')}
							options={this.state.actions} />}
					</Col>

					<Col sm={8} md={4}>
					{!this.state || !this.state.users ? <WaitIcon type="field" /> :
						<SelectionBox ref="uSelBox"
							value={this.state.uSelBox}
							mode="single"
							optionDisplay="name"
							label={__('User')}
							onChange={this.onValueChange('user')}
							options={this.state.users} />}
					</Col>
				</Row>
				<Row>
					<Col sm={4} md={5}>
					{!this.state || !this.state.types ? <WaitIcon type="field" /> :
						<SelectionBox ref="tySelBox"
							value={this.state.tySelBox}
							mode="single"
							optionDisplay="name"
							label={'Type'}
							onChange={this.onValueChange('type')}
							options={this.state.types} />}
					</Col>

					<Col sm={4} md={4}>
						
					</Col>

					<Col sm={4} md={3}>
						<Input type="text" ref="skey" label={'Search Key:'} />
					</Col>
				</Row>
				<Row>
					<Col md={12}><Button onClick={this.refresh} bsStyle="primary">{'Update'}</Button></Col>
				</Row>
				</div>

				{!this.state || !this.state.values ? <WaitIcon type="card" /> : this.tableRender()} 

			</Card>
		);
	}
}

CommandHistory.propTypes = {
	route: React.PropTypes.object
};
