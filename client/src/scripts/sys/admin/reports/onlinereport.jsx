
import React from 'react';
import { Col, Grid, Row, Badge } from 'react-bootstrap';
import { Card, CollapseRow, WaitIcon } from '../../../components/index';
import { server } from '../../../commons/server';

/**
 * The page controller of the public module
 */
export default class OnlineReport extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
	}

	componentWillMount() {
		this.refreshTable();
	}

	/**
	 * Called when the report wants to update its content
	 * @return {[type]} [description]
	 */
	refreshTable() {
		const self = this;

		return server.post('/api/admin/rep/onlinereport')
		.then(res => {
			// wrap itens inside a controller object
			const list = res.list.map(item => ({ data: item, state: 'ok' }));
			// generate new result
			const result = { count: res.count, list: list };
			// set state
			self.setState({ values: result });
			// return to the promise
			return result;
		});
	}

	parseDetails(item) {
		const collapsedValue = (<div className="text-small">
									<dl>
										<Col sm={4}>
											<dt>{__('admin.websessions.lastrequest') + ':'}</dt>
											<dd>
												{'TO DO: passar a registrar ultimo acesso'}
												{item.lastAccess}
											</dd>
										</Col>
										<Col sm={4}>
											<dt>{__('admin.websessions.sessiontime') + ':'}</dt>
											<dd>{'TO DO: CALC VALUE BASED ON loginDate'}</dd>
										</Col>
									</dl>
								</div>);
		return (collapsedValue);
	}

	headerRender(count) {
		const countHTML = <Badge className="tbl-counter">{count}</Badge>;

		// create the header of the card
		return (
			<Row>
				<Col sm={12}>
					<h4>{__('admin.websessions')}{countHTML}</h4>
				</Col>
			</Row>
			);
	}

	render() {
		if (!this.state || !this.state.values) {
			return <WaitIcon type="card" />;
		}

		/* amigo estou aqui*/
		const rowList = this.state.values.list.map(item => ({ data: item.data, detailsHTML: this.parseDetails(item.data) }));
		const header = this.headerRender(this.state.values.count);

		return (
				<Card header={header}>
				{'TO DO: passar a derrubar o usuario depois de um determinado tempo sem atividade'}
					<Grid className="mtop-2x table">
						<Row className="title">
							<Col md={4} className="nopadding">
								{__('User')}
							</Col>

							<Col md={4} className="nopadding">
								{__('UserLogin.loginDate')}
							</Col>

							<Col md={4} className="nopadding">
								{__('admin.websessions.idletime')}
							</Col>
						</Row>

						{
							rowList.map(item => (
							<CollapseRow collapsable={item.detailsHTML} className={'tbl-cell'}>
								<Col md={4}>
									{item.data.userName + ' (' + item.data.userLogin + ')'}
								</Col>

								<Col md={4}>
									{item.data.loginDate}
								</Col>

								<Col md={4}>
									{'TO DO: CALC VALUE BASED ON lastAccess'}
								</Col>
							</CollapseRow>
							))
						}

					</Grid>
				</Card>
			);
	}
}

OnlineReport.propTypes = {
	route: React.PropTypes.object
};
