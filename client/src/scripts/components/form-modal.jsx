import React from 'react';
import { Modal, Button } from 'react-bootstrap';


export default class FormModal extends React.Component { //TODOMSR: destruir este componente e colocar esse contexto dentro do form-dialog

	constructor(props) {
		super(props);
		this.yesClick = this.yesClick.bind(this);
		this.noClick = this.noClick.bind(this);
		this.saveClick = this.saveClick.bind(this);
		this.cancelClick = this.cancelClick.bind(this);
		this.closeClick = this.closeClick.bind(this);
		this.customClick = this.customClick.bind(this);
	}

	createButtons() {
		switch (this.props.type) {
			case 'YesNo': return (
				<p className="text-center">
					<Button bsSize="large" bsStyle="primary"
						onClick={this.yesClick}>{__('global.yes')}</Button>
					<Button bsSize="large" bsStyle="default"
						onClick={this.noClick} >{__('global.no')}</Button>
				</p>
				);
			case 'SaveCancel': return (
				<p className="text-center">
					<Button bsSize="large" bsStyle="primary"
						onClick={this.saveClick}>{__('action.save')}</Button>
					<Button bsSize="large" bsStyle="default"
						onClick={this.cancelClick} >{__('action.cancel')}</Button>
				</p>
				);
			case 'CustomCancel': return (
				<p className="text-center">
					<Button bsSize="large" bsStyle="primary"
						onClick={this.customClick}>{(this.props.customBtnLbl ? this.props.customBtnLbl : 'CUSTOMLABEL')}</Button>
					<Button bsSize="large" bsStyle="default"
						onClick={this.cancelClick} >{__('action.cancel')}</Button>
				</p>
				);
			default: return (
				<p className="text-center">
					<Button bsSize="large" bsStyle="default"
						onClick={this.closeClick} >{__('action.close')}</Button>
				</p>
				);
		}
	}

	yesClick() {
		this.close('yes');
	}

	noClick() {
		this.close('no');
	}

	saveClick() {
		this.close('save');
	}

	cancelClick() {
		this.close('cancel');
	}

	closeClick() {
		this.close('close');
	}

	customClick() {
		this.close('custom');
	}

	close(evt) {
		if (this.props.onClose) {
			this.props.onClose(evt);
		}
	}

	render() {
		const buttons = this.createButtons();

		return (
			<Modal show={this.props.show} onHide={this.cancelClick} bsSize={this.props.size}>
				<Modal.Header closeButton>
					<Modal.Title>
						{this.props.title}
					</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					{this.props.content}
				</Modal.Body>
				<Modal.Footer>
					{buttons}
				</Modal.Footer>
			</Modal>
			);
	}
}

FormModal.propTypes = {
	show: React.PropTypes.bool,
	title: React.PropTypes.string,
	type: React.PropTypes.oneOf(['YesNo', 'SaveCancel', 'Close', 'CustomCancel']),
	onClose: React.PropTypes.func,
	size: React.PropTypes.string,
	customBtnLbl: React.PropTypes.string,
	content: React.PropTypes.node
};

FormModal.defaultProps = {
	show: false,
	type: 'Close'
};
