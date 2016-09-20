import React from 'react';
import Form from '../forms/form';
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
export default class RemoteForm extends React.Component {

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

        if (this.props.remoteFormMounted) {
            this.props.remoteFormMounted(res.title);
        }

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
            <Form {...props} />
        );
    }
}

RemoteForm.propTypes = {
    // the location in the server where form is located
    remotePath: React.PropTypes.any.isRequired,
    remoteFormMounted: React.PropTypes.func,

    // Form props
    errors: React.PropTypes.array,
    onChange: React.PropTypes.func,
    readOnly: React.PropTypes.bool,
    className: React.PropTypes.string
};
