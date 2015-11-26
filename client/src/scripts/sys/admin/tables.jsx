
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Card, Fluidbar, Sidebar } from '../../components/index';


/**
 * The page controller of the public module
 */
export default class Tables extends React.Component {

	render() {
		const unitName = 'Centro de Referência Professor Hélio Fraga';

		let style = {
			minHeight: '550px',
			marginLeft: '-15px'
		};

		const items = [
			{
				caption: __('admin.adminunits'),
				perm: 'ADMINUNITS',
				icon: 'sitemap'
			},
			{
				caption: __('admin.tbunits'),
				perm: 'UNITS',
				icon: 'hospital-o'
			},
			{
				caption: __('admin.labs'),
				perm: 'UNITS',
				icon: 'building'
			},
			{
				caption: __('admin.sources'),
				perm: 'SOURCES',
				icon: 'dropbox'
			},
			{
				caption: __('admin.substances'),
				perm: 'SUBSTANCES',
				icon: 'h-square'
			},
			{
				caption: __('admin.products'),
				perm: 'PRODUCTS',
				icon: 'cube'
			},
			{
				caption: __('admin.regimens'),
				perm: 'REGIMENS',
				icon: 'medkit'
			},
			{
				separator: true
			},
			{
				caption: __('admin.weeklyfreq'),
				perm: 'WEEKFREQ',
				icon: 'calendar'
			},
			{
				caption: __('admin.ageranges'),
				perm: 'AGERANGES',
				icon: 'tasks'
			},
			{
				caption: __('admin.tags'),
				perm: 'TAGS',
				icon: 'tags'
			},
			{
				separator: true
			},
			{
				caption: __('admin.users'),
				perm: 'USERS',
				icon: 'user'
			},
			{
				caption: __('admin.profiles'),
				perm: 'PROFILES',
				icon: 'group'
			}
		];


		return (
			<div>
				<Fluidbar>
					<h3>{'Administration - Tables'}</h3>
				</Fluidbar>
				<Grid fluid>
					<Row>
						<Col md={3}>
							<Sidebar items={items} />
						</Col>
						<Col md={9}>
							<Card>
								<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="plus" size="large"/>
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

Tables.propTypes = {
	app: React.PropTypes.object
};
