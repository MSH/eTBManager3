import React from 'react';
import { ButtonToolbar, Button, Collapse } from 'react-bootstrap';
import CrudController from './crud-controller';
import CrudForm from './crud-form';
import { Card, Expandable, AsyncButton } from '../../components';


/**
 * Component to control the displaying inside of a cell in a react grid
 */
export default class CrudCell extends React.Component {

	constructor(props) {
		super(props);

		this.expandRender = this.expandRender.bind(this);
		this.editClick = this.editClick.bind(this);
		this.deleteClick = this.deleteClick.bind(this);
	}

	componentWillMount() {
		const controller = this.props.controller;

		const self = this;
		// get the handler to the controller event handler
		const handler = controller.on((evt, data) => {
			if (data && data.id !== self.props.id) {
				return;
			}

			switch (evt) {
				case 'close-form':
					self.props.cell.setSize(null);
					return;
				case 'item-updated':
					self.setState({ item: controller.itemById(data.id) });
					return;
				case 'open-form':
					self.forceUpdate();
					return;
				default:
					return;
			}
		});

		// resolve the item given by the controller
		const item = controller.itemById(this.props.id);
		if (__DEV__) {
			if (!item) {
				throw new Error('Item not found');
			}
		}

		this.setState({ item: item, handler: handler });
	}

	componentWillReceiveProps(props) {
		this.setState({ item: props.controller.itemById(props.id) });
	}

	componentWillUnmount() {
		this.props.controller.removeListener(this.state.handler);
	}

	/**
	 * Called when user clicks on the edit button
	 */
	editClick(evt) {
		evt.preventDefault();

		const cell = this.props.cell;
		const item = this.state.item;

		const controller = this.props.controller;

		controller
			.openForm(item)
			.then(() => {
				cell.setSize({ sm: 12 });
			})
			.catch(() => cell.forceUpdate());

		this.forceUpdate();
	}

	/**
	 * Called when user clicks on the delete button
	 */
	deleteClick(evt) {
		evt.preventDefault();

		this.props.controller.initDelete(this.state.item);
	}

	/**
	 * Render the content of the expandable area when user clicks on the cell
	 * @return {React.Component} The content of the expandable area
	 */
	expandRender() {
		const item = this.state.item;
		const cell = this.props.cell;

		const content = this.props.onExpandRender ? this.props.onExpandRender(item, cell) : null;
		const controller = this.props.controller;

		return (
			<div>
				{content}
				<ButtonToolbar className="mtop">
					<AsyncButton bsStyle="primary"
						fetching={controller.frm && controller.frm.fetching}
						onClick={this.editClick}>
						{__('action.edit')}
					</AsyncButton>
					<Button bsStyle="link"
						onClick={this.deleteClick}>
						{__('action.delete')}
					</Button>
				</ButtonToolbar>
			</div>
			);
	}


	render() {
		const controller = this.props.controller;

		// get the form id being edited
		if (controller.getFormItemId() === this.props.id && !controller.frm.fetching) {
			return (
				<Collapse in transitionAppear>
					<CrudForm schema={this.props.editorSchema} className="highlight"
						controller={controller} openOnEdit wrapType="card" />
				</Collapse>
				);
		}

		// return the content to be displayed
		return (
			<Card className="collapse-card" padding="small">
				<Expandable onExpandRender={this.expandRender}>
					{this.props.onRender(this.state.item)}
				</Expandable>
			</Card>
			);
	}
}

CrudCell.propTypes = {
	id: React.PropTypes.any.isRequired,
	controller: React.PropTypes.instanceOf(CrudController).isRequired,
	cell: React.PropTypes.any,
	onRender: React.PropTypes.func,
	onExpandRender: React.PropTypes.func,
	editorSchema: React.PropTypes.object
};
