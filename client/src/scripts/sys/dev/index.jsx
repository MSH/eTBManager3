
import React from 'react';
import { Profile, Fluidbar } from '../../components/index';
import SidebarContent from '../sidebar-content';

/** Pages of the public module */
import Home from './home';
import DatePickerExamples from './date-picker-examples';
import ReacttableExample from './reacttable-example';
import FormExample from './form-example';
import CrudExample from './crud-example';
import TableFormExample from './table-form-example';
import ShowMessage from './show-message';
import ServerForms from './server-forms';
import YearPickerExample from './year-picker-example';


const sidebar = [
    {
        title: 'Developers playground',
        icon: 'sitemap',
        path: '/index',
        view: Home
    },
    {
        title: 'Calendar',
        view: DatePickerExamples,
        path: '/calendar',
        icon: 'calendar'
    },
    {
        title: 'React Table',
        view: ReacttableExample,
        path: '/reacttable',
        icon: 'table'
    },
    {
        title: 'Table Form',
        view: TableFormExample,
        path: '/tableform',
        icon: 'table'
    },
    {
        title: 'Forms',
        view: FormExample,
        path: '/form-example',
        icon: 'reddit-alien'
    },
    {
        title: 'CRUD',
        view: CrudExample,
        path: '/crud-example',
        icon: 'ship'
    },
    {
        title: 'Show message',
        view: ShowMessage,
        path: '/show-message',
        icon: 'bicycle'
    },
    {
        title: 'Server form',
        view: ServerForms,
        path: '/server-form',
        icon: 'wpforms'
    },
    {
        title: 'Year picker',
        view: YearPickerExample,
        path: '/year-picker',
        icon: 'bath'
    }
];

/**
 * The page controller of the public module
 */
export default class Index extends React.Component {

    render() {
        return (
            <div>
                <Fluidbar>
                    <div className="margin-2x">
                        <Profile size="large"
                            title="Developers playground"
                            subtitle="Your place to test new stuff"
                            imgClass="prof-male"
                            fa="laptop" />
                    </div>
                </Fluidbar>
                <SidebarContent menu={sidebar} path="/sys/dev" route={this.props.route} />
            </div>
            );
    }
}

Index.propTypes = {
    // the route object given from the route lib
    route: React.PropTypes.object,
    // the main path of the pages in the admin menu
    path: React.PropTypes.string
};
