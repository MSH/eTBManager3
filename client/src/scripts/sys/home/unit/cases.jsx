import React from 'react';
import { Button, ButtonGroup, Grid, Row, Col, OverlayTrigger, Popover, Nav, NavItem, Badge } from 'react-bootstrap';
import { Card, Profile } from '../../../components';


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

const presumptives = [
	{
		name: 'Bruce Dickinson',
		id: '123456',
		recordNumber: '123456-789-01',
		gender: 'MALE',
		recordDate: '21-jan-2016',
		xpertResult: {
			id: 'TB_DETECT',
			name: 'TB Detected'
		},
		micResult: {
			id: 'POSITIVE',
			name: 'Positive'
		}
	},
	{
		name: 'Bruce Dickinson',
		id: '123456',
		recordNumber: '123456-789-01',
		gender: 'MALE',
		recordDate: '21-jan-2016',
		xpertResult: {
			id: 'TB_DETECT',
			name: 'TB Detected'
		},
		micResult: {
			id: 'POSITIVE',
			name: 'Positive'
		}
	},
	{
		name: 'Bruce Dickinson',
		id: '123456',
		recordNumber: '123456-789-01',
		gender: 'MALE',
		recordDate: '21-jan-2016',
		xpertResult: {
			id: 'TB_DETECT',
			name: 'TB Detected'
		},
		micResult: {
			id: 'POSITIVE',
			name: 'Positive'
		}
	},
	{
		name: 'Donna Summer',
		id: '123456',
		recordNumber: '123456-789-01',
		gender: 'FEMALE',
		recordDate: '21-jan-2016',
		xpertResult: {
			id: 'TB_DETECT',
			name: 'TB Detected'
		},
		micResult: {
			id: 'POSITIVE',
			name: 'Positive'
		}
	},
	{
		name: 'Bruce Dickinson',
		id: '123456',
		recordNumber: '123456-789-01',
		gender: 'MALE',
		recordDate: '21-jan-2016',
		xpertResult: {
			id: 'TB_DETECT',
			name: 'TB Detected'
		},
		micResult: {
			id: 'POSITIVE',
			name: 'Positive'
		}
	},
	{
		name: 'Maria da Silva',
		id: '123456',
		recordNumber: '123456-789-01',
		gender: 'FEMALE',
		recordDate: '21-jan-2016',
		xpertResult: {
			id: 'TB_DETECT',
			name: 'TB Detected'
		},
		micResult: {
			id: 'POSITIVE',
			name: 'Positive'
		}
	},
	{
		name: 'Bruce Dickinson',
		id: '123456',
		recordNumber: '123456-789-01',
		gender: 'MALE',
		recordDate: '21-jan-2016',
		xpertResult: {
			id: 'TB_DETECT',
			name: 'TB Detected'
		},
		micResult: {
			id: 'POSITIVE',
			name: 'Positive'
		}
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
						<div>
						<Nav bsStyle="tabs" activeKey={0}
							className="app-tabs"
							onSelect={this.tabSelect}>
							<NavItem key={0} eventKey={0}>{'Presumptive'}</NavItem>
							<NavItem key={1} eventKey={1}>{'TB Cases'}</NavItem>
							<NavItem key={2} eventKey={2}>{'DR-TB Cases'}</NavItem>
						</Nav>
						<Row className="mtop-2x title">
							<Col sm={4}>
								{'Patient'}
							</Col>
							<Col sm={2}>
								{'Registration date'}
							</Col>
							<Col sm={2}>
								{'Xpert'}
							</Col>
							<Col sm={2}>
								{'Microscopy'}
							</Col>
						</Row>
						{
							presumptives.map(item =>
								<Row className="tbl-cell" style={{ padding: '10px 4px' }}>
									<Col sm={4}>
										<Profile type={item.gender.toLowerCase()} size="small"
											title={item.name} subtitle={item.recordNumber} />
									</Col>
									<Col sm={2}>
										{'24-jan-2016'}
									</Col>
									<Col sm={2}>
										{'Positive'}
									</Col>
									<Col sm={2}>
										{'TB Positive'}
									</Col>
								</Row>
								)
						}
						</div>
					</Card>
				</Col>
			</Row>
			</Grid>
			);
	}
}
