
import React from 'react';
import { Profile, Fluidbar } from '../../components/index';
import SidebarContent from '../sidebar-content';

/** Pages of the public module */
import Home from './home';
import DatePickerExamples from './date-picker-examples';


const sidebar = [
	{
		title: 'Developers playground',
		icon: 'sitemap',
        path: '/index',
        view: Home
	},
	{
		title: 'Calendar',
		view: DatePickerExamples,
		path: '/calendar',
		icon: 'calendar'
	}
];

/**
 * The page controller of the public module
 */
export default class Index extends React.Component {

	render() {
		return (
			<div>
				<Fluidbar>
					<div className="margin-2x">
						<Profile size="large"
							title="Developers playground"
							subtitle="Your place to test new stuff"
							imgClass="prof-male"
							fa="laptop" />
					</div>
				</Fluidbar>
				<SidebarContent menu={sidebar} path="/sys/dev" route={this.props.route} />
			</div>
			);
	}
}

Index.propTypes = {
	// the route object given from the route lib
	route: React.PropTypes.object,
	// the main path of the pages in the admin menu
	path: React.PropTypes.string
};
