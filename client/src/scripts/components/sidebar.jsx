/**
 * React component that displays a side bar - a verftical bar aligned to the left taking the whole space
 *
 *  @author Ricardo Memoria
 *  nov-2015
 */

import React from 'react';
import './sidebar.less';


export default class SideBar extends React.Component {

    render() {

        return (
            <div id="sidebar-wrapper">
                {this.props.children}
            </div>
        );
    }
}


SideBar.propTypes = {
    bsStyle: React.PropTypes.string,
    children: React.PropTypes.any
};

// SideBar.defaultProps = {
//     fetching: false,
//     fetchMsg: 'Wait...'
// };
