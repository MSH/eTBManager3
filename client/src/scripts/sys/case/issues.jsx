import React from 'react';
import { FormDialog } from '../../components';
import IssueCard from './issue-card';

// definition of the form fields to edit tags
const issueEditorDef = {
    controls: [
        {
            property: 'title',
            required: true,
            type: 'string',
            max: 200,
            label: __('Issue.title'),
            size: { md: 12 }
        },
        {
            property: 'description',
            type: 'text',
            required: true,
            label: __('global.description'),
            size: { md: 12 }
        }
    ],
    title: __('cases.issues.new')
};

export default class Issues extends React.Component {

    constructor(props) {
        super(props);
        this.onIssueEvent = this.onIssueEvent.bind(this);
        this.modalClose = this.modalClose.bind(this);
        this.edtConfirm = this.edtConfirm.bind(this);

        this.state = { edtItem: null, doc: {} };
    }

    onIssueEvent(evt, issue, doc) {
        if (evt === 'openedt') {
            this.setState({ edtItem: issue, doc: { title: issue.title, description: issue.description } });
            return null;
        }

        return this.props.onIssueEvent(evt, issue, doc);
    }

    edtConfirm() {
        this.props.onIssueEvent('edit', this.state.edtItem, this.state.doc);
        this.modalClose();
    }

    modalClose() {
        this.setState({ edtItem: null, doc: {} });
    }

    render() {
        const issues = this.props.issues;

        // shown if no issues is registered
        if (!issues || issues.length === 0) {
            return null;
        }

        return (
            <div>
                {
                    issues.map((issue) => (<IssueCard key={issue.id} issue={issue} onIssueEvent={this.onIssueEvent}/>))
                }

                <FormDialog
                    schema={issueEditorDef}
                    doc={this.state.doc}
                    onCancel={this.modalClose}
                    onConfirm={this.edtConfirm}
                    wrapType={'modal'}
                    modalShow={!!this.state.edtItem} />
            </div>
            );
    }
}


Issues.propTypes = {
    issues: React.PropTypes.array,
    /**
     * Possible events: edit, remove, reopen, close
     */
    onIssueEvent: React.PropTypes.func
};
