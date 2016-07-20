
import React from 'react';
import { Grid, Row, Col, Nav, NavItem, Button } from 'react-bootstrap';
import { Card, WaitIcon, MessageDlg, Fa, CommandBar } from '../../../components';
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

import { generateName, mockTbCase } from '../../mock-data';


export default class Details extends React.Component {

	constructor(props) {
		super(props);
		this.selectTab = this.selectTab.bind(this);
		this.show = this.show.bind(this);
		this.deleteConfirm = this.deleteConfirm.bind(this);
		this.reopenConfirm = this.reopenConfirm.bind(this);
		this.goToUnitPage = this.goToUnitPage.bind(this);

		this.state = { selTab: 2 };
	}

	componentWillMount() {
		const id = this.props.route.queryParam('id');
		this.fetchData(id);
	}

	componentWillReceiveProps(nextProps) {
		// check if page must be updated
		const id = nextProps.route.queryParam('id');
		const oldId = this.state.tbcase ? this.state.tbcase.id : null;
		if (id !== oldId) {
			this.fetchData(id);
		}
	}


	fetchData(id) {
		const self = this;
		server.get('/api/cases/case/' + id)
		.then(tbcase => {
			const contacts = [];
			for (var i = 0; i < 5; i++) {
				const res = generateName();
				contacts.push({
					id: res.id,
					name: res.name,
					gender: res.gender,
					age: res.age
				});
			}
			const data = Object.assign({}, mockTbCase, { contacts: contacts }, tbcase);

			self.setState({
				tbcase: data
			});
		});

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
						<a key={item.id} className={'tag-link tag-' + item.type}>
							<div className="tag-title">{item.name}</div>
						</a>
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

	deleteConfirm(action) {
		const self = this;

		if (action === 'yes') {
			server.delete('/api/cases/case/' + this.state.tbcase.id)
				.then(() => {
					this.setState({ showDelSuccess: true });
				});
		}

		this.setState({ showDelConfirm: false });
	}

	goToUnitPage() {
		app.goto('/sys/home/unit/cases?id=' + this.state.tbcase.ownerUnit.id);
	}

	reopenConfirm(action) {
		if (action === 'yes') {
			server.get('/api/cases/case/reopen/' + this.state.tbcase.id)
				.then(() => {
					console.log('update this.state.tbcase');
					this.setState({ showReopenConfirm: false });
				});
		} else if (action === 'no') {
			this.setState({ showReopenConfirm: false });
		}
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

		const commands = [
		{
			label: __('cases.delete'),
			onClick: this.show('showDelConfirm', true),
			icon: 'remove'
		},
		{
			label: __('cases.close'),
			onClick: this.show('showCloseCase', true),
			icon: 'power-off'
		},
		{
			label: __('cases.reopen'),
			onClick: this.show('showReopenConfirm', true),
			icon: 'power-off'
		},
		{
			label: __('cases.move'),
			onClick: this.show('showMoveCase', true),
			icon: 'toggle-right'
		}
		];

		if (this.state.tbcase.state === 'CLOSED') {
			commands.splice(1, 1);
		} else {
			commands.splice(2, 1);
		}

		return (
			<div>
				<PatientPanel patient={tbcase.patient} recordNumber={tbcase.recordNumber} />
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

				<MessageDlg show={this.state.showDelConfirm}
					onClose={this.deleteConfirm}
					title={__('action.delete')}
					message={__('form.confirm_remove')} style="warning" type="YesNo" />

				<MessageDlg show={this.state.showDelSuccess}
					onClose={this.goToUnitPage}
					title={__('action.delete')}
					message={__('default.entity_deleted')} style="info" type="Ok" />

				<MessageDlg show={this.state.showReopenConfirm}
					onClose={this.reopenConfirm}
					title={__('cases.reopen')}
					message={__('cases.reopen.confirm')} style="warning" type="YesNo" />

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
