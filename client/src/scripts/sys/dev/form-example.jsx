
import React from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import { Card } from '../../components/index';
import Form from '../../forms/form';
import { app } from '../../core/app';


/**
 * Initial page that declare all routes of the module
 */
export default class ReacttableExample extends React.Component {

    /* eslint no-console: "off", no-alert: "off" */
    constructor(props) {
        super(props);
        this.onChangeDoc = this.onChangeDoc.bind(this);
        this.clearIt = this.clearIt.bind(this);
        this.validate = this.validate.bind(this);

        this.state = { doc: {} };
    }


    onChangeDoc(doc) {
        this.forceUpdate();
        // display object in the console
        console.log(doc);
    }

    validate() {
        const errors = this.refs.form.validate();
        this.setState({ errors: errors });
        if (!errors) {
            alert('Form is ok');
        }
    }

    clearIt() {
        this.setState({ doc: {} });
        this.forceUpdate();
    }


    render() {
        // THE FORM LAYOUT
        const schema = {
            controls: [
                {
                    property: 'name',
                    required: true,
                    type: 'string',
                    max: 100,
                    label: __('form.name'),
                    size: { sm: 6 }
                },
                {
                    property: 'level',
                    required: true,
                    type: 'select',
                    label: __('form.level'),
                    options: { from: 1, to: 5 },
                    size: { sm: 3 }
                },
                {
                    property: 'iniDate',
                    required: true,
                    type: 'date',
                    label: 'Start date',
                    size: { sm: 3, newLine: true }
                },
                {
                    property: 'adminUnitId',
                    type: 'adminUnit',
                    label: 'Administrative unit',
                    size: { sm: 3, newLine: true }
                },
                {
                    property: 'action1',
                    required: true,
                    type: 'listBox',
                    label: __('form.action'),
                    options: app.getState().app.lists.CommandAction,
                    size: { md: 3 },
                    vertical: true,
                    textAlign: 'left'
                },
                {
                    property: 'action2',
                    required: false,
                    type: 'listBox',
                    label: __('form.action'),
                    options: app.getState().app.lists.CommandAction,
                    size: { md: 6 }
                }
            ]
        };

        return (
            <div>
                <Card title="Reactive table">
                    <Row>
                        <Col md={12}>
                            <Form ref="form"
                                schema={schema}
                                doc={this.state.doc}
                                onChange={this.onChangeDoc}
                                errors={this.state.errors} />
                            <Button bsStyle="primary"
                                onClick={this.validate}>{'Validate'}</Button>
                        </Col>
                    </Row>
                </Card>
                <Card title="Document">
                    <div>
                    {JSON.stringify(this.state.doc, null, '    ')}
                    </div>
                    <Button onClick={this.clearIt}>{'Clear it'}</Button>
                </Card>
                {
                    this.state.errors ?
                    <Card title="Errors">
                        {JSON.stringify(this.state.errors, null, '    ')}
                    </Card> : null
                }
            </div>
        );
    }
}
