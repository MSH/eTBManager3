import React from 'react';
import { Profile } from '../../../components';
import { CrudView } from '../../crud';
import CRUD from '../../../commons/crud';

export default class SysSetup extends React.Component {

	componentWillMount() {
		const editor = {
			layout: [
			{
				type: 'string',
				property: 'name',
				size: { sm: 6 },
				required: true
			}
			]
		};

		this.setState({ crud: new CRUD('workspace'), editor: editor });
	}

	cellRender(item) {
		return <Profile title={item.name} type="ws" size="small" />;
	}

	render() {
		return (
			<CrudView
				title={this.props.route.data.title}
				onCellRender={this.cellRender}
				crud={this.state.crud}
				editorSchema={this.state.editor}
				perm="WORKSPACES" />
			);
	}
}

SysSetup.propTypes = {
	route: React.PropTypes.object
};
