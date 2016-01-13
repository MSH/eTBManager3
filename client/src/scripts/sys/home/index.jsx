
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Card, Fluidbar, WaitIcon } from '../../components/index';
import Popup from '../../components/popup';
import { app } from '../../core/app';
import { WORKSPACE_CHANGE, WORKSPACE_CHANGING } from '../../core/actions';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	constructor(props) {
		super(props);
		this.toggleCard = this.toggleCard.bind(this);
		this.editClick = this.editClick.bind(this);
		this.itemClick = this.itemClick.bind(this);
//		this.createCell = this.createCell.bind(this);

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

	toggleCard() {
		var show = this.state.show;
		this.setState({ show: !show });
	}

	editClick(evt) {
		evt.stopPropagation();
	}

	cellSize(item) {
		return item === 3 ? { md: 12 } : { md: 6 };
	}

	itemClick(evt) {
		console.log(evt);
		this.refs.pop1.preventHide();
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
			<div><a href="#">{session.unitName}</a>
			<div>{aus}</div>
			<a href="#">{session.workspaceName}</a>
			</div>
		);

		const unitName = 'Centro de Referência Professor Hélio Fraga';

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

				<Grid className="mtop-2x">
					<Row>
						<Col md={8} mdOffset={2}>
						<Popup ref="pop1">
							<h3 onClick={this.itemClick} >{'Test 1'}</h3>
							<h3 onClick={this.itemClick} >{'Test 2'}</h3>
						</Popup>
						<Popup ref="pop2">
							<h3 onClick={this.itemClick} >{'Test 3'}</h3>
							<h3 onClick={this.itemClick} >{'Test 4'}</h3>
						</Popup>
						<Card>
							<Profile title={unitName}
								subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="large"
								imgClass="prof-tbunit"
								/>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="medium"/>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="small"/>
						</Card>
						</Col>
					</Row>
					<Row>
						<Col md={8} mdOffset={2}>
							<Card>
								<Profile title="Male" subtitle="A male example" type="male"/>
							</Card>
							<Card>
								<Profile title="Female" subtitle="A female example" type="female"/>
							</Card>
							<Card>
								<Profile title="TB unit" subtitle="The location of the hospital" type="tbunit"/>
							</Card>
							<Card>
								<Profile title="Laboratory" subtitle="The location of the laboratory" type="lab" />
							</Card>
							<Card>
								<Profile title="Workspace" subtitle="The location of the workspace" type="ws"/>
							</Card>
						</Col>
					</Row>
				</Grid>
			</div>
			);
	}
}
