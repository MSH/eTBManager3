import React from 'react';
import Form from '../../forms/form';
import { Card, Fa } from '../../components';
import { OverlayTrigger, Tooltip, Col, Row } from 'react-bootstrap';

import moment from 'moment';
import { getDisplaySchema, getFollowUpType } from './followup-utils';

export default class FollowupDisplay extends React.Component {

    constructor(props) {
        super(props);
    }

    renderButtons() {
        return (
            <div className="pull-right">
                <a className="lnk-muted" onClick={this.props.onEdit}><Fa icon="pencil"/>{__('action.edit')}</a>
                <OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
                    <a className="lnk-muted" onClick={this.props.onDelete}><Fa icon="trash-o"/></a>
                </OverlayTrigger>
            </div>
            );
    }

    renderHeader() {
        const followup = this.props.followup;
        const followUpType = getFollowUpType(followup.type);

        const schema = getDisplaySchema(followup.type);
        const doc = followup.data;

        if (!schema || !doc) {
            return null;
        }

        var month = ' - ';
        month = month + followup.monthOfTreatment;

        const subtitle = (<div>
                            {
                                moment(doc.date).format('ll')
                            }
                            {
                                followup.monthOfTreatment && <span>{month}</span>
                            }
                        </div>);

        const header = (
            <Row className="profile profile-medium">
                <Col sm={10}>
                    <div className="profile-image">
                        <Fa icon={followUpType.icon} />
                    </div>
                    <div className="profile-title">{followup.name}</div>
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

        console.log(followup);

        const schema = getDisplaySchema(followup.type);
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
