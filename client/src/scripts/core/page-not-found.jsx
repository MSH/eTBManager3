import React from 'react';
import { Button } from 'react-bootstrap';
import Callout from '../components/callout';
import { app } from './app';


export default class PageNotFound extends React.Component {

    homeClick() {
        app.goto('/sys/home/index');
    }

    render() {
        return (
            <Callout bsStyle="danger" >
                <h1>{__('pagenotfound')}</h1>
                <p>{__('pagenotfound.msg')}</p>
                <p>
                    <Button bsStyle="danger" onClick={this.homeClick}>{__('pagenotfound.home')}</Button>
                </p>
            </Callout>
        );
    }
}
