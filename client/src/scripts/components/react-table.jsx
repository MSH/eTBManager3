import React from 'react';
import { Grid, Row, Col, Collapse } from 'react-bootstrap';
import { isFunction } from '../commons/utils';
import ReactRow from './react-row';

/**
 * Display a reactive table just using the Twitter Bootstrap grid system
 */
export default class ReactTable extends React.Component {

	constructor(props) {
		super(props);

		if (__DEV__) {
			if (!props.columns) {
				throw new Error('columns must be defined in ReactTable');
			}

			props.columns.map(c => {
				if (!c.size) {
					throw new Error('No column size specified in ReactTable');
				}
			});
		}

		this.rowClick = this.rowClick.bind(this);
		this.rowRender = this.rowRender.bind(this);
	}

	/**
	 * Return the React component that will be displayed on the screen
	 * @return {[type]} [description]
	 */
	titleRender() {
		const cols = this.props.columns;

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

	/**
	 * Called when user clicks on the row
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	rowClick(item, row) {
		if (this.props.onClick) {
			this.props.onClick(item, row);
		}
		if (this.props.onCollapseRender) {
			item.iscollapsed = !item.iscollapsed;
			row.forceUpdate();
		}
	}

	collapseRender(item, row) {
		if (this.props.onCollapseRender) {
			return this.props.onCollapseRender(item, row);
		}
		return null;
	}

	/**
	 * Table content render - rows and its values
	 * @return {React.Component} Return the component to be displayed
	 */
	contentRender() {
		const lst = this.props.values;
		if (!lst) {
			return null;
		}

		const clickable = !!this.props.onClick || !!this.props.onCollapseRender;

		return lst.map((item, index) => {
			// return the row
			return (
				<ReactRow key={item.id ? item.id : index}
					index={index}
					value={item}
					onRender={this.rowRender}
					onClick={clickable ? this.rowClick : null} />
				);
		}, this);
	}


	/**
	 * Row render - This function is called by ReactRow component whenever the row
	 * must be rerendered
	 * @param  {Object} item The object item related to the row
	 * @param  {ReactRow} row  The instance of ReactRow component
	 * @return {React.Component}      The content of the row
	 */
	rowRender(item, row) {
		// check if there is a row render defined
		if (this.props.onRowRender) {
			const content = this.props.onRowRender(item, row);
			// render returned any content ?
			if (content) {
				return content;
			}
		}

		const self = this;
		return (
			<div className="row tbl-row">
				{
					self.props.columns.map((c, ind2) => {
						// get cell content
						const content = isFunction(c.content) ? c.content(item, row) : item[c.content];
						return <Col key={ind2} {...c.size} className={this.alignClass(c)}>{content}</Col>;
					})
				}
				{this.props.onCollapseRender &&
				<Col sm={12}>
					<Collapse in={item.iscollapsed}>
						<div>{this.collapseRender(item, row)}</div>
					</Collapse>
				</Col>}
			</div>
		);
	}

	render() {
		// prepare the element class
		const classes = [];
		if (this.props.className) {
			classes.push(this.props.className);
		}

		if (this.props.onClick || this.props.onCollapseRender) {
			classes.push('tbl-hover');
		}

		return (
			<Grid fluid className={classes.join(' ')}>
				{
					this.titleRender()
				}
				{
					this.contentRender()
				}
			</Grid>
			);
	}
}

ReactTable.propTypes = {
	columns: React.PropTypes.array,
	values: React.PropTypes.array,
	onClick: React.PropTypes.func,
	className: React.PropTypes.string,
	onCollapseRender: React.PropTypes.func,
	onRowRender: React.PropTypes.func
};
