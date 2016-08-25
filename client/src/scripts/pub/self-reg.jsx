import React from 'react';
import { Row, Col, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';
import AsyncButton from '../components/async-button';
import Card from '../components/card';
import Error from '../components/error';
import Fa from '../components/fa';
import Logo from './logo';
import BorderlessForm from './borderless-form';
import { validateForm } from '../commons/validator';
import { server } from '../commons/server';

/**
 * Values to be validated
 */
const formModel = {
	name: {
		required: true
	},
	login: {
		required: true
	},
	email: {
		required: true,
		email: true
	},
	organization: {
	}
};

/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class UserReg extends React.Component {
	constructor(props) {
		super(props);
		this.submit = this.submit.bind(this);
		this.state = {};
	}

	/**
	 * Called when user clicks on the continue button
	 */
	submit() {
		const res = validateForm(this, formModel);

		// is there any validation error ?
		if (res.errors) {
			this.setState({ errors: res.errors });
			return;
		}

		// clear error messages
		this.setState({ errors: null, fetching: true });

		const self = this;
		server.post('/api/pub/selfreg', res.value)
		.then(resp => {
			if (!resp.success) {
				self.setState({ errors: resp.errors, fetching: false });
				return;
			}

			self.setState({ success: true, 	fetching: false });
		})
		.catch(() => self.setState({ fetching: false }));
	}

	/**
	 * Render message when user successfully register himself
	 * @return {[type]} [description]
	 */
	renderSuccess() {
		return (
			<Card>
				<div className="text-center">
					<div className="text-primary">
					<Fa icon="check-circle" size={3} />
					<h1>{__('global.success')}</h1>
					</div>
					<p>
						{__('userreg.success.1')}
					</p>
				</div>
			</Card>
			);
	}


	/**
	 * Render the component
	 */
	render() {
		const err = {};
		if (this.state.errors) {
			// transform error from an array to an object
			this.state.errors.forEach(item => {
				err[item.field] = item.msg;
			});
		}

		const fetching = !!this.state.fetching;
		const success = !!this.state.success;

		return (
			<Logo backLink>
			{
				success ? this.renderSuccess() :
				<div>
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
								<Error msg={err.name} />
							</FormGroup>

							<FormGroup validationState={err.login ? 'error' : undefined} >
								<ControlLabel>{__('User.login') + ':'}</ControlLabel>
								<FormControl type="text"
									ref="login"
									placeholder="Ex.: RMEMORIA" />
								<Error msg={err.login} />
							</FormGroup>

							<FormGroup validationState={err.email ? 'error' : undefined} >
								<ControlLabel>{__('User.email') + ':'}</ControlLabel>
								<FormControl type="email"
									ref="email"
									placeholder="Ex.: rmemoria@teste.com" />
								<Error msg={err.email} />
							</FormGroup>

							<FormGroup validationState={err.organization ? 'error' : undefined} >
								<ControlLabel>{__('login.organization') + ':'}</ControlLabel>
								<FormControl type="text"
									ref="organization"
									placeholder="Ex.: MSH" />
								<Error msg={err.organization} />
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
			}
			</Logo>
		);
	}
}
