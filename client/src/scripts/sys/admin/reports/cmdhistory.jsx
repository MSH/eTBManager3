
import React from 'react';
import { Col, Grid, Row, Badge } from 'react-bootstrap';
import { Card, CollapseRow, WaitIcon } from '../../../components/index';
import { server } from '../../../commons/server';

/**
 * The page controller of the public module
 */
export default class CommandHistory extends React.Component {

	constructor(props) {
		super(props);
		this.state = { };
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

		server.post('/api/admin/rep/cmdhistory')
		.then(res => {
			// generate new result
			const result = { count: res.length, list: res };
			// set state
			self.setState({ values: result });
		});
	}

	parseDetails(item) {
		const collapsedValue = (<div className="text-small">
									<dl>
										<Col sm={4}>
											<dt>{__('User.login') + ':'}</dt>
											<dd>{item.userLogin}</dd>
										</Col>
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
					<h4>{'Command History'} {countHTML}</h4>
				</Col>
			</Row>
			);
	}

	render() {
		if (!this.state || !this.state.values) {
			return <WaitIcon type="card" />;
		}

		const rowList = this.state.values.list.map(item => ({ data: item, detailsHTML: this.parseDetails(item) }));
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
									{item.data.userName}
								</Col>

								<Col md={4}>
									{new Date(item.data.loginDate).toString()}
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

CommandHistory.propTypes = {
	route: React.PropTypes.object
};
