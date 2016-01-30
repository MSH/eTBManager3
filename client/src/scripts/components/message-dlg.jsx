import React from 'react';
import { Modal, Button } from 'react-bootstrap';


export default class MessageDlg extends React.Component {

	constructor(props) {
		super(props);
		this.yesClick = this.yesClick.bind(this);
		this.noClick = this.noClick.bind(this);
		this.okClick = this.okClick.bind(this);
		this.cancelClick = this.cancelClick.bind(this);
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
			default: return (
					<p className="text-center">
					<Button bsSize="large"
						bsStyle="primary"
						onClick={this.okClick}>{__('form.ok')}
					</Button>
					</p>
					);
		}
	}

	yesClick() {
		this.close('yes');
	}

	cancelClick() {
		this.close('cancel');
	}

	noClick() {
		this.close('no');
	}

	okClick() {
		this.close('ok');
	}

	close(evt) {
		if (this.props.onClose) {
			this.props.onClose(evt);
		}
	}

	render() {
		const buttons = this.createButtons();

		let icon = 'fa fa-4x fa-';
		let icondiv = 'pull-left circle-5x';
		switch (this.props.style) {
			case 'danger':
				icon += 'exclamation';
				icondiv += ' bg-danger text-danger';
				break;
			case 'warning':
				icon += 'exclamation-triangle';
				icondiv += ' bg-warning text-warning';
				break;
			default:
				icon += 'info';
				icondiv += ' bg-info text-info';
		}

		return (
			<Modal show={this.props.show} onHide={this.onCancel} bsSize={this.props.size}>
				<Modal.Header closeButton>
					<Modal.Title>
						{this.props.title}
					</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<div className={icondiv} >
						<i className={icon}/>
					</div>
					<div style={{ minHeight: '55px' }}>
						<p className="lead">{this.props.message}</p>
					</div>
				</Modal.Body>
				<Modal.Footer>
					{buttons}
				</Modal.Footer>
			</Modal>
			);
	}
}

MessageDlg.propTypes = {
	show: React.PropTypes.bool,
	title: React.PropTypes.string,
	message: React.PropTypes.string,
	style: React.PropTypes.oneOf(['info', 'danger', 'warning']),
	type: React.PropTypes.oneOf(['YesNo', 'Ok']),
	onClose: React.PropTypes.func,
	size: React.PropTypes.string
};

MessageDlg.defaultProps = {
	show: false,
	style: 'info',
	type: 'Ok'
};
