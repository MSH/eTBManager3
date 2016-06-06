import React from 'react';
import { Row, Col, FormGroup, FormControl, HelpBlock, ControlLabel } from 'react-bootstrap';
import AsyncButton from '../components/async-button';
import Logo from './logo';
import BorderlessForm from './borderless-form';


/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class UserReg extends React.Component {
	constructor(props) {
		super(props);
		this.submit = this.submit.bind(this);
	}

	/**
	 * Called when user clicks on the continue button
	 */
	submit() {
		navigator.goto('/init/initoptions');
	}

	/**
	 * Render the component
	 */
	render() {
		const err = {};
		const fetching = false;

		return (
			<Logo backLink>
				<div className="login">
				<Row>
					<Col md={12}>
						<div className="text-center">
							<h3>{__('userreg')}</h3>
						</div>
						<BorderlessForm>
							<FormGroup validationState={err.name ? 'error' : undefined} >
								<ControlLabel>{__('User.name') + ':'}</ControlLabel>
								<FormControl type="text"
									ref="name"
									placeholder="Ex.: Ricardo Memoria" autoFocus />
								{err.name && <HelpBlock>{err.name}</HelpBlock>}
							</FormGroup>

							<FormGroup validationState={err.user ? 'error' : undefined} >
								<ControlLabel>{__('User.login') + ':'}</ControlLabel>
								<FormControl type="text"
									ref="login"
									placeholder="Ex.: RMEMORIA" />
								{err.user && <HelpBlock>{err.user}</HelpBlock>}
							</FormGroup>

							<FormGroup validationState={err.email ? 'error' : undefined} >
								<ControlLabel>{__('User.email') + ':'}</ControlLabel>
								<FormControl type="email"
									ref="email"
									placeholder="Ex.: rmemoria@teste.com" />
								{err.email && <HelpBlock>{err.email}</HelpBlock>}
							</FormGroup>

							<FormGroup validationState={err.organization ? 'error' : undefined} >
								<ControlLabel>{__('login.organization') + ':'}</ControlLabel>
								<FormControl type="text"
									ref="organization"
									placeholder="Ex.: MSH" />
								{err.organization && <HelpBlock>{err.organization}</HelpBlock>}
							</FormGroup>
						</BorderlessForm>
					</Col>
				</Row>
				<Row>
					<Col sm={12}>
						<AsyncButton block onClick={this.submit}
							bsSize="large"
							fetching={fetching} fetchCaption={__('action.entering')}>
							{__('action.submit')}
						</AsyncButton>
					</Col>
				</Row>
				</div>
			</Logo>
		);
	}
}