
import React from 'react';
import { Tabs, Tab } from 'react-bootstrap';
import { Fluidbar, Card } from '../../../components/index';

/**
 * The page controller of the public module
 */
export default class UserSession extends React.Component {

	constructor(props) {
		super(props);

	}

	render() {
		return (
			<Fluidbar>
				<Tabs defaultActiveKey={2}>
					<Tab eventKey={1} title="Tab 1">{'Tab 1 content'}</Tab>
					<Tab eventKey={2} title="Tab 2">{'Tab 2 content'}</Tab>
				</Tabs>
			</Fluidbar>
		);
	}
}

UserSession.propTypes = {
	route: React.PropTypes.object
};
