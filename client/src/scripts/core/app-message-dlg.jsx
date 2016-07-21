
import React from 'react';
import { app } from './app';
import { SHOW_MESSAGE } from './actions';
import MessageDlg from '../components/message-dlg';

export default class AppMessageDlg extends React.Component {

	constructor(props) {
		super(props);
		this.close = this.close.bind(this);
		this._onAppChange = this._onAppChange.bind(this);
        this.state = {};
	}

    componentDidMount() {
        app.add(this._onAppChange);
    }

    componentWillUmount() {
        app.remove(this._onAppChange);
    }

    /**
     * Called when application state changes
     * @param  {[type]} action [description]
     * @return {[type]}        [description]
     */
    _onAppChange(action, data) {
        if (action === SHOW_MESSAGE) {
            this.setState({ show: true, data: data });
        }
    }

    close(evt) {
		this.state.data.onClose(evt);
		this.setState({ show: false, data: null });
    }


	render() {
		if (!this.state.show) {
			return null;
		}

		const props = Object.assign({}, this.state.data);
		delete props.onClose;

		return <MessageDlg show onClose={this.close} {...props} />;
	}
}
