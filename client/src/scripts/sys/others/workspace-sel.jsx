import React from 'react';
import { Modal, Button, Nav, NavItem } from 'react-bootstrap';
import { Fa, WaitIcon, Profile } from '../../components';
import { app } from '../../core/app';
import { server } from '../../commons/server';
import { changeWorkspace } from '../session';


export default class WorkspaceSel extends React.Component {

	constructor(props) {
		super(props);

		this.close = this.close.bind(this);
	}

	componentWillMount() {
		const self = this;
		const handler = app.add(evt => {
			if (evt === 'open-ws-sel') {
				server.post('/api/sys/workspaces')
				.then(res => {
					self.setState({ list: res });
				});
				self.setState({ show: true });
			}
		});

		this.state = { handler: handler };
	}

	componentWillUnmount() {
		app.remove(this.state.handler);
	}

	close() {
		this.setState({ show: null });
	}

	changeWs(ws) {
		return () => {
			changeWorkspace(ws.id);
		};
	}

	render() {
		if (!this.state.show) {
			return null;
		}

		const list = this.state.list;
		const wsid = app.getState().session.userWorkspaceId;

		const bodyStyle = {
			overflowY: 'auto',
			maxHeight: window.innerHeight * 0.7
		};

		return (
			<Modal show={this.state.show} >
				<Modal.Header closeButton onHide={this.close}>
					<Modal.Title id="contained-modal-title">{__('changews')}</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<div style={bodyStyle}>
					{
						list ?
						<Nav bsStyle="pills" stacked className="nav-select">
							{
								list.map(ws => (
									<NavItem bsStyle="pills"
										key={ws.id}
										onClick={this.changeWs(ws)}
										active={ws.id === wsid}
										disabled={ws.id === wsid}>
										<Profile type="ws" title={ws.name} size="small"/>
									</NavItem>
								))
							}
						</Nav> :
						<WaitIcon type="card" />
					}
					</div>
				</Modal.Body>
				<Modal.Footer>
					<Button bsStyle="primary" onClick={this.close}>{__('action.cancel')}</Button>
				</Modal.Footer>
				</Modal>
			);
	}
}

WorkspaceSel.propTypes = {

};
