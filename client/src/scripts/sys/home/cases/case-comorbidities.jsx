import React from 'react';
import { Badge, Row, Col } from 'react-bootstrap';
import CaseComments from './case-comments';
import CRUD from '../../../commons/crud';
import { Card, Fa } from '../../../components';

// TODOMS: ver c ricardo se está ok usar o crud de cases. o log ficou legal, mostrar
const crud = new CRUD('case');

const comorbidities = [
		{ field: 'alcoholExcessiveUse', name: __('TbCase.alcoholExcessiveUse') },
		{ field: 'tobaccoUseWithin', name: __('TbCase.tobaccoUseWithin') },
		{ field: 'hivPositive', name: __('TbCase.hivPositive') },
		{ field: 'diabetes', name: __('TbCase.diabetes') },
		{ field: 'anaemia', name: __('TbCase.anaemia') },
		{ field: 'malnutrition', name: __('TbCase.malnutrition') }
	];

export default class CaseComorbidities extends React.Component {

	constructor(props) {
		super(props);
		this.changeComorbiditieState = this.changeComorbiditieState.bind(this);
	}

	componentWillMount() {
		const tbcase = this.props.tbcase;

		// put the comorbidities separated, so it will be displayed with the true first
		const trueOnes = comorbidities.filter(item => tbcase[item.field] === true);
		const falseOnes = comorbidities.filter(item => tbcase[item.field] === false);

		const data = [];
		trueOnes.map(item => data.push({ field: item.field, name: item.name, value: true, fetch: null }));
		falseOnes.map(item => data.push({ field: item.field, name: item.name, value: false, fetch: null }));

		this.setState({ data: data, countTrue: trueOnes.length });
	}

	itemHeaderRender(item) {
		if (item.fetch === 'yes') {
			return (<div className="pull-right text-muted text-small">
						<Fa icon="circle-o-notch" spin/>
						{__('global.saving')}
					</div>);
		}

		if (item.fetch === 'error') {
			return (<div className="pull-right bs-error text-small">
						{__('global.errorsaving')}
					</div>);
		}

		return null;
	}

	headerRender() {
		return (
			<Row>
				<Col sm={12}>
					{__('TbField.COMORBIDITY')} <Badge className="tbl-counter">{this.state.countTrue > 0 ? this.state.countTrue : '-'}</Badge>
				</Col>
			</Row>
			);
	}

	changeComorbiditieState(field) {
		const self = this;
		return (() => {
			// updates UI attribute that is used to display info
			const com = this.state.data.find(i => i.field === field);
			com.value = !com.value;
			com.fetch = 'yes';

			// updates the counter
			const countTrue = com.value === true ? this.state.countTrue + 1 : this.state.countTrue - 1;
			self.setState({ countTrue: countTrue });

			// mount doc
			const doc = {};
			doc[field] = com.value;

			// send request
			crud.update(self.props.tbcase.id, doc)
				.then(() => {
					com.fetch = null;
					self.forceUpdate();
				})
				.catch(() => {
					com.fetch = 'error';
					self.forceUpdate();
				});
		});
	}

	render() {
		const tbcase = this.props.tbcase;
		const data = this.state.data;

		// put 3 cols in each row
		// TODOMS: ver c ricardo maneira melhor de fazer isso
		const rows = [];
		let cols = [];
		let colcounter = 0;
		data.map((item, index) => {
			if (colcounter === 3) {
				rows.push(
					<Row key={index}>
						{
							cols.map(col => col)
						}
					</Row>
					);
				cols = [];
				colcounter = 0;
			}

			cols.push(
				<Col md={4} key={item.field}>
					<Card header={this.itemHeaderRender(item)}>
						<a onClick={this.changeComorbiditieState(item.field)}>
							<Fa icon={item.value === true ? 'check' : 'close'} />
							<span className="lnk-label">{item.name}</span>
						</a>
					</Card>
				</Col>);

			colcounter++;
		});

		// check if there is any incomplete row
		if (cols.length > 0) {
			rows.push(
				<Row>
					{
						cols.map(col => col)
					}
				</Row>
				);
		}

		return (
				<CaseComments
					tbcase={tbcase} group="COMORBIDITIES">
					<Card header={this.headerRender()} padding="combine">
						{
							rows.map(row => row)
						}
					</Card>
				</CaseComments>
			);
	}
}

CaseComorbidities.propTypes = {
	tbcase: React.PropTypes.object
};
