import React from 'react';
import CrudView from '../packages/crud/crud-view';
import CaseComments from './case-comments';
import CRUD from '../../commons/crud';
import Form from '../../forms/form';
import moment from 'moment';
import { isEmpty } from '../../commons/utils';
import { getOptionName, getOptionList } from '../mock-option-lists';


const crud = new CRUD('prevtreat');

const readOnlySchema = {
    controls: [
        {
            property: 'am',
            type: 'yesNo',
            label: __('cases.prevtreat.am'),
            size: { sm: 2 },
            visible: i => i.am
        },
        {
            property: 'cfz',
            type: 'yesNo',
            label: __('cases.prevtreat.cfz'),
            size: { sm: 2 },
            visible: i => i.cfz
        },
        {
            property: 'cm',
            type: 'yesNo',
            label: __('cases.prevtreat.cm'),
            size: { sm: 2 },
            visible: i => i.cm
        },
        {
            property: 'cs',
            type: 'yesNo',
            label: __('cases.prevtreat.cs'),
            size: { sm: 2 },
            visible: i => i.cs
        },
        {
            property: 'e',
            type: 'yesNo',
            label: __('cases.prevtreat.e'),
            size: { sm: 2 },
            visible: i => i.e
        },
        {
            property: 'eto',
            type: 'yesNo',
            label: __('cases.prevtreat.eto'),
            size: { sm: 2 },
            visible: i => i.eto
        },
        {
            property: 'h',
            type: 'yesNo',
            label: __('cases.prevtreat.h'),
            size: { sm: 2 },
            visible: i => i.h
        },
        {
            property: 'lfx',
            type: 'yesNo',
            label: __('cases.prevtreat.lfx'),
            size: { sm: 2 },
            visible: i => i.lfx
        },
        {
            property: 'ofx',
            type: 'yesNo',
            label: __('cases.prevtreat.ofx'),
            size: { sm: 2 },
            visible: i => i.ofx
        },
        {
            property: 'r',
            type: 'yesNo',
            label: __('cases.prevtreat.r'),
            size: { sm: 2 },
            visible: i => i.r
        },
        {
            property: 's',
            type: 'yesNo',
            label: __('cases.prevtreat.s'),
            size: { sm: 2 },
            visible: i => i.s
        },
        {
            property: 'z',
            type: 'yesNo',
            label: __('cases.prevtreat.z'),
            size: { sm: 2 },
            visible: i => i.z
        }
    ]
};

export default class CasePrevTbTreats extends React.Component {

    constructor(props) {
        super(props);
        this.cellRender = this.cellRender.bind(this);

        this._editorSchema = {
            defaultProperties: {
                tbcaseId: props.tbcase.id
            },
            controls: [
                {
                    type: 'period',
                    property: 'period',
                    label: __('cases.treat'),
                    size: { sm: 12 },
                    required: true
                },
                {
                    type: 'select',
                    label: __('cases.prevtreat.outcome'),
                    property: 'outcome',
                    options: getOptionList('prevTbTreatOutcome'),
                    size: { sm: 12 },
                    required: true
                },
                {
                    type: 'subtitle',
                    label: __('cases.prevtreat.substances'),
                    size: { sm: 12 }
                },
                {
                    property: 'r',
                    type: 'bool',
                    label: __('cases.prevtreat.r'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'h',
                    type: 'bool',
                    label: __('cases.prevtreat.h'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 's',
                    type: 'bool',
                    label: __('cases.prevtreat.s'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'e',
                    type: 'bool',
                    label: __('cases.prevtreat.e'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'z',
                    type: 'bool',
                    label: __('cases.prevtreat.z'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'am',
                    type: 'bool',
                    label: __('cases.prevtreat.am'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'cfz',
                    type: 'bool',
                    label: __('cases.prevtreat.cfz'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'cm',
                    type: 'bool',
                    label: __('cases.prevtreat.cm'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'cs',
                    type: 'bool',
                    label: __('cases.prevtreat.cs'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'eto',
                    type: 'bool',
                    label: __('cases.prevtreat.eto'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'lfx',
                    type: 'bool',
                    label: __('cases.prevtreat.lfx'),
                    size: { sm: 2 },
                    defaultValue: false
                },
                {
                    property: 'ofx',
                    type: 'bool',
                    label: __('cases.prevtreat.ofx'),
                    size: { sm: 2 },
                    defaultValue: false
                }
            ],
            title: doc => doc && doc.id ? __('cases.prevtreat.edt') : __('cases.prevtreat.new')
        };
    }

    cellRender(item) {
        const title = moment().month(item.month - 1).format('MMM') + '-' + item.year +
            (item.month ? ' ' + __('global.until') + ' ' +
                moment().month(item.outcomeMonth).format('MMM') + '-' + item.outcomeYear : '');

        return (
            <span>
                <div>
                    <div className="label label-default">
                    {getOptionName('prevTbTreatOutcome', item.outcome)}
                    </div>
                    <div className="text-muted">{title}</div>
                </div>
                <hr/>
                <Form readOnly schema={readOnlySchema} doc={item} />
            </span>
        );
    }

    render() {
        const tbcase = this.props.tbcase;

        return (
            <CaseComments
                tbcase={tbcase} group="PREV_TREATS">
                <CrudView combine modal
                    cellSize={{ md: 12 }}
                    title={__('cases.prevtreat')}
                    editorSchema={this._editorSchema}
                    crud={crud}
                    onCellRender={this.cellRender}
                    queryFilters={{ tbcaseId: tbcase.id }}
                    refreshAll
                    perm={'CASEMAN'}
                    />
            </CaseComments>
            );
    }
}

CasePrevTbTreats.propTypes = {
    tbcase: React.PropTypes.object
};
