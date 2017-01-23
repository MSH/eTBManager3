import React from 'react';
import { Button, ButtonGroup, ButtonToolbar } from 'react-bootstrap';
import { AsyncButton, Popup, Fa } from '../../../components';
import FollowupCalendar from './followup-calendar';


/**
 * Display the treatment followup card
 */
export default class CalendEditor extends React.Component {

    constructor(props) {
        super(props);
        this._handleDayClick = this._handleDayClick.bind(this);
        this._buttonClick = this._buttonClick.bind(this);
        this.cancelClick = this.cancelClick.bind(this);
        this.saveClick = this.saveClick.bind(this);
        this.selectAll = this.selectAll.bind(this);

        this.state = { selBtn: 'DOTS' };
    }

    componentWillMount() {
        const days = this.props.data.days ? this.props.data.days.slice(0) : [];
        const data = Object.assign({}, this.props.data, { days: days });
        this.setState({ data: data });
    }


    _handleDayClick(dt) {
        const days = this.state.data.days;
        const day = dt.getDate();
        let item = days.find(it => it.day === day);
        const status = this.state.selBtn;

        if (item) {
            if (item.status === status) {
                const index = days.indexOf(item);
                days.splice(index, 1);
            } else {
                item.status = status;
            }
        } else {
            item = { day: day, status: status };
            days.push(item);
        }

        this.setState({ data: Object.assign({}, this.state.data) });
    }

    cancelClick() {
        this._close();
    }

    saveClick() {
        this._close(this.state.data);
    }

    _close(data) {
        if (this.props.onClose) {
            const p = this.props.onClose(data);

            if (p && p.then) {
                this.setState({ saving: true });

                const self = this;
                p.catch(() => self.setState({ saving: false }));
            }
        }
    }

    /**
     * Select all days with the same state
     * @return {[type]} [description]
     */
    selectAll() {
        const data = this.state.data;

        // get number of days in a month
        const dt = new Date(data.year, data.month + 1, 0);
        const num = dt.getDate();

        // get boundaries
        const ini = data.iniDay ? data.iniDay : 1;
        const end = data.endDay ? data.endDay : num;

        const days = [];
        const status = this.state.selBtn;


        for (var i = ini; i <= end; i++) {
            days.push({ day: i, status: status });
        }

        this.setState({ data: Object.assign({}, data, { days: days }) });
    }

    /**
     * Called when user clicks on the selection toolbar button
     * @param  {[type]} key [description]
     * @return {[type]}     [description]
     */
    _buttonClick(key) {
        return () => {
            this.setState({ selBtn: key });
        };
    }


    _renderToolbar() {
        const active = this.state.selBtn;

        return (
            <div style={{ padding: '4px' }}>
                <ButtonGroup bsSize="small" vertical block>
                    <Button active={active === 'DOTS'} onClick={this._buttonClick('DOTS')}>
                        <Fa icon="circle" className="treat-DOTS"/>{__('TreatmentDayOption.DOTS')}
                    </Button>
                    <Button active={active === 'SELF_ADMIN'} onClick={this._buttonClick('SELF_ADMIN')}>
                        <Fa icon="circle" className="treat-SELF_ADMIN"/>{__('TreatmentDayOption.SELF_ADMIN')}
                    </Button>
                    <Button active={active === 'NOT_TAKEN'} onClick={this._buttonClick('NOT_TAKEN')}>
                        <Fa icon="circle" className="treat-NOT_TAKEN"/>{__('TreatmentDayOption.NOT_TAKEN')}
                    </Button>
                </ButtonGroup>
                <a className="text-small"
                    onClick={this.selectAll}
                    style={{ cursor: 'pointer' }}>
                    {__('action.selectall')}
                </a>
            </div>
        );
    }


    render() {
        const data = this.state.data;

        return (
            <div onClick={this._calendarClick}>
                <Popup show>
                    <div className="editor">
                        {this._renderToolbar()}
                        <FollowupCalendar
                            data={data}
                            onDayClick={this._handleDayClick}
                            />
                        <ButtonToolbar style={{ margin: '4px' }}>
                            <AsyncButton bsStyle="success"
                                bsSize="small"
                                fetching={this.state.saving}
                                onClick={this.saveClick}>
                                {__('action.save')}
                            </AsyncButton>
                            <Button bsStyle="link"
                                bsSize="small"
                                onClick={this.cancelClick}>
                                {__('action.cancel')}
                            </Button>
                        </ButtonToolbar>
                    </div>
                </Popup>
            </div>
        );
    }
}

CalendEditor.propTypes = {
    data: React.PropTypes.object.isRequired,
    onClose: React.PropTypes.func
};
