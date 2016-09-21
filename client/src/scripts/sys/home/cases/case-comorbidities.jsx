import React from 'react';
import { Badge, Row, Col, Button } from 'react-bootstrap';
import CaseComments from './case-comments';
import { Card, Fa, FormDialog } from '../../../components';
import { server } from '../../../commons/server';

const fschema = {
    title: __('cases.comorbidities'),
    controls: [
        {
            property: 'alcoholExcessiveUse',
            type: 'yesNo',
            label: __('TbCase.alcoholExcessiveUse'),
            size: { md: 12 }
        },
        {
            property: 'tobaccoUseWithin',
            type: 'yesNo',
            label: __('TbCase.tobaccoUseWithin'),
            size: { md: 12 }
        },
        {
            property: 'aids',
            type: 'yesNo',
            label: __('TbCase.aids'),
            size: { md: 12 }
        },
        {
            property: 'diabetes',
            type: 'yesNo',
            label: __('TbCase.diabetes'),
            size: { md: 12 }
        },
        {
            property: 'anaemia',
            type: 'yesNo',
            label: __('TbCase.anaemia'),
            size: { md: 12 }
        },
        {
            property: 'malnutrition',
            type: 'yesNo',
            label: __('TbCase.malnutrition'),
            size: { md: 12 }
        }
    ]
};

export default class CaseComorbidities extends React.Component {

    constructor(props) {
        super(props);
        this.save = this.save.bind(this);
        this.showForm = this.showForm.bind(this);
        this.headerRender = this.headerRender.bind(this);

        this.state = { showForm: false };
    }

    componentWillMount() {
        const tbcase = this.props.tbcase;

        // create data for UI controlling
        const trueOnes = fschema.controls.filter(item => tbcase[item.property]);

        // create doc
        const doc = {};
        trueOnes.forEach(item => { doc[item.property] = true; });

        this.setState({ uidata: trueOnes, doc: doc });
    }

    headerRender() {
        let size = 12;
        const hasPerm = true;

        let btn;
        if (hasPerm && this.state.showForm === false) {
            btn = <Button onClick={this.showForm(true)}><Fa icon="pencil"/>{__('action.edit')}</Button>;
            size = 10;
        }
        else {
            btn = null;
        }

        return (
            <Row>
                <Col sm={size}>
                    {__('TbField.COMORBIDITY')} <Badge className="tbl-counter">{this.state.uidata.length > 0 ? this.state.uidata.length : '-'}</Badge>
                </Col>
                {
                    btn && <Col sm={2}><div className="pull-right">{btn}</div></Col>
                }
            </Row>
            );
    }

    showForm(state) {
        const self = this;
        return (() => self.setState({ showForm: state }));
    }

    save() {
        return this.update(this.props.tbcase.id, this.state.doc).then(() => {
            const uidata = [];
            fschema.controls.forEach(item => {
                if (this.state.doc[item.property]) {
                    uidata.push(item);
                }
            });
            this.setState({ uidata: uidata, showForm: false });
        });
    }

    update(id, data) {
        return server.post('/api/cases/case/comorbidity/' + id, data)
        .then(res => {
            if (!res.success) {
                return Promise.reject(res.errors);
            }
            return res.result;
        });
    }

    render() {
        const tbcase = this.props.tbcase;
        const data = this.state.uidata;

        // put 3 or less cols in each row
        const rows = [];
        let cols = [];
        data.forEach((item, index) => {
            cols.push(
                <Col md={6} key={item.field + '' + index}>
                    <Card>
                        <span>
                            <Fa icon={'check'} />
                            <span>{item.label}</span>
                        </span>
                    </Card>
                </Col>);

            if (cols.length === 3 || index === data.length - 1) {
                rows.push(
                    <Row key={index}>
                        {
                            cols
                        }
                    </Row>
                    );
                cols = [];
            }
        });

        return (
                <span>
                    <FormDialog
                        schema={fschema}
                        doc={this.state.doc}
                        onConfirm={this.save}
                        onCancel={this.showForm(false)}
                        wrapType={'modal'}
                        modalShow={this.state.showForm}/>

                    <CaseComments
                        tbcase={tbcase} group="COMORBIDITIES">
                        <Card header={this.headerRender()} padding="combine">
                            {
                                rows
                            }
                        </Card>
                    </CaseComments>
                </span>
            );
    }
}

CaseComorbidities.propTypes = {
    tbcase: React.PropTypes.object
};
