import React from 'react';
import { Grid, Row, Col, Collapse } from 'react-bootstrap';
import { isFunction } from '../commons/utils';
import ReactRow from './react-row';


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
	rowClick(item, row) {
		if (this.props.onClick) {
			this.props.onClick(item);
		}
		if (this.props.collapseRender) {
			item.iscollapsed = !item.iscollapsed;
			row.forceUpdate();
		}
	}

	collapseRender(item) {
		if (this.props.collapseRender) {
			return this.props.collapseRender(item);
		}
		return null;
	}

	contentRender() {
		const lst = this.props.values;
		if (!lst) {
			return null;
		}

		const clickable = !!this.props.onClick || !!this.props.collapseRender;

		return lst.map((item, index) => {

			const rowRender = () => (
				<div>
					{
						this.props.columns.map((c, ind2) => {
							const content = isFunction(c.content) ? c.content(item, index) : item[c.content];
							return <Col key={ind2} {...c.size} className={this.alignClass(c)}>{content}</Col>;
						})
					}
					{this.props.collapseRender &&
					<Col sm={12}>
						<Collapse in={item.iscollapsed}>
							<div>{this.collapseRender(item)}</div>
						</Collapse>
					</Col>}
				</div>
				);

			return (
				<ReactRow key={'id' in item ? item.id : index}
					className="row tbl-row"
					value={item}
					onRender={rowRender}
					onClick={clickable ? this.rowClick : null} />
				);
		}
		);
	}

	render() {
		// prepare the element class
		const classes = [];
		if (this.props.className) {
			classes.push(this.props.className);
		}

		if (this.props.onClick || this.props.collapseRender) {
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
	collapseRender: React.PropTypes.func
};
