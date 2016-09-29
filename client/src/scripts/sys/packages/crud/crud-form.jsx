
import React from 'react';
import { Collapse } from 'react-bootstrap';
import { FormDialog } from '../../../components';
import controlWrapper from './crud-control-wrapper';

class CrudForm extends React.Component {

    componentWillMount() {
        this.setState({ visible: this.props.controller.isFormOpen() });
    }

    eventHandler(evt, data) {
        if (evt !== 'open-form' && evt !== 'close-form') {
            return;
        }

        const onnew = this.props.openOnNew;

        // ignore events if not on new forms
        if (!onnew || (onnew && data && data.id)) {
            return;
        }

        this.setState({ visible: evt === 'open-form' });
    }


    render() {
        if (!this.state.visible) {
            return null;
        }

        const controller = this.props.controller;
        const schema = controller.getFormSchema();

        const frm = (
            <FormDialog schema={schema}
                modalShow
                doc={controller.frm.doc}
                onConfirm={controller.saveAndClose}
                wrapType={this.props.wrapType}
                onCancel={controller.closeForm}
                className={this.props.className}
                />
            );

        const animate = this.props.animate && this.props.wrapType !== 'modal';

        return animate ?
            <Collapse in transitionAppear>
                {frm}
            </Collapse> :
            frm;
    }
}

CrudForm.propTypes = {
    schema: React.PropTypes.object,
    controller: React.PropTypes.object.isRequired,
    openOnNew: React.PropTypes.bool,
    openOnEdit: React.PropTypes.bool,
    wrapType: React.PropTypes.oneOf(['modal', 'card', 'none']),
    className: React.PropTypes.string,
    animate: React.PropTypes.bool
};

CrudForm.defaultProps = {
    wrapType: 'card',
    animate: true
};

export default controlWrapper(CrudForm);
