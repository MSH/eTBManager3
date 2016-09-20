import React from 'react';
import FrontPage from './commons/front-page';
import { app } from '../../core/app';

import General from './workspace/general';
import Cases from './view/cases';
import Inventory from './view/inventory';


const views = [
	{
		title: __('general'),
		path: '/general',
		view: General,
		default: true
	},
	{
		title: __('cases'),
		path: '/cases',
		view: Cases
	},
	{
		title: __('meds.inventory'),
		path: '/inventory',
		view: Inventory
	}
];

export default class Workspace extends React.Component {

	render() {
		const session = app.getState().session;

		return (
			<FrontPage
				title={session.workspaceName}
				type="ws"
				views={views}
				viewProps={{ scope: 'WORKSPACE' }}
				route={this.props.route}
			/>
			);
	}
}

Workspace.propTypes = {
	route: React.PropTypes.object
};
