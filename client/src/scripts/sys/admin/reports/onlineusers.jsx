
import React from 'react';
import { Col, Row, Badge } from 'react-bootstrap';
import { Card, ReactTable, WaitIcon, Profile } from '../../../components/index';
import { server } from '../../../commons/server';
import moment from 'moment';

/**
 * The page controller of the public module
 */
export default class OnlineUsers extends React.Component {

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

        return server.post('/api/admin/rep/onlineusers')
        .then(res => {
            // generate new result
            const result = { count: res.length, list: res };
            // set state
            self.setState({ values: result });
            // return to the promise
            return result;
        });
    }

    headerRender(count) {
        const countHTML = <Badge className="tbl-counter">{count}</Badge>;

        // create the header of the card
        return (
            <Row>
                <Col sm={12}>
                    <h4>{__('admin.websessions')} {countHTML}</h4>
                </Col>
            </Row>
            );
    }

    collapseRender(item) {
        return (<div className="text-small">
                    <dl>
                        <Col sm={4}>
                            <dt>{__('User.login') + ':'}</dt>
                            <dd>{item.userLogin}</dd>
                        </Col>
                        <Col sm={4}>
                            <dt>{__('admin.websessions.lastrequest') + ':'}</dt>
                            <dd>
                                {moment(item.lastAccess).format('L LT')}
                            </dd>
                        </Col>
                        <Col sm={4}>
                            <dt>{__('admin.websessions.sessiontime') + ':'}</dt>
                            <dd>{moment(item.loginDate).fromNow(true)}</dd>
                        </Col>
                    </dl>
                </div>);
    }

    render() {
        if (!this.state || !this.state.values) {
            return <WaitIcon type="card" />;
        }

        const colschema = [
            {
                title: __('User'),
                content: item => <Profile size="small" title={item.userName} type="user" />,
                size: { sm: 4 }
            },
            {
                title: __('UserLogin.loginDate'),
                content: item => moment(item.loginDate).format('L LT'),
                size: { sm: 4 }
            },
            {
                title: __('admin.websessions.idletime'),
                content: item => moment(item.lastAccess).fromNow(true),
                size: { sm: 4 }
            }
        ];

        return (
                <div>
                    <Card header={this.headerRender(this.state.values.count)}>
                        <Row>
                            <Col md={12}>
                                <ReactTable columns={colschema}
                                    values={this.state.values.list}
                                    onExpandRender={this.collapseRender} />
                            </Col>
                        </Row>
                    </Card>
                </div>
            );
    }
}

OnlineUsers.propTypes = {
    route: React.PropTypes.object
};
