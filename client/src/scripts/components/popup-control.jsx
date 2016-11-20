
import React from 'react';
import Popup from './popup';


/**
 * A component that displays a control in a bootstrap form with a drop down button to display
 * a popup content. Both control content and popup can be (shall be) customized.
 *
 * Used as base to many popup controls
 */
export default class PopupControl extends React.Component {

    constructor(props) {
        super(props);
        this.btnKeyPress = this.btnKeyPress.bind(this);
        this.controlClick = this.controlClick.bind(this);

        // initialize an empty list of values
        this.state = { };
    }

    preventHide() {
        if (this.refs.popup) {
            this.refs.popup.preventHide();
        }
    }

    /**
     * Return the rendered component for the label
     * @return {React.Component} The label component, or null if there is no label
     */
    labelRender() {
        const label = this.props.label;
        return label ? <label className="control-label">{label}</label> : null;
    }


    /**
     * Create the popup component to be displayed based on the options
     * @return {React.Component} Popup component, or null if no option is found
     */
    createPopup() {
        const content = this.props.popupContent;

        return content ? <Popup ref="popup">{content}</Popup> : null;
    }


    /**
    * Called when user clicks on the control
    **/
    controlClick() {
        if (!this.refs.popup) {
            return;
        }

        this.refs.popup.show();
    }


    btnKeyPress(evt) {
        // check if it is arrow down
        if (evt.keyCode === 40) {
            evt.preventDefault();
            this.controlClick();
        }
    }

    /**
     * Component rendering
     * @return {React.Component} Component to display
     */
    render() {
        const clazz = 'sel-box form-group' + (this.props.bsStyle ? ' has-' + this.props.bsStyle : '');

        const helpBlock = this.props.help ? (
                <div className="help-block">{this.props.help}</div>
            ) : null;

        const ctrlClass = this.props.wrapperClassName;
        const controlClass = 'form-control' + (ctrlClass ? ' ' + ctrlClass : '');

        return (
            <div className={clazz}>
                {this.labelRender()}
                <button ref="btn" className={controlClass} onClick={this.controlClick}
                    onKeyDown={this.btnKeyPress}>
                    <div className="btn-dd">
                        <i className="fa fa-chevron-down" />
                    </div>
                    {this.props.content}
                </button>
                {this.createPopup()}
                {helpBlock}
            </div>
            );
    }
}

PopupControl.propTypes = {
    label: React.PropTypes.node,
    bsStyle: React.PropTypes.oneOf(['success', 'warning', 'error']),
    help: React.PropTypes.string,
    wrapperClassName: React.PropTypes.string,
    content: React.PropTypes.node,
    popupContent: React.PropTypes.node
};
