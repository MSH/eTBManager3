/**
 * This is a very simple wrap to the bootstrap button to support asynchronous operations,
 * exposing two properties:
 *
 *  - fetching: boolean - If true, button will be disabled and display an animated icon beside the caption
 *  - fetchCaption: string - If informed, when fetching, the button caption will be replaced by this caption
 *
 *  @author Ricardo Memoria
 *  sept-2015
 */

import React from 'react';
import { Button } from 'react-bootstrap';

export default class AsyncButton extends React.Component {

    render() {

        let props = this.props;
        let fetching = props.fetching;
        let fetchMsg = props.fetchCaption;

        delete props.fetching;

        props.disabled = fetching;

        if (!props.bsStyle) {
            props.bsStyle = 'primary';
        }


        return (
            <Button {...props}>
                {fetching && <i className='fa fa-circle-o-notch fa-spin fa-fw'></i>}
                {fetching && fetchMsg? fetchMsg: this.props.children}
            </Button>
        )
    }
}