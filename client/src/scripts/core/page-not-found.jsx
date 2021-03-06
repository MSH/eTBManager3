import React from 'react';
import { Button } from 'react-bootstrap';
import Callout from '../components/callout';


export default class PageNotFound extends React.Component {

    homeClick() {
        window.hash = '/sys';
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
