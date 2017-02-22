
import React from 'react';
import { Grid, Row, Col, Button, Fade, FormGroup, FormControl } from 'react-bootstrap';
import { app } from '../core/app';


/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.langChange = this.langChange.bind(this);
        this.contClick = this.contClick.bind(this);
        this.state = { lang: app.getLang() };
    }

    /**
     * Called when user clicks on the continue button
     */
    contClick() {
        // temporarily goes right to the workspace registration
        app.goto('/init/initoptions');
    }

    /**
     * Called when user select a language
     */
    langChange(evt) {
        const lang = evt.target.value;
        app.setLang(lang);
        window.location.reload(true);
        this.setState({ lang: lang });
    }

    /**
     * Render the component
     */
    render() {
        const langs = app.getState().app.languages;
        const lg = this.state.lang;

        return (
            <Fade in transitionAppear>
                <Grid>
                    <Row>
                        <Col md={6} mdOffset={3}>
                            <h1>{__('init.welcome')}</h1>
                            <p className="text-muted">{__('init.welcome.msg1')}</p>
                        </Col>
                        <Col md={6} mdOffset={3}>
                            <FormGroup bsSize="large">
                            <FormControl componentClass="select" size={8} multiple autoFocus value={[lg]} onChange={this.langChange}>
                                {langs.map((lang) =>
                                        <option key={lang.id} value={lang.id}>{lang.name}</option>
                                )
                                }
                            </FormControl>
                            </FormGroup>
                        </Col>
                        <Col md={6} mdOffset={3}>
                            <div className="pull-right">
                                <Button bsStyle="success" bsSize="large" onClick={this.contClick}>
                                    {__('action.continue')}
                                </Button>
                            </div>
                        </Col>
                    </Row>
                </Grid>
            </Fade>
        );
    }
}
