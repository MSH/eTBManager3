import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Card, RemoteForm } from '../../../components';

import CasePrevTbTreats from './case-prev-tb-treats';
import CaseContacts from './case-contacts';
import CaseAdvReacts from './case-adv-reacts';
import CaseComorbidities from './case-comorbidities';
import CaseComments from './case-comments';

export default class CaseData extends React.Component {

    render() {
        const tbcase = this.props.tbcase;

        if (!tbcase) {
            return null;
        }

        const path = '/api/tbl/case/form/readonly/' + tbcase.id;

        return (
            <div>
                <Row>
                    <Col sm={12}>
                        <CaseComments tbcase={tbcase} group="DATA">
                            <Card padding="combine">
                                <RemoteForm
                                    remotePath={path}
                                    onLoadForm={this.props.onLoad}
                                    readOnly/>
                            </Card>
                        </CaseComments>
                    </Col>
                </Row>

                <Row>
                    <Col sm={6}>
                        <CaseAdvReacts tbcase={this.props.tbcase} />
                    </Col>
                    <Col sm={6}>
                        <CaseComorbidities tbcase={this.props.tbcase} />
                    </Col>
                </Row>

                <Row>
                    <Col sm={6}>
                        <CasePrevTbTreats tbcase={this.props.tbcase} />
                    </Col>
                    <Col sm={6}>
                        <CaseContacts tbcase={this.props.tbcase} />
                    </Col>
                </Row>
            </div>
            );
    }
}


CaseData.propTypes = {
    tbcase: React.PropTypes.object,
    onLoad: React.PropTypes.any
};
