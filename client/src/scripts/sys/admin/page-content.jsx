
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { RouteView, router } from '../../components/router';
import { Fluidbar, Sidebar, WaitIcon } from '../../components/index';

import { hasPerm } from '../session';


/**
 * The page controller of the public module
 */
export default class PageContent extends React.Component {

	constructor(props) {
		super(props);
		this.menuSelect = this.menuSelect.bind(this);
	}

	componentWillMount() {
		this.updateRoutes();
	}

	/**
	 * Create the route list from the list of items
	 */
	updateRoutes() {
		const routes = RouteView.createRoutes(this.props.menu.filter(item => !item.separator));
		this.setState({ routes: routes });
	}

	/**
	 * Called when user clicks on a menu item
	 * @param  {string} data The path to move to
	 */
	menuSelect(data) {
		router.goto(this.props.path + data.path);
	}

	render() {
		// get information about the route being rendered
		const route = this.props.route;

		// get forward path
		const forpath = route.forpath;

		// get route to be rendered
		const selroute = this.state.routes.find(forpath);

		// calc selected item
		const selItem = selroute ? selroute.data : null;

		// remove items with no permission
		const menu = this.props.menu.filter(item => item.perm && !hasPerm(item.perm) ? null : item);


		return (
			<div>
				<Fluidbar>
					<h3>{__('admin') + ' - ' + this.props.title}</h3>
				</Fluidbar>
				<Grid fluid>
					<Row>
						<Col sm={3}>
							<Sidebar items={menu} selected={selItem} onSelect={this.menuSelect} />
						</Col>
						<Col sm={9}>
							<div className="mtop-2x">
								<RouteView routes={this.state.routes} loadingIcon={<WaitIcon />} />
							</div>
						</Col>
					</Row>
				</Grid>
			</div>

			);
	}
}

PageContent.propTypes = {
	// the array with menu options to display in the left menu
	menu: React.PropTypes.array,
	// the title of the page
	title: React.PropTypes.string,
	// the route object given from the route lib
	route: React.PropTypes.object,
	// the main path of the pages in the admin menu
	path: React.PropTypes.string
};
