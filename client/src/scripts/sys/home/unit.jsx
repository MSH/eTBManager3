
import React from 'react';
import { Grid, Row, Col, Nav, NavItem } from 'react-bootstrap';
import { Profile, Fluidbar } from '../../components/index';
import { app } from '../../core/app';
import { RouteView, router } from '../../components/router';


import General from './unit/general';
import Cases from './unit/cases';
import Inventory from './unit/inventory';


const routes = RouteView.createRoutes([
	{
		title: 'General',
		path: '/general',
		view: General,
		default: true
	},
	{
		title: 'Cases',
		path: '/cases',
		view: Cases
	},
	{
		title: 'Inventory',
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
		const session = app.getState().session;

		const lst = [];
		const keys = Object.keys(session.adminUnit);
		keys.forEach((k, index) => {
				lst.push(<a key={index} href="/sys/home/admunit">{session.adminUnit[k].name}</a>);
				if (index < keys.length - 1) {
					lst.push(<span key={'s' + index}>{', '}</span>);
				}
			});

		const subtitle = (
			<div>
				{lst}
				<br/>
				<a>{session.workspaceName}</a>
			</div>
		);

		// create temporary cells
		var lst2 = [];
		for (var i = 0; i < 12; i++) {
			lst2.push(i);
		}

		return (
			<div>
				<Fluidbar>
					<Grid>
						<Row>
							<Col md={12}>
								<div className="margin-2x">
									<Profile title={session.unitName}
										subtitle={subtitle}
										type="tbunit"
										size="large"
										/>
								</div>
							</Col>
						</Row>
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
					</Grid>
				</Fluidbar>
				<RouteView routes={routes} />
			</div>
			);
	}
}

Unit.propTypes = {
	route: React.PropTypes.object
};
