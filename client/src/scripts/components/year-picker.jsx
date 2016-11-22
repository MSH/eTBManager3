import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import PopupControl from './popup-control';
import Fa from './fa';

import './year-picker.less';

/**
 * React component for selecting an year
 */
export default class YearPicker extends React.Component {

    constructor(props) {
        super(props);

        this.prevClick = this.prevClick.bind(this);
        this.nextClick = this.nextClick.bind(this);
        this.selectYear = this.selectYear.bind(this);
        this.state = { page: 0 };
    }

    componentWillReceiveProps(newProps) {
        if (newProps.value !== this.props.value) {
            this.setState({ page: 0 });
        }
    }

    prevClick(evt) {
        this.setState({ page: this.state.page - 1 });
        evt.preventDefault();
    }

    nextClick(evt) {
        this.setState({ page: this.state.page + 1 });
        evt.preventDefault();
    }

    selectYear(year) {
        return () => {
            if (this.props.onChange) {
                this.props.onChange(year);
            }
        };
    }

    renderPopup() {
        // get the year
        const props = this.props;
        let year = props.value ? props.value : (new Date()).getFullYear();

        year = (Math.floor(year / 20) * 20);
        year -= this.state.page * 20;

        const addRow = y => (
            <Col xs={3} className={'year' + (y === props.value ? ' active' : '')}
                onClick={this.selectYear(y)}>
                {y}
            </Col>
        );

        const lst = [];
        for (var i = 0; i < 5; i++) {
            lst.push(
                <Row key={i + 1}>
                    {addRow(year++)}
                    {addRow(year++)}
                    {addRow(year++)}
                    {addRow(year++)}
                </Row>
                );
        }

        return (
            <div className="year-picker">
                <Grid fluid>
                    <Row>
                        <Col xs={12}>
                            <a className="pull-right btn-naveg" onClick={this.prevClick}>
                                <Fa icon="chevron-right"/>
                            </a>
                            <a onClick={this.nextClick} className="btn-naveg">
                                <Fa icon="chevron-left"/>
                            </a>
                        </Col>
                    </Row>
                    {lst}
                </Grid>
            </div>
        );
    }

    render() {
        if (this.props.noPopup) {
            return this.renderPopup();
        }

        const props = Object.assign({}, this.props);

        const content = props.value ? props.value : <span className="text-muted">{__('datetime.year')}</span>;

        delete props.value;
        delete props.onChange;

        return (
            <PopupControl {...props}
                content={content}
                autoHide={false}
                popupContent={this.renderPopup()}/>
        );
    }
}

YearPicker.propTypes = {
    value: React.PropTypes.number,
    onChange: React.PropTypes.func,
    noPopup: React.PropTypes.bool,

    // to be used by popupControl
    label: React.PropTypes.node,
    bsStyle: React.PropTypes.oneOf(['success', 'warning', 'error']),
    help: React.PropTypes.string,
    wrapperClassName: React.PropTypes.string
};
