import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Popover, Overlay } from 'react-bootstrap';
import { LinkTooltip } from '../../../components';
import FilterPopup from './filter-popup';
import FilterBox from './filter-box';

import './filters.less';

/**
 * Display an inline editor for selection of filters
 */
export default class FiltersSelector extends React.Component {

    constructor(props) {
        super(props);
        this.btnRef = this.btnRef.bind(this);
        this.addFilter = this.addFilter.bind(this);
        this.hidePopup = this.hidePopup.bind(this);
        this.togglePopup = this.togglePopup.bind(this);
        this._onValueChange = this._onValueChange.bind(this);
        this.removeFilter = this.removeFilter.bind(this);
    }

    componentWillMount() {
        this.state = { show: this.props.showPopup };
    }

    /**
     * Add a filter to the card
     */
    addFilter(item) {
        let lst = this.props.filterValues;

        if (!lst) {
            lst = [];
        }

        lst.push({ filter: item, value: null });

        this.notifyChange(lst.slice(0));
        this.setState({ show: false });
    }

    /**
     * Remove a filter from the list of selected filters
     */
    removeFilter(filter) {
        const lst = this.props.filterValues;

        if (!lst) {
            return;
        }

        const index = lst.findIndex(item => item.filter === filter);

        lst.splice(index, 1);
        this.notifyChange(lst);
        this.setState({ show: false });
    }

    /**
     * Called when the list of filter values is changed
     */
    notifyChange(filterValues) {
        if (this.props.onChange) {
            this.props.onChange(filterValues);
        }
    }

    /**
     * Toggle the displaying of the filter popup
     */
    togglePopup() {
        this.setState({ show: !this.state.show });
    }

    /**
     * Return a reference to the button where overlay must be displayed upon
     */
    btnRef() {
        return ReactDOM.findDOMNode(this.refs.btnadd);
    }

    /**
     * Manually hide the popup panel
     */
    hidePopup() {
        this.setState({ show: false });
    }

    /**
     * Render the filters and its selected values
     */
    renderFilters() {
        const filters = this.props.filterValues;
        if (!filters) {
            return null;
        }

        return filters.map(it => (
            <FilterBox key={it.filter.id}
                filter={it.filter}
                value={it.value}
                onChange={this._onValueChange}
                onRemove={this.removeFilter} />
        ));
    }

    /**
     * Called when a filter value is changed
     */
    _onValueChange(filter, value) {
        const lst = this.props.filterValues;

        const item = lst.find(it => it.filter === filter);
        item.value = value;
        this.notifyChange(lst.slice(0));
    }


    render() {
        return (
            <div>
                <div className="text-muted text-uppercase">
                    {__('form.filters')}
                </div>
                {
                    this.renderFilters()
                }
                <Row>
                    <Col sm={12} className="mtop">
                        {
                            this.props.footer
                        }
                        <LinkTooltip ref="btnadd" toolTip={__('form.filters.add')} icon="plus"
                            onClick={this.togglePopup} />
                        {
                            this.props.filters &&
                            <Overlay
                                animation
                                target={this.btnRef}
                                placement="right"
                                show={this.state.show}
                                rootClose
                                onHide={this.hidePopup} >

                                <Popover id="filter-popover" style={{ minWidth: '330px' }}>
                                    <FilterPopup
                                        filters={this.props.filters}
                                        onSelect={this.addFilter} />
                                </Popover>
                            </Overlay>
                        }
                    </Col>
                </Row>
            </div>
        );
    }
}

FiltersSelector.propTypes = {
    filters: React.PropTypes.array,
    filterValues: React.PropTypes.array,
    onChange: React.PropTypes.func,
    showPopup: React.PropTypes.bool,
    footer: React.PropTypes.node
};

