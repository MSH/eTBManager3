import React from 'react';
import { ReactGrid, WaitIcon } from '../../components';
import CrudCell from './crud-cell';
import controlWrapper from './crud-control-wrapper';


class CrudGrid extends React.Component {
	constructor(props) {
		super(props);

		this.cellRender = this.cellRender.bind(this);
	}

	eventHandler(evt) {
		if (evt === 'list' || evt === 'fetching-list') {
			this.forceUpdate();
		}
	}

	cellRender(item, cell) {
		const controller = this.props.controller;
		const id = controller.resolveId(item);

		return (
			<CrudCell cell={cell}
				options={this.props.options}
				id={id}
				controller={controller}
				onRender={this.props.onRender}
				onExpandRender={this.props.onExpandRender}
				editorSchema={this.props.editorSchema} />
			);
	}

	render() {
		const controller = this.props.controller;

		if (controller.isFetching()) {
			return <WaitIcon type="card" />;
		}

		if (!controller.getList()) {
			return null;
		}

		return (
			<ReactGrid
				values={controller.getList()}
				onCellRender={this.cellRender}
				cellSize={this.props.cellSize} />
			);
	}
}

CrudGrid.propTypes = {
	controller: React.PropTypes.object,
	onRender: React.PropTypes.func,
	onExpandRender: React.PropTypes.func,
	editorSchema: React.PropTypes.object,
	cellSize: React.PropTypes.object,
	options: React.PropTypes.array
};

export default controlWrapper(CrudGrid);

