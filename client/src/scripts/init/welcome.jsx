import React from 'react';
import { Grid, Row, Col, Input, Button, Fade } from 'react-bootstrap';
import { navigator } from '../components/router.jsx';
import Title from '../components/title.jsx';


/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.langChange = this.langChange.bind(this);
        this.contClick = this.contClick.bind(this);
    }

    /**
     * Called when user clicks on the continue button
     */
    contClick() {
        navigator.goto('/init/initoptions');
    }

    /**
     * Called when user select a language
     */
    langChange() {
        let elem = this.refs.langs.getInputDOMNode();
        let lang = elem.options[elem.selectedIndex].value;
        window.app.setLang(lang);
        window.location.reload(true);
    }

    /**
     * Render the component
     */
	render() {
		let langs = this.props.appState.app.languages;
		let lg = window.app.getLang();

		return (
            <Fade in transitionAppear>
                <Grid>
                    <Row>
                        <Col md={6} mdOffset={3}>
                            <Title text={__('Welcome to e-TB Manager')}></Title>
                            <p className='text-muted'>{__('Please select your language and click \'Continue\'')}</p>
                        </Col>
                        <Col md={6} mdOffset={3}>
                            <Input type="select" ref="langs" size={8} multiple autoFocus
                                   bsSize="large" value={[lg]} onChange={this.langChange}>
                                { langs.map((lang) =>
                                        <option key={lang.id} value={lang.id}>{lang.name}</option>
                                )
                                }
                            </Input>
                        </Col>
                        <Col md={6} mdOffset={3}>
                            <div className='pull-right'>
                                <Button bsStyle="primary" pullRight bsSize='large' onClick={this.contClick}>{__('Continue')}
                                </Button>
                            </div>
                        </Col>
                    </Row>
                </Grid>
            </Fade>
			);
	}
}