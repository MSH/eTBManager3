import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { isFunction } from '../commons/utils';


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
	rowClick(item) {
		return () => {
			if (this.props.onClick) {
				this.props.onClick(item);
			}
		};
	}

	contentRender() {
		const lst = this.props.values;
		if (!lst) {
			return null;
		}

		const clickable = !!this.props.onClick;

		return lst.map((item, index) =>
			<Row key={index} className="tbl-row" onClick={clickable ? this.rowClick(item) : null}>
			{
				this.props.columns.map((c, ind2) => {
					const content = isFunction(c.content) ? c.content(item, index) : item[c.content];
					return <Col key={ind2} {...c.size} className={this.alignClass(c)}>{content}</Col>;
				})
			}
			</Row>
		);
	}

	render() {
		// prepare the element class
		const classes = [];
		if (this.props.className) {
			classes.push(this.props.className);
		}

		if (this.props.onClick) {
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
	mode: React.PropTypes.oneOf(['click', 'single-sel', 'multi-sel']),
	className: React.PropTypes.string,
	showCounter: React.PropTypes.bool,
	onChangePage: React.PropTypes.func,
	page: React.PropTypes.number,
	maxPage: React.PropTypes.number
};
