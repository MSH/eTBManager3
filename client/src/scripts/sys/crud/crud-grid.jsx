import React from 'react';
import { ReactGrid, WaitIcon } from '../../components';
import CrudCell from './crud-cell';


export default class CrudGrid extends React.Component {
	constructor(props) {
		super(props);

		// this.expandRender = this.expandRender.bind(this);
		// this.editClick = this.editClick.bind(this);
		// this.deleteClick = this.deleteClick.bind(this);
		this.cellRender = this.cellRender.bind(this);
	}

	componentWillMount() {
		const self = this;
		const handler = this.props.controller.on((evt) => {
			if (evt === 'list' || evt === 'fetching-list') {
				self.forceUpdate();
			}
		});

		this.setState({ values: null, handler: handler });
	}

	componentWillUnmount() {
		this.props.controller.removeListener(this.state.handler);
	}

	cellRender(item, cell) {
		const controller = this.props.controller;
		const id = controller.resolveId(item);

		return (
			<CrudCell cell={cell}
				id={id}
				controller={controller}
				onRender={this.props.onRender}
				onExpandRender={this.props.onExpandRender}
				editorSchema={this.props.editorSchema} />
			);
	}

	render() {
		const controller = this.props.controller;

		if (!controller.getList()) {
			return null;
		}

		return controller.isFetching() ?
			<WaitIcon type="card" /> :
			<ReactGrid
				values={controller.getList()}
				onCellRender={this.cellRender}
				cellSize={this.props.cellSize} />;
	}
}

CrudGrid.propTypes = {
	controller: React.PropTypes.object,
	onRender: React.PropTypes.func,
	onExpandRender: React.PropTypes.func,
	editorSchema: React.PropTypes.object,
	cellSize: React.PropTypes.object
};
