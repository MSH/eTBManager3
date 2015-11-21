import React from 'react';
import { Button } from 'react-bootstrap';
import Callout from '../components/callout';


export default class PageNotFound extends React.Component {

    render() {
        return (
            <Callout bsStyle="danger" >
                <h1>Page not found</h1>
                <p>The page you are trying to access does not exist</p>
                <p>
                    <Button bsStyle="danger">Go to home page</Button>
                </p>
            </Callout>
        );
    }
}
