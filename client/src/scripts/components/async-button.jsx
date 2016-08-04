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
        const props = this.props;
        const fetching = props.fetching;
        const fetchMsg = props.fetchMsg;

        const btnProps = Object.assign({},
            this.props,
            {
                disabled: fetching || this.props.disabled,
                bsStyle: props.bsStyle ? props.bsStyle : 'primary'
            });

        delete btnProps.fetching;
        delete btnProps.faIcon;
        delete btnProps.fetchMsg;

        if (fetching) {
            btnProps.onClick = null;
        }

        const faIcon = this.props.faIcon;

        return (
            <Button {...btnProps}>
                {fetching && <i className="fa fa-circle-o-notch fa-spin fa-fw" />}
                {!fetching && faIcon ? <i className={'fa fa-fw fa-' + faIcon}/> : null}
                {fetching && fetchMsg ? fetchMsg : this.props.children}
            </Button>
        );
    }
}

AsyncButton.propTypes = {
    fetching: React.PropTypes.bool,
    fetchMsg: React.PropTypes.string,
    children: React.PropTypes.any,
    faIcon: React.PropTypes.string,
    disabled: React.PropTypes.bool
};

AsyncButton.defaultProps = {
    fetching: false,
    fetchMsg: __('global.wait')
};
