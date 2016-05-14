
import React from 'react';
import { Row, Col, Input, Button, Fade, Alert } from 'react-bootstrap';
import { Card, AsyncButton } from '../components/index';
import { validateForm } from '../commons/validator';
import { app } from '../core/app';
import { server } from '../commons/server';


/**
 * Values to be validated
 */
const formModel = {
	user: {
		required: true
	},
	pwd: {
		required: true
	}
};

/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class Login extends React.Component {
	constructor(props) {
		super(props);
		this.loginClick = this.loginClick.bind(this);
	}


	forgotPwdClick() {
		app.goto('/pub/forgotpwd');
	}

	/**
	 * Called when user clicks on the continue button
	 */
	loginClick() {
		const res = validateForm(this, formModel);

		// is there any validation error ?
		if (res.errors) {
			this.setState({ errors: res.errors });
			return;
		}

		// hide messages and disable button
		this.setState({ invalid: false, errors: null, fetching: true });
		const val = res.value;

		const self = this;

		// request login to the server
		this.login(val.user, val.pwd)
		.then(data => {
			if (data) {
				app.goto('/sys/home/index');
			}
			else {
				self.setState({ fetching: false, invalid: true });
			}
		});
	}

	/**
	 * Perform login into the system. Returns a promise that will indicate if user
	 * @param  {String} user the user account
	 * @param  {String} pwd  the user password
	 * @return {Promise}      Promise that will be resolved with the authentication token, or null if failed
	 */
	login(user, pwd) {
		return server.post('/api/auth/login', { username: user, password: pwd })
		.then(data => {
			if (!data.success) {
				return null;
			}

			// register the authentication token in the cookies
			const authToken = data.authToken;
			app.setCookie('autk', authToken);
			return authToken;
		});
	}


	/**
	 * Render the component
	 */
	render() {
		const st = this.state;
		const err = st && st.errors || {};
		const fetching = st && st.fetching;

		const iconUser = (<i className="fa fa-user fa-fw"></i>);
		const iconPwd = (<i className="fa fa-key fa-fw"></i>);

		return (
			<Fade in transitionAppear>
				<div className="container central-container-md">
					<Card title={__('login.enter_system')}>
						<div>
							<Row>
								<Col sm={12}>
									<Input type="text" ref="user" placeholder={__('User.login')} autoFocus
										addonBefore={iconUser}
										help={err.user} bsStyle={err.user ? 'error' : undefined}
									/>
								</Col>
							</Row>
							<Row>
								<Col sm={12}>
									<Input type="password" ref="pwd" placeholder={__('User.password')}
										addonBefore={iconPwd}
										help={err.pwd} bsStyle={err.pwd ? 'error' : undefined}/>
								</Col>
							</Row>
							<Row>
								<Col sm={12}>
									<Input type="checkbox" label={__('login.rememberme')} />
								</Col>
							</Row>
							{st && st.invalid && (
							<Row >
								<Col sm={12}>
									<Alert bsStyle="danger">
										{__('login.invaliduserpwd')}
									</Alert>
								</Col>
							</Row>
							)}
							<Row>
								<Col sm={12}>
									<AsyncButton block onClick={this.loginClick}
										fetching={fetching} fetchCaption={__('action.entering')}>
										{__('action.enter')}
									</AsyncButton>
								</Col>
							</Row>
							<Row>
								<hr/>
							</Row>
							<Row>
								<Col sm={12}>
									<Button bsStyle="link" onClick={this.forgotPwdClick}>{__('login.msg2')}</Button>
								</Col>
							</Row>
							<Row>
								<Col sm={12}>
									<p className="mtop-2x">{__('login.newuser')}</p>
									<Button bsStyle="default" block>{__('login.createaccount')}</Button>
								</Col>
							</Row>
						</div>
					</Card>
				</div>
			</Fade>
		);
	}
}
