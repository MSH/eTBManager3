import React from 'react';
import { ButtonToolbar, Button, Collapse } from 'react-bootstrap';
import { ReactTable, WaitIcon, AsyncButton } from '../../components';
import CrudForm from './crud-form';


export default class CrudTable extends React.Component {
	constructor(props) {
		super(props);

		this.expandRender = this.expandRender.bind(this);
		this.editClick = this.editClick.bind(this);
		this.deleteClick = this.deleteClick.bind(this);
		this.rowRender = this.rowRender.bind(this);
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

	/**
	 * Called when user clicks on the edit button
	 */
	editClick(item, row) {
		return evt => {
			evt.preventDefault();

			if (this.edtitem) {
				return;
			}

			const controller = this.props.controller;

			controller
				.openForm(item)
				.then(() => {
					row.forceUpdate();

					const handler = controller.on(e => {
						if (e === 'close-form') {
							controller.removeListener(handler);
							row.forceUpdate();
						}
					});
				})
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
			this.props.controller.initDelete(item);
		};
	}

	expandRender(item, row) {
		const content = this.props.onExpandRender ? this.props.onExpandRender(item, row) : null;
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
		const fi = controller.formInfo;

		// item is being edited ?
		if (!fi || fi.item !== item || fi.fetching || fi.closing) {
			return null;
		}

		// display cell for editing
		return (
			<Collapse in transitionAppear>
				<div className="row card-content highlight">
				<CrudForm schema={this.props.editorSchema}
					controller={controller} openOnEdit wrapType={'none'} />
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
				onExpandRender={this.expandRender}
				onRowRender={this.rowRender} />;
	}
}

CrudTable.propTypes = {
	controller: React.PropTypes.object,
	columns: React.PropTypes.array,
	onExpandRender: React.PropTypes.func,
	editorSchema: React.PropTypes.object
};
