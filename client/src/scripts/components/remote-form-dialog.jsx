import React from 'react';
import FormDialog from './form-dialog';
import WaitIcon from './wait-icon';
import { server } from '../commons/server';
import { isFunction } from '../commons/utils';

/**
 * Remote form - Load the schema, document and resources from a remote path.
 * It uses a FormDialog component to display a form that is loaded from the
 * server using the *remotePath* property, which is a string pointing to a
 * server resource (using GET) or a function that returns a promise to be
 * resolved with (schema, doc, resources)
 *
 */
export default class RemoteFormDialog extends React.Component {

    constructor(props) {
        super(props);
        this.resolveForm = this.resolveForm.bind(this);
    }

    componentWillMount() {
        this.loadForm(this.props.remotePath);
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.remotePath !== this.props.remotePath) {
            this.loadForm(nextProps.remotePath);
        }
    }

    /**
     * Load the data from the remote path
     */
    loadForm(remotePath) {
        this.setState({ fetching: true });

        const p = isFunction(remotePath) ? remotePath() : server.get(remotePath);
		p.then(this.resolveForm);
    }

    /**
     * Resolve a form by a given string containing java script code
     */
    resolveForm(data) {
		/* eslint no-new-func: "off" */
		const func = new Function('', 'return ' + data.schema + ';');

		const res = func();
        console.log(res);

        this.setState({ schema: res,
            doc: data.doc,
            resources: data.resources,
            fetching: false
        });
    }

    render() {
        if (this.state.fetching) {
            return <WaitIcon type="card" />;
        }

        // create the properties of the form dialog
        const props = Object.assign({}, this.props,
            {
                schema: this.state.schema,
                doc: this.state.doc,
                resources: this.state.resources
            });

        delete props.remotePath;

        return (
            <FormDialog {...props} />
        );
    }
}

RemoteFormDialog.propTypes = {
    // the location in the server where form is located
    remotePath: React.PropTypes.any.isRequired,

    // the properties used by FormDialog
	onConfirm: React.PropTypes.func,
	onCancel: React.PropTypes.func,
	onInit: React.PropTypes.func,
	confirmCaption: React.PropTypes.any,
	resources: React.PropTypes.object,
	wrapType: React.PropTypes.oneOf(['modal', 'card', 'none']),
	hideCancel: React.PropTypes.bool,
	className: React.PropTypes.string,
	modalShow: React.PropTypes.bool,
	modalBsSize: React.PropTypes.string
};
