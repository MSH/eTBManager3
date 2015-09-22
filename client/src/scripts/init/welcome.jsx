import React from 'react';
import { Grid, Row, Col, Input, Button } from 'react-bootstrap';


export default class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.langChange = this.langChange.bind(this);
    }

    langChange(evt) {
        let elem = this.refs.langs.getInputDOMNode();
        let lang = elem.options[elem.selectedIndex].value;
        window.app.setCookie('lang', lang);
        window.location.reload(true);
    }

	render() {
		let langs = this.props.appState.app.languages;
		let lg = window.app.getCookie('lang');

		return (
			<Grid>
				<Row>
					<Col md={6} mdOffset={3}>
						<h1>Welcome to e-TB Manager</h1>
						<p className='text-muted'>In order to continue, please select your language</p>
					</Col>
					<Col md={6} mdOffset={3}>
						<Input type="select" ref="langs" size={8} multiple autoFocus
							bsSize="large" value={[lg]} onChange={this.langChange}>
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