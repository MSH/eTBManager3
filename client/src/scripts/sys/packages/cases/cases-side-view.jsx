import React from 'react';
import { Card, CommandBar, Sideview } from '../../../components';
import { server } from '../../../commons/server';
import { RouteView } from '../../../components/router';

import AdvancedSearch from './advanced-search';
import TagCasesList from './tag-cases-list';
import SummaryList from './summary-list';
import CasesReports from '../cases/cases-reports';
import ReportView from '../reports/report-view';
import ReportEditor from '../reports/report-editor';

/**
 * These are common views used in the side view of workspace, admin units and units
 */
const commonViews = [
    {
        title: __('cases.search'),
        icon: 'search',
        path: '/search',
        view: AdvancedSearch,
        sideView: true
    },
    {
        title: __('reports'),
        icon: 'table',
        path: '/reports',
        view: CasesReports,
        sideView: true
    },
    {
        title: __('admin.tags'),
        path: '/tag',
        view: TagCasesList
    },
    {
        title: __('global.summary'),
        path: '/summary',
        view: SummaryList
    },
    {
        title: __('reports'),
        path: '/reportedt',
        view: ReportEditor
    },
    {
        title: __('reports'),
        path: '/report',
        view: ReportView
    }
];


/**
 * Display a common left side view with commands, views and tags related to the
 * provided scope in the properties
 */
export default class CasesSideView extends React.Component {

    /**
     * Create a route table with the specific views of the page and the common views.
     * This is to help the parent component to create a route table for its RouteView
     * component
     */
    static createRoutes(views) {
        const lst = views ? views.slice(0).concat(commonViews) : commonViews;

        return RouteView.createRoutes(lst);
    }

    constructor(props) {
        super(props);
        this.newNotif = this.newNotif.bind(this);

        this.state = {
            cmds: [
                {
                    title: __('cases.notifycase'),
                    icon: 'book',
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
                    icon: 'book',
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
            ]
        };
    }

    componentWillMount() {
        this.loadData();
    }

    /**
     * Load the data from the server and update state
     */
    loadData() {
        const self = this;

        const req = {
            scope: this.props.scope,
            scopeId: this.props.scopeId
        };

        // get data from the server
        server.post('/api/cases/summary', req)
        .then(res => self.setState({
            data: res
        }));
    }

    /**
     * Called when user clicks on a new case/suspect
     */
    newNotif(item) {
        const k = item.key.split('.');
        window.location = '#/sys/case/new?diag=' + k[1] +
            '&cla=' + k[0] +
            '&id=' + this.props.scopeId;
    }

    generateViews() {
        let views = this.props.views;
        // get only the views to dislay
        views = (views ? views.concat(commonViews) : commonViews).filter(it => it.sideView);

        const data = this.state.data;
        const res = this.props.scopeId ? views.map(it =>
            Object.assign({}, it, { path: it.path + '?id=' + this.props.scopeId }
        )) : views;

        res.splice(0, 0, { title: 'Views' });

        const qryparams = this.props.scopeId ? '&id=' + this.props.scopeId : '';

        // display the summary
        if (data.summary) {
            res.push({ title: __('global.summary') });
            data.summary.forEach(it => {
                res.push({
                    title: it.name,
                    count: it.count,
                    path: '/summary?sum=' + it.id + qryparams
                });
            });
        }

        // display the tags
        if (data.tags && data.tags.length > 0) {
            res.push({ title: __('admin.tags') });
            data.tags.forEach(it => {
                res.push({
                    title: it.name,
                    count: it.count,
                    path: '/tag?tag=' + it.id + qryparams,
                    className: 'tag-link-' + it.type.toLowerCase()
                });
            });
        } else {
            const tagNoResult = {
                noResultMsg: __('cases.details.tags.noresult2'),
                title: __('admin.tags'),
                icon: 'tags'
            };

            res.push(tagNoResult);
        }

        return res;
    }

    render() {
        const state = this.state;
        if (!state.data) {
            return null;
        }

        const scope = this.props.scope;

        return (
            <div>
                <Card>
                    {
                        scope === 'UNIT' &&
                        <CommandBar commands={this.state.cmds} />
                    }
                    <Sideview route={this.props.route}
                        views={this.generateViews()} />
                </Card>
            </div>
        );
    }
}

CasesSideView.propTypes = {
    scope: React.PropTypes.oneOf(['WORKSPACE', 'ADMINUNIT', 'UNIT']).isRequired,
    scopeId: React.PropTypes.string,
    route: React.PropTypes.object,
    views: React.PropTypes.array
};
