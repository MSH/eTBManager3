
import React from 'react';
import { Row, Col, Nav, NavItem } from 'react-bootstrap';
import { app } from '../../core/app';
import { RouteView, router } from '../../components/router';
import UnitPanel from './commons/unit-panel';


import General from './unit/general';
import Cases from './unit/cases';
import Inventory from './unit/inventory';


const routes = RouteView.createRoutes([
	{
		title: __('general'),
		path: '/general',
		view: General,
		default: true
	},
	{
		title: __('cases'),
		path: '/cases',
		view: Cases
	},
	{
		title: __('meds.inventory'),
		path: '/inventory',
		view: Inventory
	}
]);


export default class Unit extends React.Component {

	constructor(props) {
		super(props);

		this.tabSelect = this.tabSelect.bind(this);
	}


	tabIndex() {
		const route = this.props.route;
		const path = route.forpath;
		if (!path) {
			return 0;
		}
		return routes.list.findIndex(it => path.startsWith(it.data.path));
	}

	tabSelect(key) {
		const item = routes.list[key].data;
		router.goto('/sys/home/unit' + item.path);
	}

	render() {
		const content = (
			<Row>
				<Col md={12}>
					<Nav bsStyle="tabs" activeKey={this.tabIndex()}
						onSelect={this.tabSelect}
						className="app-tabs">
						{
							routes.list.map((it, index) =>
								<NavItem key={index} eventKey={index}>
									{it.data.title}
								</NavItem>)
						}
					</Nav>
				</Col>
			</Row>
			);

		return (
			<div>
				<UnitPanel content={content} />
				<RouteView routes={routes} />
			</div>
			);
	}
}

Unit.propTypes = {
	route: React.PropTypes.object
};
