import React from 'react';
import { Label } from 'react-bootstrap';
import { Card, Profile, Fa } from '../../components';
import SessionUtils from '../session-utils';

import moment from 'moment';

export default class OtherCases extends React.Component {

    renderOtherCase(data, index) {
        if (this.props.tbcase.id === data.id) {
            return null;
        }

        const title = SessionUtils.classifDisplay(data.classification.id, data.diagnosisType.id);

        const subtitle = (
                <div>
                    <div>
                        {__('TbCase.registrationDate') + ': '}
                        {moment(data.registrationDate).format('L')}
                    </div>
                    <div><Fa icon="hospital-o"/>{data.ownerUnit.name}</div>
                </div>
            );

        const state = data.state.id !== 'CLOSED' ? <Label bsStyle="danger" >{data.state.name}</Label> : data.state.name;

        return (
            <div className="other-case">
                <a href={SessionUtils.caseHash(data.id)} key={index} className="mbottom-2x">
                    <div className="pull-right text-muted">
                        {state}
                    </div>
                    <Profile
                        title={title}
                        subtitle={subtitle}
                        size="small"/>
                </a>
            </div>
        );
    }

    render() {
        const tbcase = this.props.tbcase;

        // don't have other cases
        if (tbcase.allCases.length < 2) {
            return null;
        }

        const otherCases = tbcase.allCases.map((item, i) => this.renderOtherCase(item, i));

        return (<Card title="Other Cases">{otherCases}</Card>);
    }
}


OtherCases.propTypes = {
    tbcase: React.PropTypes.object
};
