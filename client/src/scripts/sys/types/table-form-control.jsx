
import React from 'react';
import Form from '../../forms/form';
import { Col, ButtonToolbar, Button, Alert } from 'react-bootstrap';
import Fa from '../../components/fa';
import ReactTable from '../../components/react-table';
import msgs from '../../commons/messages';

import './table-form-control.less';

/**
 * Used in the Form library. Provide input data of string and number types
 */
export default class TableFormControl extends React.Component {

    static typeName() {
        return 'tableForm';
    }

    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.state = { errorsarr: [] };
        this.addRow = this.addRow.bind(this);
        this.remRow = this.remRow.bind(this);
    }

    /**
     * Called when user changes the value in the control
     * @return {[type]} [description]
     */
    onChange() {
        const sc = this.props.schema;
        const value = this.props.value;

        this.props.onChange({ schema: sc, value: value });
    }

    /**
     * Set the component focus
     * @return {[type]} [description]
     */
    focus() {

    }

    validate() {
        const sc = this.props.schema;
        const values = this.props.value;

        if (sc.min && values.length < sc.min) {
            const msg = msgs.minListQtt(sc.min);
            this.setState({ qttError: msg });
            return msg;
        }

        if (sc.max && values.length > sc.max) {
            const msg = msgs.maxListQtt(sc.max);
            this.setState({ qttError: msg });
            return msg;
        }

        this.setState({ qttError: null });

        var i;
        var errors = null;
        for (i = 0; i < this.props.value.length; i++) {
            const e = this.state.errorsarr;
            e[i] = this.refs['form' + i].validate();
            if (e[i]) {
                errors = e[i];
            }
        }

        this.forceUpdate();
        return errors;
    }

    addRow() {
        const sc = this.props.schema;

        const values = this.props.value ? this.props.value.slice(0) : [];
        values.push({});

        this.props.onChange({ schema: sc, value: values });
    }

    remRow() {
        if (!this.props.value) {
            return;
        }

        const sc = this.props.schema;
        const values = this.props.value.slice(0);
        values.pop();

        this.props.onChange({ schema: sc, value: values });
    }

    contentRender() {
        var rowsQuantity = this.props.value ? this.props.value.length : 0;
        var i;

        var rows = [];
        for (i = 0; i < rowsQuantity; i++) {
            rows[i] = this.getNewRow(i);
        }

        return (<div className="table-form">{rows}</div>);
    }

    getNewRow(key) {
        const sc = this.props.schema;

        if (key < 0) {
            return null;
        }

        return (
            <div key={key} className="row tbl-form-row">
                <Col sm={12}>
                    <Form ref={'form' + key}
                        schema={sc.fschema}
                        key={key}
                        doc={this.props.value[key]}
                        onChange={this.onChange}
                        errors={this.state.errorsarr[key]}
                        resources={this.state.resources} />
                </Col>
            </div>
        );
    }

    render() {
        const sc = this.props.schema;

        if (sc.readOnly) {
            if (!this.props.value) {
                return <Alert className="mtop" bsStyle="warning">{__('form.norecordfound')}</Alert>;
            }

            return (<ReactTable columns={sc.readOnlyColumns} values={this.props.value} />);
        }

        if (!sc.fschema) {
            return null;
        }

        return (
            <div>
                {
                    this.contentRender()
                }
                {
                    <div className="form-group has-error">
                        <div className="help-block">{this.state.qttError}</div>
                    </div>
                }
                <ButtonToolbar className={'def-margin-bottom'}>
                    <Button onClick={this.addRow}>
                        <Fa icon="plus"/>
                    </Button>

                    <Button onClick={this.remRow}
                        disabled={!this.props.value || this.props.value.length <= 0}>
                        <Fa icon="minus"/>
                    </Button>
                </ButtonToolbar>
            </div>
            );
    }

}

TableFormControl.propTypes = {
    value: React.PropTypes.array,
    schema: React.PropTypes.object,
    onChange: React.PropTypes.func,
    resources: React.PropTypes.any,
    ctitles: React.PropTypes.array,
    errors: React.PropTypes.any
};
