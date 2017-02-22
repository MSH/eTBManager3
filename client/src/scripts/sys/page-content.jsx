
import React from 'react';
import { Fluidbar } from '../components/index';
import SidebarContent from './sidebar-content';


/**
 * The page controller of the public module
 */
export default class PageContent extends React.Component {

    render() {
        return (
            <div>
                {
                    this.props.title &&
                    <Fluidbar>
                        <h3>{this.props.title}</h3>
                    </Fluidbar>
                }
                <SidebarContent menu={this.props.menu}
                    path={this.props.path}
                    route={this.props.route}
                    viewProps={this.props.viewProps}
                    queryParams={this.props.queryParams} />
            </div>
        );
    }
}

PageContent.propTypes = {
    // the array with menu options to display in the left menu
    menu: React.PropTypes.array,
    // the title of the page
    title: React.PropTypes.string,
    // the route object given from the route lib
    route: React.PropTypes.object,
    // the main path of the pages in the admin menu
    path: React.PropTypes.string,
    // properties passed forward to the route view component
    viewProps: React.PropTypes.object,
    queryParams: React.PropTypes.any
};
