import React from 'react';
import Form from '../../../forms/form';
import { Card, Fa } from '../../../components';
import { OverlayTrigger, Tooltip, Col, Row } from 'react-bootstrap';

import moment from 'moment';
import { getSchema } from './followup-display-schemas';

export default class FollowupDisplay extends React.Component {

	renderButtons() {
		return (<div className="mtop-2x">
					<a className="lnk-muted" onClick={this.props.onEdit}><Fa icon="pencil"/>{__('action.edit')}</a>
					<OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
						<a className="lnk-muted" onClick={this.props.onDelete}><Fa icon="trash-o"/></a>
					</OverlayTrigger>
				</div>);
	}

	renderHeader() {
		const followup = this.props.followup;

		const schema = getSchema(followup.type.id.toLowerCase());
		const doc = followup.data;

		if (!schema || !doc) {
			return null;
		}

		var datefield = 'dateCollected';
		if (followup.type.id === 'MEDEXAM' || followup.type.id === 'HIV' || followup.type.id === 'XRAY') {
			datefield = 'date';
		}

		var month = ' - ';
		month = month + followup.type.monthOfTreatment;

		const subtitle = (<div>
							{
								moment(doc[datefield]).format('ll')
							}
							{
								followup.type.monthOfTreatment && <span>{month}</span>
							}
						</div>);

		const header = (<Row className="profile profile-medium">
							<Col sm={10}>
								<div className="profile-image">
									<Fa icon={followup.type.id === 'MEDEXAM' ? 'stethoscope' : 'file-text'} />
								</div>
								<div className="profile-title">{followup.type.name}</div>
								<div className="profile-subtitle">{subtitle}</div>
							</Col>
							<Col sm={2}>
								<div className="text-right">{this.renderButtons()}</div>
							</Col>
							<Col sm={12}>
								<hr/>
							</Col>
						</Row>
						);

		return header;
	}

	render() {
		const followup = this.props.followup;

		const schema = getSchema(followup.type.id.toLowerCase());
		const doc = followup.data;

		if (!schema || !doc) {
			return null;
		}

		return (
			<Card header={this.renderHeader()}>
				<Form schema={schema} doc={doc} readOnly/>
			</Card>);
	}
}

FollowupDisplay.propTypes = {
	followup: React.PropTypes.object.isRequired,
	onEdit: React.PropTypes.func,
	onDelete: React.PropTypes.func
};
