import React from 'react';
import { Card, CommandBar, Sideview } from '../../../components';
import { server } from '../../../commons/server';


export default class CasesSideView extends React.Component {

    constructor(props) {
        super(props);
        this.newNotif = this.newNotif.bind(this);

        this.state = {
            cmds: [
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
        window.location = '#/sys/home/cases/newnotif?diag=' + k[1] +
            '&cla=' + k[0] +
            '&id=' + this.props.scopeId;
    }

    generateViews() {
        const data = this.state.data;
        const res = this.props.views.map(it =>
            Object.assign({}, it, { path: it.path + '?id=' + this.props.scopeId }
        ));

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
        if (data.tags) {
            res.push({ title: __('admin.tags') });
            data.tags.forEach(it => {
                res.push({
                    title: it.name,
                    count: it.count,
                    path: '/tag?tag=' + it.id + qryparams,
                    className: 'tag-link tag-' + it.type.toLowerCase()
                });
            });
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
