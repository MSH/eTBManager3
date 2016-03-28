import React from 'react';
import { ButtonToolbar, Button, Collapse } from 'react-bootstrap';
import { ReactTable, WaitIcon, AsyncButton } from '../../components';
import CrudForm from './crud-form';


export default class CrudTable extends React.Component {
	constructor(props) {
		super(props);

		this.collapseRender = this.collapseRender.bind(this);
		this.editClick = this.editClick.bind(this);
		this.deleteClick = this.deleteClick.bind(this);
		this.rowRender = this.rowRender.bind(this);
	}

	componentWillMount() {
		const self = this;
		const handler = this.props.controller.on((evt) => {
			switch (evt) {
				case 'list':
				case 'fetching-list':
					self.forceUpdate();
					return;
				default: return;
			}
		});

		this.setState({ values: null, handler: handler });
	}

	componentWillUnmount() {
		this.props.controller.removeListener(this.state.handler);
	}

	/**
	 * Called when user clicks on the edit button
	 */
	editClick(item, row) {
		return evt => {
			evt.preventDefault();

			if (this.edtitem) {
				return;
			}

			this.props
				.controller
				.openForm(item)
				.then(() => row.forceUpdate())
				.catch(() => row.forceUpdate());

			row.forceUpdate();
		};
	}


	/**
	 * Called when user clicks on the delete button
	 */
	deleteClick(item) {
		return evt => {
			evt.preventDefault();
			console.log('delete', item);
		};
	}

	collapseRender(item, row) {
		const content = this.props.onCollapseRender ? this.props.onCollapseRender(item, row) : null;
		const controller = this.props.controller;

		return (
			<div>
				{content}
				<ButtonToolbar className="mtop">
					<AsyncButton bsStyle="primary"
						fetching={controller.formInfo && controller.formInfo.fetching}
						onClick={this.editClick(item, row)}>
						{__('action.edit')}
					</AsyncButton>
					<Button bsStyle="link"
						onClick={this.deleteClick(item, row)}>
						{__('action.delete')}
					</Button>
				</ButtonToolbar>
			</div>
			);
	}

	rowRender(item) {
		const controller = this.props.controller;
		// item is being edited ?
		if (!controller.formInfo || controller.formInfo.item !== item || (controller.formInfo.fetching)) {
			return null;
		}

		// display cell for editing
		return (
			<Collapse in transitionAppear>
				<div className="row card-content highlight">
				<CrudForm schema={this.props.editorSchema}
					controller={controller} openOnEdit cardWrap={false} />
				</div>
			</Collapse>
			);
	}

	render() {
		const controller = this.props.controller;

		if (!controller.getList()) {
			return null;
		}

		return controller.isFetching() ?
			<WaitIcon type="card" /> :
			<ReactTable columns={this.props.columns}
				values={controller.getList()}
				onCollapseRender={this.collapseRender}
				onRowRender={this.rowRender}
				/>;
	}
}

CrudTable.propTypes = {
	controller: React.PropTypes.object,
	columns: React.PropTypes.array,
	onCollapseRender: React.PropTypes.func,
	editorSchema: React.PropTypes.object
};
