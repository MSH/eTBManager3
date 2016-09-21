import React from 'react';
import CrudView from '../../crud/crud-view';
import CaseComments from './case-comments';
import CRUD from '../../../commons/crud';
import { Profile } from '../../../components';

const crud = new CRUD('sideeffect');

export default class CaseAdvReact extends React.Component {

    constructor(props) {
        super(props);

        // form fields for contacts
        const editorSchema = {
            defaultProperties: {
                tbcaseId: props.tbcase.id
            },
            controls: [
                {
                    type: 'select',
                    label: __('cases.sideeffects.desc'),
                    property: 'sideEffect',
                    required: true,
                    options: [
                        { id: 'adv1', name: 'Headache' },
                        { id: 'adv2', name: 'Constirpação' },
                        { id: 'adv3', name: 'Dor na coluna' },
                        { id: 'adv4', name: 'Febre interna' }
                    ],
                    size: { sm: 12 }
                },
                {
                    type: 'select',
                    property: 'month',
                    label: __('cases.sideeffects.month'),
                    required: true,
                    options: { from: 1, to: 24 },
                    size: { sm: 6 }
                },
                {
                    type: 'select',
                    label: __('cases.sideeffects.medicine'),
                    property: 'substanceId',
                    options: 'substances',
                    size: { sm: 12 }
                },
                {
                    type: 'select',
                    label: __('cases.sideeffects.medicine'),
                    property: 'substance2Id',
                    options: 'substances',
                    size: { sm: 12 }
                },
                {
                    type: 'text',
                    property: 'comment',
                    label: __('global.comments'),
                    size: { sm: 12 }
                }
            ]
        };

        this.state = { editorSchema: editorSchema };
    }

    cellRender(item) {
        const subtitle = <div><b>{__('cases.sideeffects.month') + ': '}</b>{item.month}</div>;
        return (
            <Profile title={item.sideEffect} subtitle={subtitle} size="small" />
        );
    }

    collapseCellRender(item) {
        const ret = (<div>
                        <hr/>
                        <dl className="dl-horizontal">
                            <dt>{__('cases.sideeffects.medicine') + ':'}</dt>
                            <dd>{item.medicines ? item.medicines : '-'}</dd>
                            <dt>{__('global.comments') + ':'}</dt>
                            <dd>{item.comment ? item.comment : '-'}</dd>
                        </dl>
                        <hr/>
                    </div>);

        return (ret);
    }

    render() {
        const tbcase = this.props.tbcase;

        return (
            <CaseComments
                tbcase={tbcase} group="ADV_REACTS">
                <CrudView combine modal
                    title={__('cases.sideeffects')}
                    editorSchema={this.state.editorSchema}
                    crud={crud}
                    cellSize={{ md: 12 }}
                    onCellRender={this.cellRender}
                    onDetailRender={this.collapseCellRender}
                    queryFilters={{ tbcaseId: tbcase.id }}
                    />
            </CaseComments>
            );
    }
}

CaseAdvReact.propTypes = {
    tbcase: React.PropTypes.object
};
