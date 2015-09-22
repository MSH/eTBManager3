import React from 'react';
import { Grid, Row, Col, Input, Button } from 'react-bootstrap';
import Cookie from '../commons/cookies';


export default class Welcome extends React.Component {
	render() {
		let langs = this.props.appState.app.languages;
		let lg = 'en';
		console.log(Cookie.get('lang'));
		console.log(this.props.appState);

		return (
			<Grid>
				<Row>
					<Col md={6} mdOffset={3}>
						<h1>Welcome to e-TB Manager</h1>
						<p className='text-muted'>In order to continue, please select your language</p>
					</Col>
					<Col md={6} mdOffset={3}>
						<Input type="select" ref="langs" size={8} multiple autoFocus
							bsSize="large" value={[lg]}>
							{ langs.map((lang) =>
  									<option key={lang.id} value={lang.id}>{lang.text}</option>
								)
							}
    					</Input>
					</Col>
					<Col md={6} mdOffset={3}>
						<div className='pull-right'>
							<Button bsStyle="primary" pullRight bsSize='large'>Continue
							</Button>
						</div>
					</Col>
				</Row>
			</Grid>
			);
	}
}