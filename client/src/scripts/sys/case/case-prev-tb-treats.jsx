import React from 'react';
import CrudView from '../packages/crud/crud-view';
import CaseComments from './case-comments';
import CRUD from '../../commons/crud';
import Form from '../../forms/form';
import moment from 'moment';
import { getOptionName, getOptionList } from '../mock-option-lists';


var crud;

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
        crud = new CRUD('prevtreat');
        crud.fetchFormRequest = data => data.id ? data : Object.assign({}, data, { caseId: this.props.tbcase.id });
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
                    remoteForm
                    perm={'CASEMAN'}
                    />
            </CaseComments>
            );
    }
}

CasePrevTbTreats.propTypes = {
    tbcase: React.PropTypes.object
};
