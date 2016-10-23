import React from 'react';
import CrudView from '../packages/crud/crud-view';
import CaseComments from './case-comments';
import CRUD from '../../commons/crud';
import { Profile } from '../../components';
import { getOptionName, getOptionList } from '../mock-option-lists';

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
                    options: getOptionList('advReactions'),
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
            ],
            title: doc => doc && doc.id ? __('case.sideeffect.edt') : __('case.sideeffect.new')
        };

        this.state = { editorSchema: editorSchema };
    }

    cellRender(item) {
        const subtitle = <div><b>{__('cases.sideeffects.month') + ': '}</b>{item.month}</div>;

        return (
            <Profile title={getOptionName('advReactions', item.sideEffect)}
                subtitle={subtitle}
                size="small" />
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
                    perm={'ADV_EFFECTS'}
                    />
            </CaseComments>
            );
    }
}

CaseAdvReact.propTypes = {
    tbcase: React.PropTypes.object
};
