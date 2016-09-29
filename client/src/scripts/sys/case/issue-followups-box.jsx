import React from 'react';
import { OverlayTrigger, Tooltip, Button } from 'react-bootstrap';
import { Profile, Fa, FormDialog, AutoheightInput } from '../../components';
import moment from 'moment';
import { app } from '../../core/app';

// definition of the form fields to edit tags
const issueEditorDef = {
    controls: [
        {
            property: 'text',
            type: 'text',
            required: true,
            label: __('Issue.answer'),
            size: { md: 12 }
        }
    ],
    title: __('Issue.edtanswer')
};

export default class IssueFollowUpsBox extends React.Component {

    constructor(props) {
        super(props);
        this.addAnswer = this.addAnswer.bind(this);
        this.editClick = this.editClick.bind(this);
        this.modalClose = this.modalClose.bind(this);
        this.editConfirm = this.editConfirm.bind(this);
        this.textChange = this.textChange.bind(this);

        this.state = { disabled: true, doc: {} };
    }

    /**
     * Called when user adds a new comment
     */
    addAnswer() {
        const txt = this.refs.input.getText();
        this.props.onEvent('add', txt);
        this.refs.input.setText('');
    }

    /**
     * Return a function to be called when user clicks on the edit link
     * @param  {[type]} item [description]
     * @return {[type]}      [description]
     */
    editClick(item) {
        const self = this;
        return () => {
            self.setState({ edtitem: item, doc: { text: item.text } });
        };
    }

    /**
     * Called when user clicks on the close link
     * @return {[type]} [description]
     */
    modalClose() {
        this.setState({ edtitem: null });
    }

    /**
     * Called when user confirms the changed text in the dialog box
     * @return {[type]} [description]
     */
    editConfirm() {
        const item = this.state.edtitem;
        this.props.onEvent('edit', item, this.state.doc.text);
        this.modalClose();
    }

    /**
     * Return a function to be called when user clicks on the remove link
     * @param  {[type]} item [description]
     * @return {[type]}      [description]
     */
    removeClick(item) {
        return () => app.messageDlg({
            message: __('form.confirm_remove'),
            style: 'warning',
            type: 'YesNo'
        }).then(res => {
            if (res === 'yes') {
                this.props.onEvent('remove', item);
            }
        });
    }

    /**
     * Called when the text of the input box is changed. Enable or disable the add button
     */
    textChange() {
        this.setState({ disabled: !this.refs.input.getText().trim() });
    }

    followUpsRender(followups) {
        if (!followups || followups.length === 0) {
            return null;
        }

        return (followups.map(followup => (
            <div className="media" key={followup.id}>
                <div className="media-left">
                    <Profile type="user" size="small"/>
                </div>
                <div className="media-body">
                    <div className="pull-right">
                        {followup.id.indexOf('fakeid') < 0 && followup.id.indexOf('error') < 0 &&
                            <span>
                                <a className="lnk-muted" onClick={this.editClick(followup)}><Fa icon="pencil"/>{__('action.edit')}</a>
                                <OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
                                    <a className="lnk-muted" onClick={this.removeClick(followup)}><Fa icon="remove"/></a>
                                </OverlayTrigger>
                            </span>
                        }
                        {followup.id.indexOf('fakeid') >= 0 &&
                            <span className="lnk-muted">
                                <Fa icon="circle-o-notch" spin/>
                                {__('global.saving')}
                            </span>
                        }
                        {followup.id.indexOf('error') >= 0 &&
                            <span className="bs-error">
                                {__('global.errorsaving')}
                            </span>
                        }
                    </div>
                    <div className="text-muted"><b>{followup.user.name}</b>{' ' + __('global.wrotein') + ' '}<b>{moment(followup.followupDate).format('lll')}</b></div>
                    <div className="sub-text">{followup.unit.name}</div>
                    {followup.text.split('\n').map((item, index) =>
                        <span key={index}>
                            {item}
                            <br/>
                        </span>
                        )
                    }
                </div>
            </div>
            )));
    }

    render() {

        const followups = this.props.issue.followups;

        return (
                <div>

                    {
                        this.followUpsRender(followups)
                    }

                    {
                        this.props.issue.closed === false &&
                        <div className="mtop">
                            <AutoheightInput ref="input" onChange={this.textChange} />
                            <Button bsStyle="primary"
                                disabled={this.state.disabled}
                                onClick={this.addAnswer}
                                bsSize="small"
                                style={{ marginTop: '6px' }}>
                                    {__('Issue.answer')}
                            </Button>
                        </div>
                    }

                    <FormDialog
                        schema={issueEditorDef}
                        doc={this.state.doc}
                        onCancel={this.modalClose}
                        onConfirm={this.editConfirm}
                        wrapType={'modal'}
                        modalShow={!!this.state.edtitem} />

                </div>
                );
    }
}


IssueFollowUpsBox.propTypes = {
    issue: React.PropTypes.object.isRequired,
    /**
     * Possible events: add, edit, remove
     */
    onEvent: React.PropTypes.func
};
