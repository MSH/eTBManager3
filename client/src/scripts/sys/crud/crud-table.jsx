import React from 'react';
import { ReactTable, WaitIcon } from '../../components';

export default class CrudTable extends React.Component {
	componentWillMount() {
		const self = this;
		this.props.controller.on((evt, res) => {
			switch (evt) {
				case 'list':
					self.setState({ values: res.list.map(item => item.data) });
					return;
				case 'fetching-list':
					self.forceUpdate();
					return;
				default: return;
			}
		});

		this.setState({ values: null });
	}

	render() {
		if (!this.state.values) {
			return null;
		}

		const controller = this.props.controller;

		return controller.isFetching() ?
			<WaitIcon type="card" /> :
			<ReactTable columns={this.props.columns} values={this.state.values} />;
	}
}

CrudTable.propTypes = {
	controller: React.PropTypes.object,
	columns: React.PropTypes.array
};
