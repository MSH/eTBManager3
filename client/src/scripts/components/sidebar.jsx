/**
 * React component that displays a side bar - a verftical bar aligned to the left taking the whole space
 *
 *  @author Ricardo Memoria
 *  nov-2015
 */

import React from 'react';
import { Nav, NavItem } from 'react-bootstrap';

// load style
import './sidebar.less';

export default class Sidebar extends React.Component {

    itemClick(param) {
		if (this.props.onSelect) {
			this.props.onSelect(param);
		}
    }

    render() {
		// get the items to fill in the sidebar
		const items = this.props.items || [];

        let count = 0;

        return (
			<div className="sidebar">
				<Nav onSelect={this.props.onSelect} activeKey={this.props.selected}>
					{items.map(item => {
                        count++;

						if (item.separator) {
							return <NavItem disabled key={count}><hr/></NavItem>;
						}

						if (item.icon) {
							return (
                                <NavItem eventKey={item} key={count}>
                                    <i className={'fa fa-fw fa-' + item.icon}/>{item.title}
                                </NavItem>
                                );
						}

						return <NavItem eventKey={item} key={count}>{item.title}</NavItem>;
					})}
				</Nav>
			</div>
        );
    }
}


Sidebar.propTypes = {
	items: React.PropTypes.array,
    onSelect: React.PropTypes.func,
    children: React.PropTypes.any,
    selected: React.PropTypes.object
};
