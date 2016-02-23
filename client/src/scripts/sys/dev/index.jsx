
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar, Sidebar } from '../../components/index';
import { RouteView, router } from '../../components/router';

/** Pages of the public module */
import Home from './home';
import DatePickerExamples from './date-picker-examples';


const sidebar = [
	{
		title: 'Developers playground',
		icon: 'sitemap',
        path: '/index',
        view: Home
	},
	{
		title: 'Calendar',
		view: DatePickerExamples,
		path: '/calendar',
		icon: 'calendar'
	}
];

const routes = RouteView.createRoutes(sidebar);


/**
 * The page controller of the public module
 */
export default class Index extends React.Component {

	constructor(props) {
		super(props);

		this.menuSelect = this.menuSelect.bind(this);
	}

	menuSelect(data) {
		router.goto('/sys/dev' + data.path);
	}

	render() {
		// get information about the route being rendered
		const route = this.props.route;

		// get forward path
		const forpath = route.forpath;

		// get route to be rendered
		const selroute = routes.find(forpath);

		// calc selected item
		const selItem = selroute ? selroute.data : null;

		return (
			<div>
				<Fluidbar>
					<div className="margin-2x">
						<Profile size="large"
							title="Developers playground"
							subtitle="Your place to test new stuff"
							imgClass="prof-male"
							fa="laptop" />
					</div>
				</Fluidbar>
				<Grid className="mtop-2x">
					<Row>
						<Col md={2}>
							<Sidebar items={sidebar} selected={selItem} onSelect={this.menuSelect} />
						</Col>
						<Col md={8}>
							<RouteView routes={routes} />
						</Col>
					</Row>
				</Grid>
			</div>
			);
	}
}

Index.propTypes = {
	// the route object given from the route lib
	route: React.PropTypes.object,
	// the main path of the pages in the admin menu
	path: React.PropTypes.string
};
