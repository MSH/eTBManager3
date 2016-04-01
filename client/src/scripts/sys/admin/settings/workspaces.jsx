import React from 'react';
import { Card, FormDialog } from '../../../components';

export default class SysSetup extends React.Component {

	render() {
		return (
			<Card title={this.props.route.data.title}>
				
			</Card>
			);
	}
}

SysSetup.propTypes = {
	route: React.PropTypes.object
};
