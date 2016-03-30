import React from 'react';
import { ButtonToolbar, Button, Collapse } from 'react-bootstrap';
import { ReactGrid, WaitIcon, AsyncButton, Card } from '../../components';
import CrudForm from './crud-form';


export default class CrudGrid extends React.Component {
	constructor(props) {
		super(props);

		this.expandRender = this.expandRender.bind(this);
		this.editClick = this.editClick.bind(this);
		this.deleteClick = this.deleteClick.bind(this);
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

	/**
	 * Called when user clicks on the edit button
	 */
	editClick(item, cell) {
		return evt => {
			evt.preventDefault();

			if (this.edtitem) {
				return;
			}

			const controller = this.props.controller;

			controller
				.openForm(item)
				.then(() => {
					cell.setSize({ sm: 12 });

					const handler = controller.on(e => {
						if (e === 'close-form') {
							controller.removeListener(handler);
							cell.setSize(null);
						}
					});
				})
				.catch(() => cell.forceUpdate());

			cell.forceUpdate();
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

	expandRender(item, cell) {
		const content = this.props.onExpandRender ? this.props.onExpandRender(item, cell) : null;
		const controller = this.props.controller;

		return (
			<div>
				{content}
				<ButtonToolbar className="mtop">
					<AsyncButton bsStyle="primary"
						fetching={controller.formInfo && controller.formInfo.fetching}
						onClick={this.editClick(item, cell)}>
						{__('action.edit')}
					</AsyncButton>
					<Button bsStyle="link"
						onClick={this.deleteClick(item, cell)}>
						{__('action.delete')}
					</Button>
				</ButtonToolbar>
			</div>
			);
	}

	cellRender(item) {
		const controller = this.props.controller;
		const fi = controller.formInfo;

		// item is being edited ?
		if (!fi || fi.item !== item || fi.fetching || fi.closing) {
			return null;
		}

		// display cell for editing
		return (
			<Collapse in transitionAppear>
				<Card highlight>
					<CrudForm schema={this.props.editorSchema}
						controller={controller} openOnEdit cardWrap={false} />
				</Card>
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
			<ReactGrid
				values={controller.getList()}
				onCellRender={this.cellRender}
				onCollapseRender={this.props.onRender}
				onExpandRender={this.expandRender} />;
	}
}

CrudGrid.propTypes = {
	controller: React.PropTypes.object,
	onRender: React.PropTypes.func,
	onExpandRender: React.PropTypes.func,
	editorSchema: React.PropTypes.object
};
