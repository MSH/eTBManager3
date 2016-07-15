import React from 'react';
import moment from 'moment';
import { Grid, Row, Col } from 'react-bootstrap';
import TreatPopup from './treat-popup';
import { durationDisplay } from '../../../../commons/utils';


import './treat-timeline.less';


export default class TreatTimeline extends React.Component {

	constructor(props) {
		super(props);
		this.barClick = this.barClick.bind(this);
		this._popupHide = this._popupHide.bind(this);
	}

	componentWillMount() {
		this.setState({ ini: true, ppshow: false });
		const self = this;
		setTimeout(() => self.setState({ ini: false }), 100);
	}

	SVG(props) {
		return <svg width="100%" viewBox="0 0 100 4">{props.children}</svg>;
	}

	renderHeader(period) {
		const f = 'MMM-YYYY';
		const SVG = this.SVG;

		return (
			<SVG>
				<text x="1" y="2.2" fontSize="2.5">
					{period.ini.format(f)}
				</text>
				<text x="99" y="2.2" fontSize="2.5" textAnchor="end">
					{period.end.format(f)}
				</text>
				<text x="50" y="2.2" fontSize="2.5" textAnchor="middle">
					{durationDisplay(period.ini, period.end)}
				</text>
				<line x1="0" y1="3.2" x2="100" y2="3.2" stroke="#a0a0a0" strokeWidth="0.1"/>
				<line x1="0" y1="2" x2="0" y2="4" stroke="#a0a0a0" strokeWidth="0.1" />
				<line x1="100" y1="2" x2="100" y2="4" stroke="#a0a0a0" strokeWidth="0.1" />
			</SVG>
			);
	}


	barClick(period) {
		const self = this;
		return evt => {
			const target = evt.target;
			const show = self.state.pptarget !== target;
			setTimeout(() =>
				self.setState({
					pptarget: show ? target : null,
					data: period,
					ppshow: show
				})
			, 100);
		};
	}

	_popupHide() {
		this.setState({ ppshow: false, target: null });
	}

	calcRect(treat, period) {
		const ini = period.ini;
		const end = period.end;

		return {
			x: ini.diff(treat.ini, 'days') / treat.days * 100,
			y: 0,
			width: end.diff(ini, 'days') / treat.days * 100,
			height: 4
		};
	}

	renderBar(pos, period) {
		const color = period.color ? period.color : '#48AA8A';

		// animation of the bars
		const props = Object.assign({}, pos);
		if (this.state.ini) {
			props.width = 0;
		}

		return (
				<rect key={period.ini}
					fill={color}
					{...props}
					rx="1" ry="1"/>
			);
	}


	renderColumn(treat, periods) {
		const self = this;

		const comps = [];
		// render for each period
		periods.forEach((p, index) => {
			// calculate the position
			const pos = self.calcRect(treat, p);

			pos.onClick = self.barClick(p);
			pos.className = 'tm-column';
			// add the bar
			comps.push(self.renderBar(pos, p));

			// is there any custom text
			if (p.text) {
				comps.push(
					<svg key={'t' + index} {...pos}>
						<text x="1" y="2.6" fontSize="2" fill="white">
							{p.text}
						</text>
					</svg>
				);
			}
		});

		const SVG = this.SVG;
		return (
			<SVG>
				<rect x="0" y="0" width="100" height="4" fill="#e8e8e8" rx="1" ry="1"/>
				{comps}
			</SVG>
			);
	}

	renderPrescColumn(treat, periods, presc) {
		const lst = periods.map(p => ({
			ini: moment(p.ini),
			end: moment(p.end),
			text: p.doseUnit + ' (' + p.frequency + '/7)',
			data: p,
			presc: presc
		}));

		return this.renderColumn(treat, lst);
	}


	render() {
		const treat = this.props.treatment;
		if (!treat) {
			return null;
		}

		// calculate the treatement period to make calculation easier
		const period = {
			ini: moment(treat.period.ini),
			end: moment(treat.period.end),
			days: null
		};

		period.days = period.end.diff(period.ini, 'days');

		// prescribed medicines
		const prescs = treat.prescriptions;

		const titleSize = { md: 3 };
		const barSize = { md: 9 };

		const units = treat.units.map(it => ({
			ini: moment(it.ini),
			end: moment(it.end),
			text: it.unit.name,
			data: it,
			color: '#E67E22' // '#2980b9'
		}));

		const SVG = this.SVG;

		return (
			<Grid fluid>
				<Row>
					<Col {...titleSize} />
					<Col {...barSize}>
					{this.renderHeader(period)}
					</Col>
				</Row>
				<Row>
					<Col {...titleSize}>
						<b>
						{
							__('TbCase.healthUnits')
						}
						</b>
					</Col>
					<Col {...barSize}>
						{this.renderColumn(period, units)}
					</Col>
				</Row>
				{
					prescs.map(it => (
						<Row key={it.product.id} className="tbl-row">
							<Col {...titleSize}>
								{
									it.product.name
								}
							</Col>
							<Col {...barSize}>
								<SVG>
								{this.renderPrescColumn(period, it.periods, it)}
								</SVG>
							</Col>
						</Row>
						))
				}
				<TreatPopup ref="popup"
					data={this.state.data}
					show={this.state.ppshow}
					onHide={this._popupHide}
					target={this.state.pptarget} />
			</Grid>
			);
	}
}

TreatTimeline.propTypes = {
	treatment: React.PropTypes.object
};
