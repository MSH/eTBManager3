
import React from 'react';
import { Grid, Row, Col, Button } from 'react-bootstrap';
import { Profile, Card, Fluidbar, WaitIcon, Sidebar } from '../../components/index';
import { app } from '../../core/app';
import { WORKSPACE_CHANGE, WORKSPACE_CHANGING } from '../../core/actions';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	constructor(props) {
		super(props);
		this.editClick = this.editClick.bind(this);

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
			return this.setState({ session: data.session });
		}

		if (action === WORKSPACE_CHANGING) {
			return this.setState({ session: null });
		}
	}

	editClick(evt) {
		evt.stopPropagation();
	}

	cellSize(item) {
		return item === 3 ? { md: 12 } : { md: 6 };
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

		const aulist = lst.map(k => session.adminUnit[k].name);

		const subtitle = (
			<div><a href="#">{session.unitName}</a>
			<div>{aus}</div>
			<a href="#">{session.workspaceName}</a>
			</div>
		);

		// create temporary cells
		var lst2 = [];
		for (var i = 0; i < 12; i++) {
			lst2.push(i);
		}

		const items = [
			{
				title: 'Dashboard',
				icon: 'dashboard'
			},
			{
				title: 'My activities',
				icon: 'history'
			}
		];

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

				<Grid className="mtop-2x" fluid>
					<Row>
						<Col md={2}>
							<Sidebar items={items} selected={items[0]} />
						</Col>
						<Col md={10}>
						<Card>
							<div>
								<Profile title={session.unitName}
									subtitle={aulist.join(', ')}
									size="small"
									type="tbunit"
									/>
								<Row className="mtop-2x">
									<Col sm={6}>
										<Card title="Cases" className="card-indicator">
											<Row>
												<Col xs={6}>
													<div className="ind-value text-primary">{121}</div>
													<div className="ind-label">{'Presumptives'}</div>
												</Col>
												<Col xs={6}>
													<div className="ind-value text-primary">{78}</div>
													<div className="ind-label">{'Cases on treatment'}</div>
												</Col>
											</Row>
										</Card>
									</Col>
									<Col sm={6}>
										<Card title="Inventory" className="card-indicator">
											<Row>
												<Col xs={6}>
													<div className="ind-value text-danger">{'28 days'}</div>
													<div className="ind-label">{'Estimated stock-out'}</div>
												</Col>
												<Col xs={6}>
													<div className="ind-value text-primary">{'84 days'}</div>
													<div className="ind-label">{'First batch to expire'}</div>
												</Col>
											</Row>
										</Card>
									</Col>
								</Row>
							</div>
						</Card>
						</Col>
					</Row>
				</Grid>
			</div>
			);
	}
}
