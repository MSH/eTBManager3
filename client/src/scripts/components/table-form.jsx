import React from 'react';
import { Row, Col, ButtonToolbar, Button, Grid } from 'react-bootstrap';
import Form from '../forms/form';
import Fa from '../components/fa';


export default class TableForm extends React.Component {

	constructor(props) {
		super(props);

		if (__DEV__) {
			if (!props.fschema) {
				throw new Error('fschema (form schema) must be defined in FormTable');
			}

			if (props.ctitles) {
				props.ctitles.map(c => {
					if (!c.size) {
						throw new Error('No column size specified in ReactTable');
					}
				});
			}
		}

		this.onChangeDoc = this.onChangeDoc.bind(this);
		this.isValid = this.isValid.bind(this);
		this.state = { errorsarr: [] };
	}

	onChangeDoc() {
		this.forceUpdate();
	}

	/**
	 * Return the React component that will be displayed on the screen
	 * @return {[type]} [description]
	 */
	titleRender() {
		if (!this.props.ctitles) {
			return null;
		}

		const cols = this.props.ctitles;

		return (
			<Row className="tbl-title">
			{
				cols.map((col, index) => {
					const colProps = Object.assign({}, col.size);
					return (
						<Col key={index} {...colProps} className={this.alignClass(col)}>
							{col.title}
						</Col>
						);
				})
			}
			</Row>
			);
	}

	alignClass(col) {
		switch (col.align) {
			case 'right':
				return 'col-right';
			case 'center':
				return 'col-center';
			default:
				return null;
		}
	}

	contentRender() {
		var rowsQuantity = this.props.rowsQuantity;
		var i;

		// Clean docs array to have the same quantity as rows
		if (rowsQuantity < this.props.docs.length && this.props.docs.length > 1) {
			i = rowsQuantity + 1;
			while (rowsQuantity !== this.props.docs.length) {
				this.props.docs.pop();
				this.state.errorsarr.pop();
			}
		}


		if (!rowsQuantity || rowsQuantity < 1) {
			rowsQuantity = 1;
		}

		var rows = [];
		for (i = 0; i < rowsQuantity; i++) {
			rows[i] = this.getNewRow(i);
		}

		return (<div>{rows.map(row => row)}</div>);
	}

	getNewRow(key) {
		if (key < 0) {
			return null;
		}

		// add a new doc if don't exists to have the same docs quantity as rows
		if (!this.props.docs[key]) {
			this.props.docs.push({});
		}

		// add a new erros if don't exists to have the same errors quantity as rows
		if (!this.state.errorsarr[key]) {
			this.state.errorsarr.push({});
		}

		return 	(
					<Form ref={'form' + key}
						schema={this.props.fschema}
						key={key}
						doc={this.props.docs[key]}
						onChange={this.onChangeDoc}
						errors={this.state.errorsarr[key]}
						nodetype={this.props.nodetype} />
				);
	}

	//TODOMSR verificar se deveria ficar aqui dentro do componente.
	isValid() {
		var i;
		var valid = true;
		for (i = 0; i < this.props.rowsQuantity; i++) {
			const e = this.state.errorsarr;
			e[i] = this.refs['form' + i].validate();
			if (e[i]) {
				valid = false;
			}
		}

		this.forceUpdate();
		return valid;
	}

	render() {
		// prepare the element class
		const classes = [];
		if (this.props.className) {
			classes.push(this.props.className);
		}

		var buttons = null;

		// TODOMSR ver se esse código, grande desse jeito tem problema
		switch (this.props.nodetype) {
            case 'fluid': buttons = (<Grid fluid className="def-margin-bottom">
										<Row>
											<Col sm={12}>
												<ButtonToolbar>
													<Button onClick={this.props.addRow}><Fa icon={'plus'}/></Button>
													<Button onClick={this.props.remRow}><Fa icon={'minus'}/></Button>
												</ButtonToolbar>
											</Col>
										</Row>
									</Grid>); break;

            case 'div': buttons = (<Row className="def-margin-bottom">
										<Col sm={12}>
											<ButtonToolbar>
												<Button onClick={this.props.addRow}><Fa icon={'plus'}/></Button>
												<Button onClick={this.props.remRow}><Fa icon={'minus'}/></Button>
											</ButtonToolbar>
										</Col>
									</Row>); break;

            default: buttons = (<Grid fluid className="def-margin-bottom">
									<Row>
										<Col sm={12}>
											<ButtonToolbar>
												<Button onClick={this.props.addRow}><Fa icon={'plus'}/></Button>
												<Button onClick={this.props.remRow}><Fa icon={'minus'}/></Button>
											</ButtonToolbar>
										</Col>
									</Row>
								</Grid>); break;
        }

		return (
			<div>
				{
					this.titleRender()
				}
				{
					this.contentRender()
				}
				{
					buttons
				}
			</div>
			);
	}
}

TableForm.propTypes = {
	ctitles: React.PropTypes.array,
	rowsQuantity: React.PropTypes.number,
	className: React.PropTypes.string,
	docs: React.PropTypes.array,
	addRow: React.PropTypes.func,
	remRow: React.PropTypes.func,
	fschema: React.PropTypes.object,
	nodetype: React.PropTypes.oneOf(['fluid', 'div'])
};
