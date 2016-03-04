import React from 'react';
import { Button, ButtonGroup, Grid, Row, Col, OverlayTrigger, Popover, Nav, NavItem, Badge } from 'react-bootstrap';
import { Card } from '../../../components';


const tags = [
	{
		name: 'Not on treatment',
		type: 'userdef',
		count: 14
	},
	{
		name: 'On treatment',
		type: 'userdef',
		count: 120
	},
	{
		name: 'Closed cases',
		type: 'warn',
		count: 243
	},
	{
		name: 'DR-TB with no resistance with a long name that someone included',
		type: 'danger',
		count: 5
	},
	{
		name: 'TB with resistance',
		type: 'danger',
		count: 8
	}
];

export default class Cases extends React.Component {

	render() {

		const popup = (
			<Popover id="ppmenu" title={'Notify'}>
				<ButtonGroup vertical style={{ minWidth: '200px' }}>
					<Button bsStyle="link">{'Presumptive'}</Button>
					<Button bsStyle="link">{'TB Case'}</Button>
					<Button bsStyle="link">{'DR-TB Case'}</Button>
				</ButtonGroup>
			</Popover>
			);

		return (
			<Grid fluid>
			<Row className="mtop">
				<Col sm={3}>
					<OverlayTrigger trigger="click" placement="right"
						overlay={popup}
						rootClose="true">
						<Button bsStyle="danger" style={{ padding: '8px 40px' }}>{'Notify'}</Button>
					</OverlayTrigger>

					<Card className="mtop" title="Tags">
						<div>
							{
								tags.map(item => (
									<a className={'tag-link tag-' + item.type}>
										<Badge pullRight>{item.count}</Badge>
										<div className="tag-title">{item.name}</div>
									</a>
								))
							}
						</div>
					</Card>
				</Col>
				<Col sm={9}>
					<Card title="Cases">
						<Nav bsStyle="tabs" activeKey={0}
							onSelect={this.tabSelect}>
							<NavItem key={0} eventKey={0}>{'Presumptive'}</NavItem>
							<NavItem key={1} eventKey={1}>{'TB Cases'}</NavItem>
							<NavItem key={2} eventKey={2}>{'DR-TB Cases'}</NavItem>
						</Nav>
					</Card>
				</Col>
			</Row>
			</Grid>
			);
	}
}
