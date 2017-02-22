import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { isFunction } from '../commons/utils';
import ReactRow from './react-row';
import Expandable from './expandable';

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

        this.rowRender = this.rowRender.bind(this);
    }

    /**
     * Return the React component that will be displayed on the screen
     * @return {[type]} [description]
     */
    titleRender() {
        const cols = this.props.columns;
        var hasTitle = false;

        var ret = (
                        <Row className="tbl-title">
                        {
                            cols.map((col, index) => {
                                const colProps = Object.assign({}, col.size);
                                if (col.title) {
                                    hasTitle = true;
                                }
                                return (
                                    <Col key={index} {...colProps} className={this.alignClass(col)}>
                                        {col.title}
                                    </Col>
                                );
                            })
                        }
                        </Row>
                    );

        if (hasTitle === false) {
            ret = null;
        }

        return ret;
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
     * Table content render - rows and its values
     * @return {React.Component} Return the component to be displayed
     */
    contentRender() {
        const lst = this.props.values;
        if (!lst) {
            return null;
        }

        const clickable = !!this.props.onClick || !!this.props.onExpandRender;

        return lst.map((item, index) => {
            // return the row
            return (
                <ReactRow key={item.id ? item.id : index}
                    index={index}
                    value={item}
                    onRender={this.rowRender}
                    onClick={clickable ? this.props.onClick : null} />
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

        // columns render
        const cols = this.props.columns.map((c, ind2) => {
            // get cell content
            const content = isFunction(c.content) ? c.content(item, row) : item[c.content];
            return <Col key={ind2} {...c.size} className={this.alignClass(c)}>{content}</Col>;
        });

        if (!this.props.onExpandRender) {
            return <div className="row tbl-row">{cols}</div>;
        }

        const self = this;
        const onExpand = it => {
            return self.props.onExpandRender(it, row);
        };

        return (
            <div className="row tbl-row">
            <Expandable onExpandRender={onExpand} value={item} expandClassName="col-sm-12">
                {
                    this.props.columns.map((c, ind2) => {
                        // get cell content
                        const content = isFunction(c.content) ? c.content(item, row) : item[c.content];
                        return <Col key={ind2} {...c.size} className={this.alignClass(c)}>{content}</Col>;
                    })
                }
            </Expandable>
            </div>
        );
    }

    render() {
        // prepare the element class
        const classes = [];
        if (this.props.className) {
            classes.push(this.props.className);
        }

        if (this.props.onClick || this.props.onExpandRender) {
            classes.push('tbl-hover');
        }

        return (
            <Grid className={classes.join(' ')} fluid>
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
    onExpandRender: React.PropTypes.func,
    onRowRender: React.PropTypes.func
};
