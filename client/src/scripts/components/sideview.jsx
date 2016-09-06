/**
 * React component that displays a side bar - a verftical bar aligned to the left taking the whole space
 *
 *  @author Ricardo Memoria
 *  nov-2015
 */

import React from 'react';
import { Nav, NavItem } from 'react-bootstrap';
import { RouteView, router } from '../components/router';
import { hasPerm } from '../sys/session';


// load style
import './sideview.less';

export default class Sideview extends React.Component {

    componentWillMount() {
        this.updateRoutes(this.props.items);
    }

    componentWillReceiveProps(nextProps) {
        this.updateRoutes(nextProps.items);
    }

	/**
	 * Create the route list from the list of items
	 */
	updateRoutes(items) {
		const routes = items !== null ?
            RouteView.createRoutes(items.filter(item => !item.separator)) :
            null;

		this.setState({ routes: routes });
	}

    itemClick(item) {
		window.location.hash = this.props.route.path + item.path;
    }

    /**
     * Get the selected item
     */
    getSelected() {
        const forpath = this.props.route.forpath;
        const items = this.props.items;

        return forpath ?
            items.find(it => it.path === forpath) :
            items.find(it => it.default);
    }

    render() {
        if (!this.state.routes) {
            return null;
        }
		// get the items to fill in the sidebar
		const items = this.props.items;

        return (
			<div className="sideview">
				<Nav onSelect={this.itemClick} activeKey={this.props.selected}>
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


Sideview.propTypes = {
	items: React.PropTypes.array.isRequired,
	route: React.PropTypes.object.isRequired
};
