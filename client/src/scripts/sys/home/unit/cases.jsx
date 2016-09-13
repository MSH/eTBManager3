import React from 'react';
import { Grid, Row, Col, Badge } from 'react-bootstrap';
import { Card, WaitIcon, CommandBar, Sidebar } from '../../../components';
import AdvancedSearch from '../cases/advanced-search';
import TagCasesList from '../cases/tag-cases-list';
import CasesUnit from './cases-unit';
import { app } from '../../../core/app';
import { server } from '../../../commons/server';



export default class Cases extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			selectedTag: null
		};
		this.newNotif = this.newNotif.bind(this);

		this.toggleSearch = this.toggleSearch.bind(this);
		this.closeTagCasesList = this.closeTagCasesList.bind(this);
		this.selTag = this.selTag.bind(this);
	}

	componentWillMount() {
		this.loadCases();
	}

	/**
	 * Load the cases to be displayed
	 * @return {[type]} [description]
	 */
	loadCases() {
		const self = this;

		const unitId = this.props.route.queryParam('id');

		// get data from the server
		server.post('/api/cases/unit/' + unitId, {})
		.then(res => self.setState({
			unitId: unitId,
			cases: {
				presumptives: res.presumptives,
				drtbCases: res.drtbCases,
				tbCases: res.tbCases
			},
			tags: res.tags,
			sel: res.presumptives.length > 0 ? 0 : 1
		}));
	}

	/**
	 * Called when user clicks on a new case/suspect
	 */
	newNotif(item) {
		const k = item.key.split('.');
		app.goto('/sys/home/cases/newnotif?diag=' + k[1] +
			'&cla=' + k[0] +
			'&id=' + this.state.unitId);
	}

	/**
	 * Called when user clicks on the advanced search button. Toggles the state
	 * of the search
	 */
	toggleSearch() {
		// toggle state of advanced search
		const search = !app.getState().caseSearch;
		app.setState({ caseSearch: search });
		this.forceUpdate();
	}

	selTag(tagId) {
		return () => this.setState({ selectedTag: tagId });
	}

	closeTagCasesList() {
		this.setState({ selectedTag: null });
	}

	render() {
		const caseSearch = app.getState().caseSearch;
		const unitId = this.props.route.queryParam('id');

		const cmds = [
			{
				title: __('cases.notifycase'),
				icon: 'file-text',
				submenu: [
					{
						title: __('CaseClassification.TB.confirmed'),
						onClick: this.newNotif,
						key: 'TB.CONFIRMED'
					},
					{
						title: __('CaseClassification.DRTB.confirmed'),
						onClick: this.newNotif,
						key: 'DRTB.CONFIRMED'
					},
					{
						title: __('CaseClassification.NTM.confirmed'),
						onClick: this.newNotif,
						key: 'NTM.CONFIRMED'
					}
				]
			},
			{
				title: __('cases.notifysusp'),
				icon: 'file-text',
				submenu: [
					{
						title: __('CaseClassification.TB.suspect'),
						onClick: this.newNotif,
						key: 'TB.SUSPECT'
					},
					{
						title: __('CaseClassification.DRTB.suspect'),
						onClick: this.newNotif,
						key: 'DRTB.SUSPECT'
					},
					{
						title: __('CaseClassification.NTM.suspect'),
						onClick: this.newNotif,
						key: 'NTM.SUSPECT'
					}
				]
			}
		];
		const commands = [
			{
				label: __('cases.advancedsearch'),
				icon: 'feed',
				onClick: this.toggleSearch
			}
		];

		return (
			<Grid fluid>
			<Row className="mtop">
				<Col sm={3}>
					<Card>
						<CommandBar commands={cmds} />
						<div className="text-muted">{'Views'}</div>
						<Sidebar route={this.props.route} items={[
							{
								title: 'Cases browse',
								icon: 'anchor'
							},
							{
								title: 'Advanced search',
								icon: 'arrows'
							},
							{
								title: 'Cases by tag',
								icon: 'bell'
							}
						]}/>
					</Card>
					{
						this.state.tags &&
						<Card className="mtop" title="Tags">
							<div>
								{
									!this.state.tags ? <WaitIcon type="card" /> :
									this.state.tags.map(item => (
										<a key={item.id} className={'tag-link tag-' + item.type.toLowerCase()} onClick={this.selTag(item)}>
											<Badge pullRight>{item.count}</Badge>
											<div className="tag-title">{item.name}</div>
										</a>
									))
								}
							</div>
						</Card>
					}
				</Col>
				<Col sm={9}>
				{
					caseSearch ?
						<AdvancedSearch onClose={this.toggleSearch}/> : null
				}
				{
					!caseSearch && this.state.selectedTag ?
						<TagCasesList onClose={this.closeTagCasesList} tag={this.state.selectedTag} unitId={unitId}/> : null
				}
				{
					!(caseSearch || this.state.selectedTag) &&
						<CasesUnit cases={this.state.cases} />
				}
				</Col>
			</Row>
			</Grid>
		);
	}
}

Cases.propTypes = {
	route: React.PropTypes.object
};
