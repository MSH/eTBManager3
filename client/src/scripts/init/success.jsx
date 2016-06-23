
import React from 'react';
import { Button } from 'react-bootstrap';
import Card from '../components/card.jsx';
import { app } from '../core/app';
import { format } from '../commons/utils';


export default class Success extends React.Component {
    constructor(props) {
        super(props);
        this.contClick = this.contClick.bind(this);
    }

    /**
     * Called when user clicks on the continue button
     */
    contClick() {
        app.setState({ login: 'admin' });
        app.goto('/pub/login');
    }


    /**
     * Render the component
     */
    render() {
        const msg = this.props.wsname;

        return (
            <div className="container central-container-md">
                <Card>
                    <div className="text-center">
                        <h3>
                            {__('init.ws.name')}
                        </h3>
                        <br/>
                        <i className="fa fa-check-circle fa-4x text-success"></i>
                        <br/>
                        <p className="mtop-2x">
                            {format(__('init.ws.success'), msg)}
                        </p>
                    </div>
                    <div>
                        <Button bsStyle="default" block onClick={this.contClick}>{__('init.ws.gotologin')}</Button>
                    </div>
                </Card>
            </div>
        );
    }
}


Success.propTypes = {
    wsname: React.PropTypes.string
};
