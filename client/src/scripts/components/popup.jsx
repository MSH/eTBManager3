
import React from 'react';
import { Collapse } from 'react-bootstrap';


export default class Popup extends React.Component {

    constructor(props) {
        super(props);
        this._togglePopup = this._togglePopup.bind(this);
        this.show = this.show.bind(this);
        this.hide = this.hide.bind(this);

        this.state = { show: false };
    }

    componentWillUnmount() {
        this._remHandler();
    }

    _togglePopup() {
        const open = !this.state.show;

        if (open) {
            this.show();
        }
        else {
            const prevHide = this._prevHide;
            delete this._prevHide;

            if (!prevHide) {
                this.hide();
            }
        }
    }

    _addHandler() {
        document.addEventListener('click', this._togglePopup, false);
    }

    _remHandler() {
        document.removeEventListener('click', this._togglePopup);
    }

    hide() {
        if (!this.state.show) {
            return;
        }
        this.setState({ show: false });

        if (this.props.onHide) {
            this.props.onHide();
        }
        this._remHandler();
    }

    show() {
        delete this._prevHide;
        if (this.state.show) {
            return;
        }
        this.setState({ show: true });
        this._addHandler();
    }

    preventHide() {
        this._prevHide = true;
    }

    render() {
        const open = this.props.show || this.state.show;

        const clazz = 'dropdown' + (open ? ' open' : '');

        return (
            <div className={clazz}>
            <Collapse in={open}>
                    <div className="dropdown-menu">
                        {this.props.children}
                    </div>
            </Collapse>
            </div>
            );
    }
}

Popup.propTypes = {
    children: React.PropTypes.node,
    show: React.PropTypes.bool,
    onHide: React.PropTypes.func
};
