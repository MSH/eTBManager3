import React from 'react';
import CommentsBox from './comments-box';
import CRUD from '../../../commons/crud';
import { app } from '../../../core/app';

const crud = new CRUD('casecomment');

export default class CaseComments extends React.Component {

    constructor(props) {
        super(props);
        this.onCommentEvent = this.onCommentEvent.bind(this);
    }


    onCommentEvent(evt, data, txt) {
        switch (evt) {
            case 'add': return this.addComment(data);
            case 'remove': return this.removeComment(data);
            case 'edit': return this.editComment(data, txt);
            default: throw new Error('Invalid ' + evt);
        }
    }

    addComment(txt) {
        // add the new comment on UI
        const newComment = {
            id: 'fakeid-' + this.props.tbcase.comments.length,
            user: {
                id: app.getState().session.userId,
                name: app.getState().session.userName
            },
            group: this.props.group,
            comment: txt
        };

        this.props.tbcase.comments.push(newComment);
        this.forceUpdate();

        // create the new comment on database
        const doc = { comment: txt, group: this.props.group, tbcaseId: this.props.tbcase.id };
        crud.create(doc)
        .then(id => {
            // updates new comment id, so edit and delete should work.
            newComment.id = id;
            this.forceUpdate();

        })
        .catch(() => {
            newComment.id = 'error-' + this.props.tbcase.comments.length;
            this.forceUpdate();
        });
    }

    removeComment(item) {
        // removes the comment from UI
        const lst = this.props.tbcase.comments;
        const index = lst.indexOf(item);
        this.props.tbcase.comments.splice(index, 1);
        this.forceUpdate();

        // delete the new comment on database
        crud.delete(item.id);
    }

    editComment(item, text) {
        // refresh the comment on UI
        item.comment = text;
        this.forceUpdate();

        const doc = {};
        doc.comment = text;
        doc.group = this.props.group;

        // update the new comment on database
        crud.update(item.id, doc);
    }


    render() {
        const group = this.props.group;
        const comments = this.props.tbcase.comments ?
            this.props.tbcase.comments.filter(item => item.group === group) :
            null;

        return (
            <div>
                {
                    this.props.children
                }
                <CommentsBox values={comments} onEvent={this.onCommentEvent}/>
            </div>
            );
    }

}

CaseComments.propTypes = {
    tbcase: React.PropTypes.object,
    // name of the group in the list of comments
    group: React.PropTypes.string,
    children: React.PropTypes.node
};
