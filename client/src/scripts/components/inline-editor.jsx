import React from 'react';
import ReactDOM from 'react-dom';

export default class InlineEditor extends React.Component {

    constructor(props) {
        super(props);
        this.labelClick = this.labelClick.bind(this);
        this.keyDown = this.keyDown.bind(this);
        this.changeValue = this.changeValue.bind(this);
        this.applyValue = this.applyValue.bind(this);
    }

    componentDidUpdate() {
        const elem = ReactDOM.findDOMNode(this.refs.edt);
        if (elem) {
            elem.focus();
        }
    }

    /**
     * Called when user clicks on the label
     */
    labelClick() {
        this.setState({ editing: true, value: this.props.value });
    }

    /**
     * Called when user presses a key on the input box
     */
    keyDown(evt) {
        if (evt.keyCode === 13) {
            this.applyValue();
        }

        if (evt.keyCode === 27) {
            this.setState({ editing: false });
        }
    }

    changeValue(evt) {
        this.setState({ value: evt.target.value });
    }

    /**
     * Notify the parent about the value that was changed
     */
    applyValue() {
        const value = this.state.value;
        if (this.props.onChange) {
            this.props.onChange(value);
        }
        this.setState({ editing: false, value: null });
    }

    render() {
        const editing = this.state && this.state.editing;

        // get the style class to use
        let className = this.props.className;
        className = (className ? className + ' ' : '') +
            (editing ? 'edit' : 'display');


        return editing ?
            <input ref="edt"
                value={this.state.value}
                onKeyDown={this.keyDown}
                onChange={this.changeValue}
                onBlur={this.applyValue}
                className={className} /> :
            <span className={className} onClick={this.labelClick}>
                {this.props.value ? this.props.value : ''}
            </span>;
    }
}

InlineEditor.propTypes = {
    value: React.PropTypes.string,
    onChange: React.PropTypes.func,
    className: React.PropTypes.string
};
