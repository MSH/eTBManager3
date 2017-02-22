import React from 'react';
import { Card, Fa } from '../../components';
import SessionUtils from '../session-utils';

import moment from 'moment';

export default class OtherCases extends React.Component {

    renderOtherCase(data, index) {
        if (this.props.tbcase.id === data.id) {
            return null;
        }

        return (
            <a key={index} href={SessionUtils.caseHash(data.id)} className="other-cases">
                <div className="content">
                    <div>{SessionUtils.classifDisplay(data.classification.id, data.diagnosisType.id)}</div>
                    <div className="text-muted text-small">
                        {__('TbCase.registrationDate') + ': '}
                        {moment(data.registrationDate).format('L')}
                    </div>
                    <div>
                        {SessionUtils.caseStateDisplay(data.state)}
                    </div>
                </div>
            </a>
        );
    }

    render() {
        const tbcase = this.props.tbcase;

        // don't have other cases
        if (tbcase.allCases.length <= 1) {
            return (
                <Card title={__('cases.details.others')}>
                    <div className="message-muted">
                        <Fa icon="book" />
                        <div>{__('cases.details.othercases.noresult')}</div>
                    </div>
                </Card>
            );
        }

        return (<Card title={__('cases.details.others')}>
                    {
                        tbcase.allCases.map((item, i) => this.renderOtherCase(item, i))
                    }
                </Card>);
    }
}


OtherCases.propTypes = {
    tbcase: React.PropTypes.object
};
