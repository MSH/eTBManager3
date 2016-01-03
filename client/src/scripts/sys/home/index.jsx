
import React from 'react';
import { Grid, Row, Col, Collapse, Button, ButtonToolbar } from 'react-bootstrap';
import { Profile, Card, Fluidbar } from '../../components/index';
import CollapseCard from '../../components/collapse-card';
import GridTable from '../../components/grid-table';
import { app } from '../../core/app';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	constructor(props) {
		super(props);
		this.cardClick = this.cardClick.bind(this);
		this.toggleCard = this.toggleCard.bind(this);
		this.editClick = this.editClick.bind(this);
		this.createCell = this.createCell.bind(this);
		this.state = {};
	}

	cardClick(evt) {
		console.log('Hi card', evt);
	}

	toggleCard(evt, key) {
		var show = this.state.show;
		this.setState({ show: !show });
		console.log(evt, key);
	}

	editClick(evt) {
//		console.log(evt);
		evt.stopPropagation();
	}

	createCell(item) {
		const expdata = (
			<div>
				<p>{'This is a simple text to display the habilities to hide and show in an animated fashion'}
				</p>
				<p><b>{'And what the rest?'}</b></p>
				<p>
					{'By the edge of the world, when the rest isn\'t worth'}
				</p>
				<ButtonToolbar>
					<Button bsStyle="warning" onClick={this.editClick}>{'Edit'}</Button>
					<Button bsStyle="link">{'Delete'}</Button>
				</ButtonToolbar>
			</div>
			);

		return (
			<CollapseCard collapsable={expdata} padding="small">
				<Profile title={'UNIT ' + (item + 1)}
					subtitle="Rio de Janeiro, RJ"
					fa="hospital-o"
					size="small"/>
			</CollapseCard>
			);
	}

	render() {
		const state = app.getState();

		const lst = Object.keys(state.session.adminUnit);

		const aus = [];
		let count = 0;
		lst.forEach(key => {
			count++;
			let s = state.session.adminUnit[key].name;
			if (count < lst.length) {
				s += ', ';
			}
			aus.push(<a href="#" key={key}>{s}</a>);
		});

		const subtitle = (
			<div><a href="#">{state.session.unitName}</a>
			<div>{aus}</div>
			<a href="#">{state.session.workspaceName}</a>
			</div>
		);

		const unitName = 'Centro de Referência Professor Hélio Fraga';

		// create temporary cells
		var lst2 = [];
		for (var i = 0; i < 12; i++) {
			lst2.push(i);
		}
		// for (var i = 1; i < 4; i++) {
		// 	lst2.push(
		// 		<Row key={i * 2}>
		// 			<Col md={6}>
		// 				{this.createCell()}
		// 			</Col>
		// 			<Col md={6}>
		// 				{this.createCell()}
		// 			</Col>
		// 		</Row>
		// 		);
		// }

		return (
			<div>
				<Fluidbar>
					<Grid>
						<Row>
							<Col md={12}>
								<div className="margin-2x">
									<Profile title={state.session.userName}
										subtitle={subtitle}
										fa="user" size="large"/>
								</div>
							</Col>
						</Row>
					</Grid>
				</Fluidbar>

				<Grid className="mtop-2x">
					<Row>
						<Col md={8} mdOffset={2}>
							<Card title="This is a list">
								<div>
									<GridTable values={lst2} cellRender={this.createCell} />
									{lst2}
								</div>
							</Card>
						</Col>
					</Row>
					<Row>
						<Col mdOffset={2} md={4}>
							<Card onClick={this.toggleCard}>
								<div>
									<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="medium"/>
									<Collapse in={this.state.show}>
										<p>{'This is a simple text to display the habilities to hide and show in an animated fashion'}
										</p>
									</Collapse>
								</div>
							</Card>
						</Col>
						<Col md={4}>
							<Card>
								<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="medium"/>
							</Card>
						</Col>
					</Row>
					<Row>
						<Col md={8} mdOffset={2}>
						<Card onClick={this.cardClick}>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="large"/>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="medium"/>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="small"/>
						</Card>
						</Col>
					</Row>
				</Grid>
			</div>
			);
	}
}
