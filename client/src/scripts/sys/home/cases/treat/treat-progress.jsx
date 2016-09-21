import React from 'react';

import './treat-progress.less';

export default class TreatProgress extends React.Component {

    componentWillMount() {
        this.setState({ value: 0 });

        const self = this;
        setTimeout(() => {
            self.setState({ value: self.props.value });
        }, 300);
    }

    render() {
        const val = this.state.value;
        const r = 90;

        const c = Math.PI * r * 2;
        var pct = ((100 - val) / 100) * c;

        return (
            <div id="cont" data-pct={this.props.value}>
            <svg id="svg" width={this.props.width} height={this.props.height} viewBox="0 0 220 220">
                <circle r="90" cx="110" cy="110"
                    fill="transparent" strokeDashoffset="0"
                    strokeDasharray="565.48" />
                <circle id="bar" r="90" cx="110" cy="110"
                    fill="transparent"
                    strokeDasharray="565.48"
                    style={{ strokeDashoffset: pct }} />
                <text x="110" y="130" fontSize="56" textAnchor="middle">{this.props.value + '%'}</text>
            </svg>
            <div>{__('cases.mantreatment')}
            </div>
            </div>
            );
    }
}

TreatProgress.propTypes = {
    value: React.PropTypes.number,
    width: React.PropTypes.number,
    height: React.PropTypes.number
};

TreatProgress.defaultProps = {
    width: 100,
    height: 100
};
