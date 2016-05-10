
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar, WaitIcon } from '../../components/index';
import { app } from '../../core/app';
import SidebarContent from '../sidebar-content';
import { WORKSPACE_CHANGE, WORKSPACE_CHANGING } from '../../core/actions';

import Dashboard from './index-dashboard';
import MyActivities from './index-my-activities';


const menu = [
	{
		title: 'Dashboard',
		icon: 'dashboard',
		default: true,
		path: '/dashboard',
		view: Dashboard
	},
	{
		title: 'My activities',
		icon: 'history',
		path: '/myactivities',
		view: MyActivities
	}
];


/**
 * The page controller of the public module
 */
export default class Index extends React.Component {

	constructor(props) {
		super(props);
		this._appEvent = this._appEvent.bind(this);

		this.state = { session: app.getState().session };
	}


	componentDidMount() {
		// set handler to receive information about actions
		app.add(this._appEvent);
	}

	componentWillUmount() {
		app.remove(this._appEvent);
	}

	_appEvent(action, data) {
		if (action === WORKSPACE_CHANGE) {
			this.setState({ session: data.session });
			return;
		}

		if (action === WORKSPACE_CHANGING) {
			this.setState({ session: null });
			return;
		}
	}

	render() {
		const session = this.state.session;

		if (!session) {
			return <WaitIcon />;
		}

		const lst = Object.keys(session.adminUnit);

		const aus = [];
		let count = 0;
		lst.forEach(key => {
			count++;
			let s = session.adminUnit[key].name;
			if (count < lst.length) {
				s += ', ';
			}
			aus.push(<a href="#" key={key}>{s}</a>);
		});

		const subtitle = (
			<div><a href="#/sys/home/unit">{session.unitName}</a>
			<div>{aus}</div>
			<a href="#">{session.workspaceName}</a>
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
									<Profile title={session.userName}
										subtitle={subtitle}
										fa="user" size="large"
										imgClass="prof-male"
										/>
								</div>
							</Col>
						</Row>
					</Grid>
				</Fluidbar>
				<SidebarContent menu={menu} path="/sys/home/index" route={this.props.route} />
			</div>
			);
	}
}

Index.propTypes = {
	route: React.PropTypes.object
};
