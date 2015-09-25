import React from 'react';
import { Alert, Button } from 'react-bootstrap';


export default class PageNotFound extends React.Component {

    render() {
        return (
            <Alert bsStyle="danger" >
                <h1>Page not found</h1>
                <p>The page you are trying to access does not exist</p>
                <p>
                    <Button bsStyle="danger">Go to home page</Button>
                </p>
            </Alert>
        );
    }
}