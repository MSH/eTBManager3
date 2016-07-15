import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { WaitIcon } from '../../../components';
import { server } from '../../../commons/server';
import CasesTags from '../commons/cases-tags';
import CasesDistribution from '../commons/cases-distribution';


function fetchData(parentId) {
	const params = parentId ? '/places?id=' + parentId : '';
	return server.post('/api/cases/workspace' + params);
}


export default class Cases extends React.Component {

	componentWillMount() {
		const self = this;

		fetchData()
		.then(res => self.setState({ data: res }));

		this.state = {};
	}

	getChildren(parent) {
		const id = parent.id;
		return fetchData(id);
	}

	render() {
		const data = this.state.data;
		if (!data) {
			return <WaitIcon />;
		}

		return (
			<div className="mtop-2x">
			<Grid fluid>
				<Row>
					<Col sm={3}>
						<CasesTags tags={data.tags} />
					</Col>
					<Col sm={9}>
						<Grid fluid>
							<CasesDistribution
								root={data.places}
								onGetChildren={this.getChildren} />
						</Grid>
					</Col>
				</Row>
			</Grid>
			</div>
			);
	}
}
