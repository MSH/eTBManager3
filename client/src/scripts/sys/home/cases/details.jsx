
import React from 'react';
import { Grid, Row, Col, Nav, NavItem, Button } from 'react-bootstrap';
import { Card, WaitIcon, Fa, CommandBar } from '../../../components';
import PatientPanel from '../commons/patient-panel';
import { server } from '../../../commons/server';
import { app } from '../../../core/app';

import CaseData from './case-data';
import CaseExams from './case-exams';
import CaseTreatment from './case-treatment';
import CaseClose from './case-close';
import CaseMove from './case-move';
import CaseIssues from './case-issues';
import CaseTags from './case-tags';


export default class Details extends React.Component {

	constructor(props) {
		super(props);
		this.selectTab = this.selectTab.bind(this);
		this.show = this.show.bind(this);
		this.deleteConfirm = this.deleteConfirm.bind(this);
		this.reopenConfirm = this.reopenConfirm.bind(this);
		this.validationConfirm = this.validationConfirm.bind(this);
		this._onAppChange = this._onAppChange.bind(this);

		this.state = { selTab: 0 };
	}

	componentWillMount() {
		const id = this.props.route.queryParam('id');
		this.fetchData(id);

		app.add(this._onAppChange);
	}

	componentWillReceiveProps(nextProps) {
		// check if page must be updated
		const id = nextProps.route.queryParam('id');
		const oldId = this.state.tbcase ? this.state.tbcase.id : null;
		if (id !== oldId) {
			this.fetchData(id);
		}
	}

	componentWillUmount() {
        app.remove(this._onAppChange);
    }

    /**
     * Called when application state changes
     * @param  {[type]} action [description]
     * @return {[type]}        [description]
     */
    _onAppChange(action) {
        if (action === 'case_update') {
            this.fetchData(this.state.tbcase.id);
        }
    }

	fetchData(id) {
		const self = this;
		server.get('/api/tbl/case/' + id)
		.then(tbcase => self.setState({	tbcase: tbcase }));

		this.setState({ tbcase: null });
	}

	tagsRender() {
		const lst = this.state.tbcase.tags;

		if (!lst) {
			return null;
		}

		return (
			<div>
				{
					!lst ? <WaitIcon type="card" /> :
					lst.map(item => (
						<li key={item.id} className={'tag-' + item.type.toLowerCase()}>
							<div className="tag-title">{item.name}</div>
						</li>
					))
				}
			</div>
			);
	}

	selectTab(key) {
		this.setState({ selTab: key });
	}

	show(cpt, show) {
		const self = this;
		return () => {
			const obj = {};
			obj[cpt] = show;
			self.setState(obj);
		};
	}

	showConfirmDlg(title, message, onConfirm) {
		app.messageDlg({
			title: title,
			message: message,
			style: 'warning',
			type: 'YesNo'
		})
		.then(res => onConfirm(res));
	}

	deleteConfirm(action) {
		if (action === 'yes') {
			server.delete('/api/tbl/case/' + this.state.tbcase.id)
				.then(() => {
					app.messageDlg({
						title: __('action.delete'),
						message: __('default.entity_deleted'),
						style: 'info',
						type: 'Ok'
					})
					.then(() => app.goto('/sys/home/unit/cases?id=' + this.state.tbcase.ownerUnit.id));
				});
		}

		this.setState({ showDelConfirm: false });
	}

	validationConfirm(action) {
		if (action === 'yes') {
			server.get('/api/cases/case/validate/' + this.state.tbcase.id)
				.then(res => {
					if (res && res.errors) {
						return Promise.reject(res.errors);
					}

					this.fetchData(this.state.tbcase.id);

					return res;
				});
			}

		this.setState({ showValidationConfirm: false });
	}

	reopenConfirm(action) {
		if (action === 'yes') {
			server.get('/api/cases/case/reopen/' + this.state.tbcase.id)
				.then(res => {
					if (res && res.errors) {
						return Promise.reject(res.errors);
					}

					this.fetchData(this.state.tbcase.id);

					return res;
				});
			}

		this.setState({ showReopenConfirm: false });
	}

	render() {
		const tbcase = this.state.tbcase;

		if (!tbcase) {
			return <WaitIcon type="page" />;
		}

		const seltab = this.state.selTab;

		const tabs = (
			<Nav bsStyle="tabs" activeKey={seltab}
				onSelect={this.selectTab}
				className="app-tabs">
				<NavItem key={0} eventKey={0}>{__('cases.details.case')}</NavItem>
				<NavItem key={1} eventKey={1}>{__('cases.details.followup')}</NavItem>
				<NavItem key={2} eventKey={2}>{__('cases.details.treatment')}</NavItem>
				<NavItem key={3} eventKey={3}>{__('cases.issues')}</NavItem>
			</Nav>
			);

		const tagh = (<span>
						<h4 className="inlineb mright">
							{__('admin.tags')}
						</h4>
						<Button onClick={this.show('showTagEdt', true)} bsSize="small">
							<Fa icon="pencil"/>
						</Button>
					</span>);

		// create command list
		const commands = [
		{
			title: __('cases.delete'),
			onClick: () => this.showConfirmDlg(__('action.delete'), __('form.confirm_remove'), this.deleteConfirm),
			icon: 'remove'
		},
		{
			title: __('cases.move'),
			onClick: this.show('showMoveCase', true),
			icon: 'toggle-right'
		}];

		if (!this.state.tbcase.validated) {
			const cmd = {
							title: __('cases.validate'),
							onClick: () => this.showConfirmDlg(__('cases.validate'), __('cases.validate.confirm'), this.validationConfirm),
							icon: 'check'
						};
			commands.push(cmd);
		}

		if (this.state.tbcase.state === 'CLOSED') {
			const cmd = {
							title: __('cases.reopen'),
							onClick: () => this.showConfirmDlg(__('cases.reopen'), __('cases.reopen.confirm'), this.reopenConfirm),
							icon: 'power-off'
						};
			commands.push(cmd);
		} else {
			const cmd = {
							title: __('cases.close'),
							onClick: this.show('showCloseCase', true),
							icon: 'power-off'
						};
			commands.push(cmd);
		}

		return (
			<div>
				<PatientPanel tbcase={tbcase} />
				<Grid fluid>
					<Row className="mtop">
						<Col sm={3}>
							<CommandBar commands={commands} />
							<Card className="mtop" header={tagh}>
							{
								this.tagsRender()
							}
							</Card>
							<Card title="Other cases of this patient" />
						</Col>
						<Col sm={9}>
							{tabs}
							{seltab === 0 && <CaseData tbcase={tbcase} />}
							{seltab === 1 && <CaseExams tbcase={tbcase} />}
							{seltab === 2 && <CaseTreatment tbcase={tbcase} />}
							{seltab === 3 && <CaseIssues tbcase={tbcase} />}
						</Col>
					</Row>
				</Grid>

				<CaseClose show={this.state.showCloseCase} onClose={this.show('showCloseCase', false)} tbcase={tbcase}/>

				<CaseMove show={this.state.showMoveCase} onClose={this.show('showMoveCase', false)} tbcase={tbcase}/>

				<CaseTags show={this.state.showTagEdt} onClose={this.show('showTagEdt', false)} tbcase={tbcase}/>

			</div>
			);
	}
}


Details.propTypes = {
	route: React.PropTypes.object
};
