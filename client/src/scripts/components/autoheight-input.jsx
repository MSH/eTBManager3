import React from 'react';

export default class AutoheightInput extends React.Component {

    constructor(props) {
        super(props);
        this.inputChange = this.inputChange.bind(this);
    }

    componentDidMount() {
        this.updateHeight();
    }

    /**
     * Update the input height based on the content
     * @return {[type]} [description]
     */
    updateHeight() {
        if (!this.refs.input) {
            return;
        }
        const el = this.refs.input;
        el.style.height = 'auto';
        el.style.height = el.scrollHeight + 'px';
    }


    inputChange() {
        this.updateHeight();
        if (this.props.onChange) {
            this.props.onChange(this.refs.input.value);
        }
    }

    /**
     * Return the text stored in the input control
     * @return {[type]} [description]
     */
    getText() {
        return this.refs.input.value;
    }

    setText(txt) {
        this.refs.input.value = txt;
        this.inputChange();
    }

    /**
     * Set the focus on the control
     * @return {[type]} [description]
     */
    focus() {
        this.refs.input.focus();
    }

    render() {
        return (
            <textarea ref="input" rows="1"
                defaultValue={this.props.defaultValue}
                className={this.props.className}
                style={{ height: 'auto', overflowY: 'hidden', resize: 'none' }}
                onInput={this.inputChange}/>
        );
    }
}

AutoheightInput.propTypes = {
    className: React.PropTypes.string,
    rows: React.PropTypes.number,
    onChange: React.PropTypes.func,
    defaultValue: React.PropTypes.string
};

AutoheightInput.defaultProps = {
    rows: 1,
    className: 'form-control'
};
