import React from 'react';
import { Grid, Col, Row, Alert } from 'react-bootstrap';
import { Card, FormDialog, WaitIcon } from '../../../components';
import { server } from '../../../commons/server';

export default class SysConfig extends React.Component {

    constructor(props) {
        super(props);

        this.saveConfig = this.saveConfig.bind(this);

        this.state = {
            schema: {
                controls: [
                    {
                        property: 'systemURL',
                        label: __('SystemConfig.systemURL'),
                        type: 'string',
                        max: 200,
                        required: true,
                        size: { sm: 12 }
                    },
                    {
                        property: 'pageRootURL',
                        label: __('SystemConfig.pageRootURL'),
                        type: 'string',
                        max: 200,
                        required: true,
                        size: { sm: 12 }
                    },
                    {
                        property: 'adminMail',
                        label: __('SystemConfig.adminMail'),
                        type: 'string',
                        max: 200,
                        required: true,
                        size: { sm: 12 }
                    },
                    {
                        property: 'ulaActive',
                        label: __('SystemConfig.ulaActive'),
                        type: 'bool',
                        required: true,
                        size: { sm: 12 }
                    },
                    {
                        property: 'allowRegPage',
                        label: __('SystemConfig.allowRegPage'),
                        type: 'bool',
                        size: { sm: 12 },
                        onChange: doc => {
                            if (!doc.allowRegPage) {
                                doc.workspace = null; doc.unit = null; doc.userProfile = null;
                            }
                        }
                    },
                    {
                        property: 'workspace',
                        label: __('Workspace'),
                        type: 'select',
                        options: 'workspaces',
                        visible: doc => doc.allowRegPage,
                        required: doc => doc.allowRegPage,
                        size: { sm: 12 },
                        onChange: doc => { doc.unit = null; doc.userProfile = null; }
                    },
                    {
                        property: 'unit',
                        label: __('Unit'),
                        type: 'unit',
                        required: doc => !!doc.workspace,
                        workspaceId: doc => doc.workspace,
                        visible: doc => !!doc.workspace,
                        size: { sm: 12 }
                    },
                    {
                        property: 'userProfile',
                        label: __('UserProfile'),
                        type: 'select',
                        required: doc => !!doc.workspace,
                        options: 'profiles',
                        params: {
                            workspaceId: doc => doc.workspace
                        },
                        visible: doc => !!doc.workspace,
                        size: { sm: 12 }
                    }
                ]
            }
        };
    }

    componentWillMount() {
        const self = this;
        server.get('/api/admin/sysconfig')
        .then(res => self.setState({ doc: res }));
    }

    saveConfig(doc) {
        this.setState({ msg: null });

        const self = this;
        return server.post('/api/admin/sysconfig', doc)
        .then(() => {
            self.setState({ msg: __('admin.syssetup.success') });
        });
    }

    render() {
        const doc = this.state.doc;

        return (
            <Grid fluid>
                <Row>
                    <Col sm={8}>
                        <Card title={this.props.route.data.title}>
                            {
                                doc ?
                                <FormDialog
                                    wrapType={'none'}
                                    schema={this.state.schema}
                                    doc={doc}
                                    onConfirm={this.saveConfig}
                                    hideCancel
                                /> :
                                <WaitIcon type="card" />
                            }
                            {
                                this.state.msg &&
                                <div className="mtop">
                                    <Alert>{this.state.msg}</Alert>
                                </div>
                            }
                        </Card>
                    </Col>
                </Row>
            </Grid>
            );
    }
}

SysConfig.propTypes = {
    route: React.PropTypes.object
};
