import React from 'react';
import { Overlay, Popover, ButtonToolbar, Button } from 'react-bootstrap';
import ReactDOM from 'react-dom';
import { durationDisplay } from '../../../../commons/utils';


export default class TreatPopup extends React.Component {

	constructor(props) {
		super(props);
		this._getTarget = this._getTarget.bind(this);
	}


	_getTarget() {
		return ReactDOM.findDOMNode(this.props.target);
	}

	renderPeriod(period) {
		return (
			<div>
				<div className="clearfix">
					<div className="pull-left">
						{period.ini.format('L')}
					</div>
					<div className="pull-right">
						{period.end.format('L')}
					</div>
				</div>
				<div>
					<b>{__('period.duration') + ': '}</b>
					{durationDisplay(period.ini, period.end)}
				</div>
			</div>
		);
	}

	renderUnit(data) {
		return (
			<div style={{ minWidth: '230px' }}>
				{
					this.renderPeriod(data)
				}
				<div className="mtop-2x">
				<ButtonToolbar>
					<Button bsStyle="primary">{__('action.edit')}</Button>
					<Button>{__('action.delete')}</Button>
				</ButtonToolbar>
				</div>
			</div>
			);
	}

	renderPresc(data) {
		return (
			<div style={{ minWidth: '230px' }}>
				{
					this.renderPeriod(data)
				}
				<div>
					<b>{__('PrescribedMedicine.frequency') + ': '}</b>
					{data.data.frequency + '/7'}
				</div>
				<div>
					<b>{__('PrescribedMedicine.doseUnit') + ': '}</b>
					{data.data.doseUnit}
				</div>
				<div className="mtop-2x">
				<ButtonToolbar>
					<Button bsStyle="primary">{__('action.edit')}</Button>
					<Button>{__('action.delete')}</Button>
				</ButtonToolbar>
				</div>
			</div>
			);
	}

	render() {
		if (!this.props.target) {
			return null;
		}

		const data = this.props.data;

		const res = data.presc ?
			{
				title: data.presc.product.name,
				content: this.renderPresc(data)
			} :
			{
				title: data.text,
				content: this.renderUnit(data)
			};

		return (
			<Overlay
				rootClose
				onHide={this.props.onHide}
				show={this.props.show}
				target={this._getTarget}
				placement="bottom"
			>
				<Popover title={<b>{res.title}</b>} id="ppbar">
					{res.content}
				</Popover>
			</Overlay>
			);

	}
}

TreatPopup.propTypes = {
	title: React.PropTypes.string,
	show: React.PropTypes.bool,
	target: React.PropTypes.any,
	data: React.PropTypes.object,
	onHide: React.PropTypes.func
};
