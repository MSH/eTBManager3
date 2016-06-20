import React from 'react';
import FrontPage from './commons/front-page';
import { app } from '../../core/app';
import { server } from '../../commons/server';
import { WaitIcon } from '../../components';
import SessionUtils from '../session-utils';

import UnderConstruction from './under-construction';


const views = [
	{
		title: __('general'),
		path: '/general',
		view: UnderConstruction,
		default: true
	},
	{
		title: __('cases'),
		path: '/cases',
		view: UnderConstruction
	},
	{
		title: __('meds.inventory'),
		path: '/inventory',
		view: UnderConstruction
	}
];

export default class AdminUnit extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
	}

	componentWillMount() {
		const id = this.props.route.queryParam('id');
		this.fetchData(id);
	}

	fetchData(id) {
		const self = this;

		// get data of the unit
		server.get('/api/tbl/adminunit/' + id)
		.then(res => self.setState({ data: res }));
	}

	render() {
		const au = this.state.data;

		if (!au) {
			return <WaitIcon />;
		}

		return (
			<FrontPage
				title={au.name}
				subtitle={SessionUtils.adminUnitDisplay(au.parents, true)}
				type="place"
				views={views}
				route={this.props.route}
			/>
			);
	}
}

AdminUnit.propTypes = {
	route: React.PropTypes.object
};
