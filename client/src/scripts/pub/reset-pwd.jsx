import React from 'react';
import { FormGroup, InputGroup, HelpBlock, FormControl, Button, Alert } from 'react-bootstrap';
import Fa from '../components/fa';
import Profile from '../components/profile';
import WaitIcon from '../components/wait-icon';
import Card from '../components/card';
import { server } from '../commons/server';
import Logo from './logo';
import BorderlessForm from './borderless-form';
import { validateForm } from '../commons/validator';


/**
 * Values to be validated
 */
const form = {
	pwd1: {
		required: true,
		password: true
	},
	pwd2: {
		required: true,
		validate: doc => doc.pwd1 === doc.pwd2 ? null : __('changepwd.wrongpass2')
	}
};

export default class ResetPwd extends React.Component {

	constructor(props) {
		super(props);
		this.submit = this.submit.bind(this);
		this.state = {};
	}

	componentWillMount() {
		this.validateToken(this.props.route.params.id);
	}

	componentWillUpdate(newProps) {
		if (newProps.route.params.id !== this.state.id) {
			this.validateToken(newProps.route.params.id);
		}
	}

	validateToken(id) {
		const self = this;

		// post request to the server
		server.post('/api/pub/pwdresetinfo/' + id)
		.then(res => {
			if (res) {
				self.setState({ info: res, invalid: false });
			}
			else {
				self.setState({ invalid: true });
			}
		});

		this.setState({ id: id, info: null, invalid: null });
	}

	/**
	 * Submit the new password
	 * @return {[type]} [description]
	 */
	submit() {
		const data = validateForm(this, form);
		if (data.errors) {
			this.setState({ err: data.errors });
			return;
		}

		const params = {
			token: this.state.id,
			password: data.value.pwd1
		};

		// request password update
		const self = this;
		server.post('/api/pub/updatepwd', params)
		.then(res => {
			if (res.success) {
				self.setState({ success: true });
			}
			else {
				self.setState({ invalid: true });
			}
		});

		this.setState({ err: null });
	}

	/**
	 * Render when the token is invalid
	 * @return {React.Component} Component to be displayed
	 */
	renderInvalid() {
		return (
			<Logo backLink>
			<Alert bsStyle="danger">
				<h3>
					{__('changepwd.invalidtoken')}
				</h3>
			</Alert>
			</Logo>
			);
	}

	renderSuccess() {
		return (
			<Logo backLink>
			<Alert bsStyle="success">
				<h3>{__('changepwd')}</h3>
				<p>{__('changepwd.success1')}
				</p>
			</Alert>
			</Logo>
			);
	}

	render() {
		if (this.state.invalid) {
			return this.renderInvalid();
		}

		if (this.state.success) {
			return this.renderSuccess();
		}

		const info = this.state.info;
		if (!info) {
			return <WaitIcon type="card" />;
		}

		const err = this.state.err || {};

		return (
			<Logo>
				<div className="text-center">
					<h3>{__('changepwd')}</h3>
					<BorderlessForm>
						<FormGroup>
							<div className="text-left">
								<Profile type="user" title={info.name} size="small" />
							</div>
						</FormGroup>

						<FormGroup validationState={err.pwd1 ? 'error' : undefined} >
							<InputGroup>
								<InputGroup.Addon>
									<Fa icon="key" />
								</InputGroup.Addon>
								<FormControl type="password"
									ref="pwd1"
									placeholder={__('changepwd.newpass')} autoFocus
									/>
							</InputGroup>
							{err.pwd1 && <HelpBlock>{err.pwd1}</HelpBlock>}
						</FormGroup>

						<FormGroup validationState={err.pwd2 ? 'error' : undefined} >
							<InputGroup>
								<InputGroup.Addon>
									<Fa icon="key" />
								</InputGroup.Addon>
								<FormControl type="password"
									ref="pwd2"
									placeholder={__('changepwd.newpass2')}
									/>
							</InputGroup>
							{err.pwd2 && <HelpBlock>{err.pwd2}</HelpBlock>}
						</FormGroup>

					</BorderlessForm>

					<Button bsStyle="primary" bsSize="large" block onClick={this.submit}>
						{__('action.submit')}
					</Button>
				</div>
			</Logo>
			);
	}
}

ResetPwd.propTypes = {
	route: React.PropTypes.object.isRequired
};
