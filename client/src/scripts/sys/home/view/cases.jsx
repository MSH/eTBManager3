import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { WaitIcon } from '../../../components';
import { server } from '../../../commons/server';
import CasesTags from '../commons/cases-tags';
import CasesDistribution from '../commons/cases-distribution';
import TagCasesList from '../cases/tag-cases-list';


function fetchNode(parentId) {
	const params = parentId ? '/places?parentId=' + parentId : '';
	return server.post('/api/cases/view' + params);
}

function fetchView(adminUnitId) {
	const params = adminUnitId ? '/adminunit?adminUnitId=' + adminUnitId : '';
	return server.post('/api/cases/view' + params);
}

export default class Cases extends React.Component {

	componentWillMount() {
		const self = this;

		this.selTag = this.selTag.bind(this);
		this.closeTagCasesList = this.closeTagCasesList.bind(this);

		const adminUnitId = this.props.route.queryParam('id');

		fetchView(adminUnitId)
		.then(res => self.setState({ data: res, oldAuID: adminUnitId }));

		this.state = {};
	}

	getChildren(parent) {
		const id = parent.id;
		return fetchNode(id);
	}

	selTag(tagId) {
		return () => this.setState({ selectedTag: tagId });
	}

	closeTagCasesList() {
		this.setState({ selectedTag: null });
	}

	render() {
		const data = this.state.data;
		if (!data) {
			return <WaitIcon />;
		}

		const adminUnitId = this.props.route.queryParam('id');

		return (
			<div className="mtop-2x">
			<Grid fluid>
				<Row>
					<Col sm={3}>
						<CasesTags tags={data.tags} onClick={this.selTag}/>
					</Col>
					<Col sm={9}>
						{
							this.state.selectedTag ? <TagCasesList onClose={this.closeTagCasesList} tag={this.state.selectedTag} adminUnitId={adminUnitId}/> : null
						}
						{
							!this.state.selectedTag ?
								<Grid fluid>
									<CasesDistribution
										root={data.places}
										onGetChildren={this.getChildren} />
								</Grid> : null
						}
					</Col>
				</Row>
			</Grid>
			</div>
			);
	}
}

Cases.propTypes = {
	route: React.PropTypes.object
};
