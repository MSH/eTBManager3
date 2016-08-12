import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { WaitIcon } from '../../../components';
import { server } from '../../../commons/server';
import CasesTags from '../commons/cases-tags';
import CasesDistribution from '../commons/cases-distribution';
import TagCasesList from '../cases/tag-cases-list';


function fetchData(parentId) {
	const params = parentId ? '/places?id=' + parentId : '';
	return server.post('/api/cases/workspace' + params);
}


export default class Cases extends React.Component {

	componentWillMount() {
		const self = this;

		this.selTag = this.selTag.bind(this);
		this.closeTagCasesList = this.closeTagCasesList.bind(this);

		fetchData()
		.then(res => self.setState({ data: res }));

		this.state = {};
	}

	getChildren(parent) {
		const id = parent.id;
		return fetchData(id);
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

		return (
			<div className="mtop-2x">
			<Grid fluid>
				<Row>
					<Col sm={3}>
						<CasesTags tags={data.tags} onClick={this.selTag}/>
					</Col>
					<Col sm={9}>
						{
							this.state.selectedTag ? <TagCasesList onClose={this.closeTagCasesList} tag={this.state.selectedTag}/> : null
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
