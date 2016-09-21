import React from 'react';
import UnderConstruction from '../under-construction';

export default class General extends React.Component {

    render() {
        return <UnderConstruction />;
    }
}

General.propTypes = {
    route: React.PropTypes.object
};
