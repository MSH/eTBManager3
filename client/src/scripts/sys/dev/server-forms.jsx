import React from 'react';
import { Card, AsyncButton, FormDialog } from '../../components/index';
import { ButtonToolbar } from 'react-bootstrap';
import { server } from '../../commons/server';

export default class ShowMessage extends React.Component {

	constructor(props) {
		super(props);
		this.click = this.click.bind(this);
		this.state = {};
	}

	click() {
		this.setState({ fetching: true, code: null });

		const self = this;

		server.get('/api/test/form')
		.then(res => {
			self.setState({ fetching: false });
			self.testServerCode(res.schema);
		});
	}

	testServerCode(code) {
		/* eslint no-new-func: "off" */
		const func = new Function('', 'return ' + code + ';');

		const res = func();
		/* eslint no-console: "off" */
		console.log(res);
		this.setState({ schema: res });
	}

	render() {

		const fetching = this.state.fetching;
		const schema = this.state.schema;
		const doc = {};

		return (
			<Card title="Server forms example">
				<ButtonToolbar>
					<AsyncButton bsStyle="primary" onClick={this.click} fetching={fetching}>{'Get it'}</AsyncButton>
				</ButtonToolbar>
				{
					schema &&
					<FormDialog schema={schema} doc={doc} />
				}
			</Card>
			);
	}
}

ShowMessage.propTypes = {

};
