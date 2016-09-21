import React from 'react';
import { Grid, Row, Col, Nav, NavItem } from 'react-bootstrap';
import { Fluidbar, Profile } from '../../../components';
import { RouteView } from '../../../components/router';


/**
 * Standard view of a profile page of an entity (workspace, patient, unit, etc).
 * It contains a banner at the top with the name and icon of the entity. It also
 * supports an horizontal menu at the top of the page
 */
export default class FrontPage extends React.Component {

    constructor(props) {
        super(props);

        this.tabIndex = this.tabIndex.bind(this);
        this.viewClick = this.viewClick.bind(this);
    }

    tabIndex() {
        const route = this.props.route;
        const path = route.forpath;
        if (!path) {
            // if no path, search for the default path
            return this.props.views.findIndex(it => it.default);
        }
        return this.props.views.findIndex(it => path.startsWith(it.path));
    }

    viewClick(key) {
        const path = this.props.route.path + this.props.views[key].path;
        // preserve the query params of the original hash
        const params = window.location.hash.split('?')[1];

        window.location.hash = '#' + path + (params ? '?' + params : '');
    }

    render() {

        const routes = this.props.views ? RouteView.createRoutes(this.props.views) : null;

        return (
            <div>
            <Fluidbar>
                <Grid>
                    <Row>
                        <Col md={12}>
                            <div className="margin-2x">
                                <Profile title={this.props.title}
                                    subtitle={this.props.subtitle}
                                    type={this.props.type}
                                    size="large"
                                    />
                            </div>
                        </Col>
                    </Row>
                    {
                        this.props.views &&
                        <Row>
                            <Col md={12}>
                                <Nav bsStyle="tabs" activeKey={this.tabIndex()}
                                    onSelect={this.viewClick}
                                    className="app-tabs">
                                    {
                                        this.props.views.map((it, index) =>
                                            <NavItem key={index} eventKey={index}>
                                                {it.title}
                                            </NavItem>)
                                    }
                                </Nav>
                            </Col>
                        </Row>
                    }
                </Grid>
            </Fluidbar>
            {
                routes &&
                <RouteView routes={routes} viewProps={this.props.viewProps}/>
            }
            </div>
            );
    }
}

FrontPage.propTypes = {
    type: React.PropTypes.string.isRequired,
    title: React.PropTypes.any.isRequired,
    subtitle: React.PropTypes.any,
    views: React.PropTypes.array,
    viewProps: React.PropTypes.any,
    route: React.PropTypes.object
};
