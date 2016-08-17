import React from 'react';
import CrudView from '../../crud/crud-view';
import { Alert, Button, Badge, Row, Col } from 'react-bootstrap';
import CaseComments from './case-comments';
import CRUD from '../../../commons/crud';
import { Profile, Card } from '../../../components';
import CardWithComments from './card-with-comments';

const crud = new CRUD('case');

const comorbidities = ['alcoholExcessiveUse', 'tobaccoUseWithin', 'hivPositive', 'diabetes', 'anaemia', 'malnutrition'];

export default class CaseComorbidities extends React.Component {

	constructor(props) {
		super(props);
	}

	headerRender(trueOnes) {
		let size = 12;

		let btn;
		if (size === 12) {
			btn = <Button>{'Edit'}</Button>;
			size = 10;
		}
		else {
			btn = null;
		}

		return (
			<Row>
				<Col sm={size}>
					{__('TbField.COMORBIDITY')} <Badge className="tbl-counter">{trueOnes.length > 0 ? trueOnes.length : '-'}</Badge>
				</Col>
				{
					btn && <Col sm={2}><div className="pull-right">{btn}</div></Col>
				}
			</Row>
			);
	}

	render() {
		const tbcase = this.props.tbcase;
		const trueOnes = comorbidities.filter(item => tbcase[item] === true);

		return (
				<CaseComments
					tbcase={tbcase} group="COMORBIDITIES">
					<Card header={this.headerRender(trueOnes)} padding="combine">
						<span>{'TODO vizualization'}</span>
					</Card>
				</CaseComments>
			);
	}
}

CaseComorbidities.propTypes = {
	tbcase: React.PropTypes.object
};
