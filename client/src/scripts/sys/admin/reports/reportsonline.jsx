
import React from 'react';
import { Col, Grid, Row, Badge } from 'react-bootstrap';
import { Card, CollapseRow } from '../../../components/index';

const onlineUsers = [
	{
		name: 'Mauricio Santos1',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS1',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		lastAccess: 'Feb 25, 2016 9:11:19 AM EST',
		lastPage: 'index.html',
		idleTime: '2 minutes and 45 seconds'
	},
	{
		name: 'Mauricio Santos2',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS2',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		lastAccess: 'Feb 25, 2016 9:11:19 AM EST',
		lastPage: 'index.html',
		idleTime: '2 minutes and 45 seconds'
	},
	{
		name: 'Mauricio Santos3',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS4',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		lastAccess: 'Feb 25, 2016 9:11:19 AM EST',
		lastPage: 'index.html',
		idleTime: '2 minutes and 45 seconds'
	},
	{
		name: 'Mauricio Santos4',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS4',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		lastAccess: 'Feb 25, 2016 9:11:19 AM EST',
		lastPage: '/management/treatoutcome.seam',
		idleTime: '2 minutes and 45 seconds'
	},
	{
		name: 'Mauricio Santos5',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS5',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		lastAccess: 'Feb 25, 2016 9:11:19 AM EST',
		lastPage: 'index.html',
		idleTime: '2 minutes and 45 seconds'
	},
	{
		name: 'Mauricio Santos6',
		sessionTime: '2 hours and 30 minutes',
		login: 'MSANTOS6',
		loginDate: 'Feb 25, 2016 9:11:19 AM EST',
		lastAccess: 'Feb 25, 2016 9:11:19 AM EST',
		lastPage: 'index.html',
		idleTime: '2 minutes and 45 seconds'
	}
];

/**
 * The page controller of the public module
 */
export default class ReportsOnline extends React.Component {

	constructor(props) {
		super(props);
	}

	parseDetails(item) {
		const collapsedValue = (<div className="text-small">
									<dl>
										<Col sm={4}>
											<dt>{__('admin.websessions.lastrequest') + ':'}</dt>
											<dd>{item.lastAccess}</dd>
										</Col>
										<Col sm={4}>
											<dt>{__('admin.websessions.lastpage') + ':'}</dt>
											<dd>{item.lastPage}</dd>
										</Col>
										<Col sm={4}>
											<dt>{__('admin.websessions.sessiontime') + ':'}</dt>
											<dd>{item.sessionTime}</dd>
										</Col>
									</dl>
								</div>);
		return (collapsedValue);
	}

	headerRender() {
		const countHTML = <Badge className="tbl-counter">{6}</Badge>;

		// create the header of the card
		return (
			<Row>
				<Col sm={12}>
					<h4>{__('admin.websessions')}{countHTML}</h4>
				</Col>
			</Row>
			);
	}

	render() {
		const list = onlineUsers.map(item => ({ data: item, detailsHTML: this.parseDetails(item) }));

		const header = this.headerRender();

		return (
			<Card header={header}>
				<Grid className="mtop-2x table">
					<Row className="title">
						<Col md={4} className="nopadding">
							{__('User')}
						</Col>

						<Col md={4} className="nopadding">
							{__('UserLogin.loginDate')}
						</Col>

						<Col md={4} className="nopadding">
							{__('admin.websessions.idletime')}
						</Col>
					</Row>

					{
						list.map(item => (
						<CollapseRow collapsable={item.detailsHTML} className={'tbl-cell'}>
							<Col md={4}>
								{item.data.name + ' (' + item.data.login + ')'}
							</Col>

							<Col md={4}>
								{item.data.loginDate}
							</Col>

							<Col md={4}>
								{item.data.idleTime}
							</Col>
						</CollapseRow>
						))
					}

				</Grid>
			</Card>
		);
	}
}

ReportsOnline.propTypes = {
	route: React.PropTypes.object
};
