import React from 'react';
import { Card } from '../../../components';
import FiltersCard from '../filters/filters-card';
import { server } from '../../../commons/server';
import CasesList from './cases-list';
import SU from '../../session-utils';

import CrudController from '../../packages/crud/crud-controller';
import FakeCRUD from '../../../commons/fake-crud';


export default class AdvancedSearch extends React.Component {

    constructor(props) {
        super(props);
        this.searchCases = this.searchCases.bind(this);

        this.state = { };
    }

    componentWillMount() {
        const self = this;

        // get list of filters from the server
        server.post('/api/cases/search/init')
        .then(res => self.setState({ filters: res.filters }));
    }

    /**
     * Called when user clicks on the search button. Search the cases based on the filter values
     */
    searchCases(filters) {
        const req = {
            pageSize: 50,
            filters: {},
            scope: this.props.scope,
            scopeId: this.props.scopeId
        };

        // check if filters were declared
        if (filters) {
            Object.keys(filters).forEach(id => {
                const filter = filters[id];
                if (filter.value) {
                    req.filters[id] = filter.value;
                }
            });
        }

        const crud = new FakeCRUD('/api/cases/search');
        const controller = new CrudController(crud, {
            pageSize: 50
        });
        controller.initList(req);

        this.setState({ controller: controller });
    }

    /**
     * Called when the user changes the filter to a new filter in the filter panel
     * @param  {object} fval the filter value
     */
    changeFilter(fval) {
        const self = this;
        return filter => {
            fval.filter = filter;
            self.forceUpdate();
        };
    }

    caseClick(data) {
        window.location = SU.caseHash(data.id);
    }

    render() {
        const filters = this.state.filters;

        return (
            <div>
                <FiltersCard title={__('cases.advancedsearch')}
                    btnLabel={__('action.search')}
                    filters={filters}
                    onSubmit={this.searchCases}
                    />

                {
                    this.state.controller &&
                    <Card>
                        <CasesList controller={this.state.controller}
                            onCaseClick={this.caseClick}/>
                    </Card>
                }
            </div>
            );
    }
}

AdvancedSearch.propTypes = {
    scope: React.PropTypes.string.isRequired,
    scopeId: React.PropTypes.string
};
