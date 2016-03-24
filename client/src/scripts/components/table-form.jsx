import React from 'react';
import { Grid, Row, Col, ButtonToolbar, Button } from 'react-bootstrap';
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
		if (rowsQuantity < this.props.docs.length) {
			i = rowsQuantity + 1;
			while (rowsQuantity !== this.props.docs.length) {
				this.props.docs.pop();
			}
		}


		if (!rowsQuantity || rowsQuantity === 0) {
			rowsQuantity = 1;
		}

		var rows = [];
		for (i = 0; i < rowsQuantity; i++) {
			rows[i] = this.getNewRow(i);
		}

		return (<div>{rows.map(row => row)}</div>);
	}

	getNewRow(key) {
		// add a new doc if don't exists to have the same docs quantity as rows
		if (!this.props.docs[key]) {
			this.props.docs.push({});
		}

		// add a new erros if don't exists to have the same errors quantity as rows
		if (!this.props.errorsarr[key]) {
			this.props.errorsarr.push({});
		}

		return 	(<Row key={key}>
					<Form ref="form"
						schema={this.props.fschema}
						doc={this.props.docs[key]}
						onChange={this.onChangeDoc}
						errors={this.props.errorsarr[key]} />
				</Row>);
	}

	render() {
		// prepare the element class
		const classes = [];
		if (this.props.className) {
			classes.push(this.props.className);
		}

		return (
			<Grid fluid className={classes.join(' ')}>
				{
					this.titleRender()
				}
				{
					this.contentRender()
				}
				<Row className="tbl-title">
					<Col sm={12}>
						<ButtonToolbar>
							<Button onClick={this.props.addRow}><Fa icon={'plus'}/></Button>
							<Button onClick={this.props.remRow}><Fa icon={'minus'}/></Button>
						</ButtonToolbar>
					</Col>
				</Row>
			</Grid>
			);
	}
}

TableForm.propTypes = {
	ctitles: React.PropTypes.array,
	rowsQuantity: React.PropTypes.number,
	className: React.PropTypes.string,
	docs: React.PropTypes.array,
	errorsarr: React.PropTypes.array,
	addRow: React.PropTypes.func,
	remRow: React.PropTypes.func,
	fschema: React.PropTypes.object
};
