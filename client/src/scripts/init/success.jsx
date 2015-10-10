import React from 'react';
import { Grid, Row, Button, Fade } from 'react-bootstrap';
import { navigator } from '../components/router.jsx';
import Title from '../components/title.jsx';
import Card from '../components/card.jsx';


export default class Success extends React.Component {
    constructor(props) {
        super(props);
        this.contClick = this.contClick.bind(this);
    }

    /**
     * Called when user clicks on the continue button
     */
    contClick() {
        navigator.goto('/pub/login');
    }


    /**
     * Render the component
     */
    render() {
        let msg = this.props.appState.message;

        return (
            <Fade in transitionAppear>
                <div className='container central-container-md'>
                    <Card>
                        <div className='text-center'>
                            <h3>
                                Workspace registration
                            </h3>
                            <br/>
                            <i className='fa fa-check-circle fa-4x text-success'></i>
                            <br/>
                            <p className='mtop-2x'>
                                The workspace <span className='badge'>{msg}</span> was successfully registered.
                            </p>
                        </div>
                        <div>
                            <Button bsStyle='default' block onClick={this.contClick}>Go to login page</Button>
                        </div>
                    </Card>
                </div>
            </Fade>
        );
    }
}