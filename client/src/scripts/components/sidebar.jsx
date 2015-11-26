/**
 * React component that displays a side bar - a verftical bar aligned to the left taking the whole space
 *
 *  @author Ricardo Memoria
 *  nov-2015
 */

import React from 'react';
import { Nav, NavItem } from 'react-bootstrap';
import { hasPerm } from '../core/act-session';

// load style
import './sidebar.less';

export default class Sidebar extends React.Component {

    render() {
		// get the items to fill in the sidebar
		let items = this.props.items || [];

		// remove items with no permission
		items = items.filter(item => item.perm && !hasPerm(item.perm) ? null : item);

        return (
			<div className="sidebar">
				<Nav>
					{items.map(item => 	{
						if (item.separator) {
							return <hr/>;
						}

						if (item.icon) {
							return <NavItem key={item.caption}><i className={'fa fa-fw fa-' + item.icon}/>{item.caption}</NavItem>;
						}

						return <NavItem>{item.caption}</NavItem>;
					})}
				</Nav>
			</div>
        );
    }
}


Sidebar.propTypes = {
	items: React.PropTypes.array,
    children: React.PropTypes.any
};
