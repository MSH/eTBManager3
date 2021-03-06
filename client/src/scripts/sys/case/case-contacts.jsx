import React from 'react';
import CrudView from '../packages/crud/crud-view';
import { Profile } from '../../components';
import CaseComments from './case-comments';
import CRUD from '../../commons/crud';
import Form from '../../forms/form';
import { getOptionName, getOptionList } from '../mock-option-lists';
import moment from 'moment';

const crud = new CRUD('contact');

export default class CaseContacts extends React.Component {

    constructor(props) {
        super(props);
        this.cellRender = this.cellRender.bind(this);
        this.collapseCellRender = this.collapseCellRender.bind(this);

        const editorSchema = {
            defaultProperties: {
                tbcaseId: props.tbcase.id
            },
            controls: [
                {
                    type: 'string',
                    label: __('CaseContact.name'),
                    property: 'name',
                    required: true,
                    max: 100,
                    size: { sm: 12 }
                },
                {
                    type: 'select',
                    property: 'gender',
                    label: __('Gender'),
                    required: true,
                    options: [
                        { id: 'MALE', name: __('Gender.MALE') },
                        { id: 'FEMALE', name: __('Gender.FEMALE') }
                    ],
                    size: { sm: 6 }
                },
                {
                    type: 'number',
                    property: 'age',
                    label: __('TbCase.age'),
                    required: true,
                    size: { sm: 6 }
                },
                {
                    type: 'select',
                    property: 'contactType',
                    label: __('TbField.CONTACTTYPE'),
                    required: true,
                    options: getOptionList('contactType')
                },
                {
                    type: 'yesNo',
                    property: 'examinated',
                    label: __('CaseContact.examined'),
                    required: true,
                    size: { sm: 6 }
                },
                {
                    type: 'date',
                    property: 'dateOfExamination',
                    label: __('CaseContact.dateOfExamination'),
                    visible: value => value.examinated,
                    size: { sm: 6 }
                },
                {
                    type: 'select',
                    label: __('CaseContact.conduct'),
                    property: 'conduct',
                    options: getOptionList('contactConduct'),
                    size: { sm: 12 }
                },
                {
                    type: 'text',
                    label: __('global.comments'),
                    property: 'comments',
                    size: { sm: 12 }
                }
            ],
            title: doc => doc && doc.id ? __('case.casecontact.edt') : __('case.casecontact.new')
        };

        this.state = { editorSchema: editorSchema };
    }

    cellRender(item) {
        return (
            <Profile size="small" type={item.gender.toLowerCase()}
                title={item.name} subtitle={__('TbCase.age') + ': ' + item.age + ' - ' + getOptionName('contactType', item.contactType)} />
        );
    }

    collapseCellRender(item) {
        const YesNo = Form.types.yesNo;
        const ret = (<div>
                        <hr/>
                        <dl className="dl-horizontal">
                            <dt>{__('CaseContact.examined') + ':'}</dt>
                            <dd><YesNo value={item.examinated} noForm /></dd>
                            <dt>{__('CaseContact.dateOfExamination') + ':'}</dt>
                            <dd>{item.dateOfExamination ? moment(item.dateOfExamination).format('ll') : '-'}</dd>
                            <dt>{__('CaseContact.conduct') + ':'}</dt>
                            <dd>{item.conduct ? getOptionName('contactConduct', item.conduct) : '-'}</dd>
                            <dt>{__('global.comments') + ':'}</dt>
                            <dd>{item.comments ? item.comments : '-'}</dd>
                        </dl>
                        <hr/>
                    </div>);

        return (ret);
    }

    render() {
        const tbcase = this.props.tbcase;

        return (
            <CaseComments
                tbcase={tbcase} group="CONTACTS">
                <CrudView combine modal
                    cellSize={{ md: 12 }}
                    title={__('cases.contacts')}
                    editorSchema={this.state.editorSchema}
                    crud={crud}
                    onCellRender={this.cellRender}
                    onDetailRender={this.collapseCellRender}
                    queryFilters={{ tbcaseId: tbcase.id }}
                    perm={'CASECONTACT'}
                    />
            </CaseComments>
        );
    }
}

CaseContacts.propTypes = {
    tbcase: React.PropTypes.object
};
