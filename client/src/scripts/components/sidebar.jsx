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

    itemClick(item) {
		if (this.props.onSelect) {
			this.props.onSelect(item);
		}

		if (!item.path) {
			return;
		}

		window.location.hash = this.props.path + item.path;
    }

    render() {
		// get the items to fill in the sidebar
		const items = this.props.items || [];

        return (
			<div className="sidebar">
				<Nav onSelect={this.props.onSelect} activeKey={this.props.selected}>
					{items.map((item, index) => {
						if (item.separator) {
							return <NavItem disabled key={index}><hr/></NavItem>;
						}

						return (
							<NavItem eventKey={item} key={index}>
							{
								item.icon && <i className={'fa fa-fw fa-' + item.icon} />
							}
							{item.title}
							</NavItem>
							);
					})}
				</Nav>
			</div>
        );
    }
}


Sidebar.propTypes = {
	items: React.PropTypes.array,
    onSelect: React.PropTypes.func,
    selected: React.PropTypes.object,
	path: React.PropTypes.string,
	route: React.PropTypes.object
};
