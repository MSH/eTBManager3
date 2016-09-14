import React from 'react';
import { Grid, Col, Row, DropdownButton, MenuItem } from 'react-bootstrap';
import { Card, WaitIcon, Fa } from '../../../components';
import Form from '../../../forms/form';
import { server } from '../../../commons/server';
import { app } from '../../../core/app';
import TreatProgress from './treat/treat-progress';
import TreatTimeline from './treat/treat-timeline';
import AddMedicine from './treat/add-medicine';
import TreatFollowup from './treat/treat-followup';
import NoTreatPanel from './treat/no-treat-panel';


/**
 * Display the content of the case treatment tab
 */
export default class CaseTreatment extends React.Component {

	constructor(props) {
		super(props);

		this.menuClick = this.menuClick.bind(this);

		this.state = {
			sc1: {
				controls: [
					{
						type: 'string',
						label: __('Regimen'),
						value: doc => doc.regimen ? doc.regimen.name : __('regimens.individualized'),
						size: { md: 12 }
					},
					{
						type: 'text',
						property: 'regimenIni.name',
						label: __('TbCase.regimenIni'),
						visible: doc => doc.regimen && doc.regimenIni && doc.regimenIni.id !== doc.regimen.id,
						size: { md: 12 }
					},
					{
						type: 'period',
						property: 'period',
						label: __('cases.treat'),
						size: { md: 12 }
					}
				]
			}
		};
	}

	componentWillMount() {
		const self = this;
		const tbcase = this.props.tbcase;

		// check if case is on treatment and has treatment information
		if (!tbcase.treatment && tbcase.treatmentPeriod) {
			const id = tbcase.id;

			server.get('/api/cases/case/treatment/' + id)
				.then(res => {
					tbcase.treatment = res;
					self.forceUpdate();
				});
		}
	}

	menuClick(key) {
		switch (key) {
			case 1:
				this.addMedicine();
				break;
			case 3:
				this.undoTreatment();
				break;
			default:
		}
	}

	/**
	 * Called when user select the command to add a new medicine to the treatment regimen
	 */
	addMedicine() {
		this.setState({ show: 'add-med' });
	}

	/**
	 * Undo the treatment, moving the case back to the 'not on treatment' state
	 */
	undoTreatment() {
		app.messageDlg({
			title: __('cases.treat.undo'),
			message: __('cases.treat.undo.confirm'),
			style: 'warning',
			type: 'YesNo'
		})
		.then(res => {
			if (res !== 'yes') {
				return Promise.reject();
			}

			const caseId = this.props.tbcase.id;
			return server.post('/api/cases/case/treatment/undo/' + caseId);
		})
		.then(() => app.dispatch('case-update'))
		.catch(() => {});
	}

	closeDlg() {
		this.setState({ show: null });
	}

	render() {
		const tbcase = this.props.tbcase;

		// is not on treatment
		if (!tbcase.treatmentPeriod) {
			return <NoTreatPanel tbcase={this.props.tbcase} />;
		}

		const data = tbcase.treatment;

		if (!data) {
			return <WaitIcon type="card" />;
		}

		const optionsBtn = (
			<DropdownButton className="lnk-muted" bsStyle="link"
				title={<Fa icon="cog" />} id="ttmenu" pullRight
				onSelect={this.menuClick}>
				<MenuItem eventKey={1}>{__('Regimen.add')}</MenuItem>
				<MenuItem eventKey={2}>{__('cases.regimens.change')}</MenuItem>
				<MenuItem eventKey={3}>{__('cases.treat.undo')}</MenuItem>
				<MenuItem eventKey={4}>{'Add medicine'}</MenuItem>
			</DropdownButton>
			);

		return (
			<div>
				<Card title={__('cases.details.treatment')}>
					<Grid fluid>
						<Row>
							<Col md={6}>
								<Form doc={data} schema={this.state.sc1} readOnly />
							</Col>
							<Col md={6}>
								<TreatProgress value={data.progress}/>
							</Col>
						</Row>
					</Grid>
				</Card>

				<Card title={__('cases.details.treatment.prescmeds')} headerRight={optionsBtn}>
					<TreatTimeline treatment={data} />
				</Card>

				<TreatFollowup treatment={data} tbcase={this.props.tbcase} />
			</div>
			);
	}
}

CaseTreatment.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
