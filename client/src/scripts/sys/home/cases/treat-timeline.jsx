import React from 'react';
import moment from 'moment';
import { Grid, Row, Col } from 'react-bootstrap';


function SVG(props) {
	return <svg width="100%" viewBox="0 0 100 4">{props.children}</svg>;
}

export default class TreatTimeline extends React.Component {

	renderHeader(period) {
		const f = 'MMM-YYYY';

		return (
			<SVG>
				<text x="1" y="2.2" fontSize="2.5">
					{period.ini.format(f)}
				</text>
				<text x="99" y="2.2" fontSize="2.5" textAnchor="end">
					{period.ini.format(f)}
				</text>
				<text x="50" y="2.2" fontSize="2.5" textAnchor="middle">
					{moment.duration(period.days, 'days').humanize()}
				</text>
				<line x1="0" y1="3.2" x2="100" y2="3.2" stroke="#a0a0a0" strokeWidth="0.1"/>
				<line x1="0" y1="2" x2="0" y2="4" stroke="#a0a0a0" strokeWidth="0.1" />
				<line x1="100" y1="2" x2="100" y2="4" stroke="#a0a0a0" strokeWidth="0.1" />
			</SVG>
			);
	}

	renderBar(treat, period) {
		const ini = moment(period.ini);
		const end = moment(period.end);

		const x = ini.diff(treat.ini, 'days') / treat.days * 100;
		const width = end.diff(ini, 'days') / treat.days * 100;

		const color = period.color ? period.color : '#48AA8A';

		return (
				<rect key={period.ini}
					x={x} y="0" width={width} height="4"
					fill={color} rx="1" ry="1"/>
			);
	}


	renderColumn(treat, periods) {
		const self = this;

		return (
			<SVG>
				<rect x="0" y="0" width="100" height="4" fill="#e8e8e8" rx="1" ry="1"/>
				{
					periods.map(p => self.renderBar(treat, p))
				}
			</SVG>
			);
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

		// current unit under treatment
		const unitName = treat.units[treat.units.length - 1].unit.name;

		// prescribed medicines
		const prescs = this.props.treatment.prescriptions;

		const titleSize = { md: 3 };
		const barSize = { md: 9 };

		const colors = [
			'#2980b9', '#2C3E50', '#E67E22'
		];

		const units = treat.units.map((it, index) => ({
			ini: it.ini,
			end: it.end,
			color: colors[index % 3]
		}));

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
						{
							unitName
						}
					</Col>
					<Col {...barSize}>
						{this.renderColumn(period, units)}
					</Col>
				</Row>
				{
					prescs.map(it => (
						<Row key={it.medicine.id} className="tbl-row">
							<Col {...titleSize}>
								{
									it.medicine.name
								}
							</Col>
							<Col {...barSize}>
								<SVG>
								{this.renderColumn(period, it.periods)}
								</SVG>
							</Col>
						</Row>
						))
				}
			</Grid>
			);
	}
}

TreatTimeline.propTypes = {
	treatment: React.PropTypes.object
};
