 import React from 'react';
import { Card, RemoteFormDialog } from '../../components/index';
import { Button, ButtonToolbar } from 'react-bootstrap';
import { server } from '../../commons/server';

export default class ShowMessage extends React.Component {

	constructor(props) {
		super(props);
		this.click = this.click.bind(this);
		this.click2 = this.click2.bind(this);
		this.state = {};
	}

	click() {
		// using a path
		this.setState({ remotePath: '/api/test/form' });
	}

	click2() {
		// using a function that will return a prosise with form data
		this.setState({
			remotePath: () => server.get('/api/test/form')
		});
	}

	render() {
		const remotePath = this.state.remotePath;

		return (
			<Card title="Server forms example">
				<ButtonToolbar>
					<Button bsStyle="primary" onClick={this.click} >{'Get it'}</Button>
					<Button bsStyle="primary" onClick={this.click2}>{'Get it 2'}</Button>
				</ButtonToolbar>
				{
					remotePath &&
					<RemoteFormDialog
						remotePath={remotePath}
						/>
				}
			</Card>
			);
	}
}

ShowMessage.propTypes = {

};
