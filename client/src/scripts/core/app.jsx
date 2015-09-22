'use strict';

import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import Toolbar from './toolbar.jsx';
import WaitIcon from '../components/wait-icon.jsx';
import RootContent from '../components/root-content.jsx';


class App extends Component {

  render() {
  	let st = this.props.state;
  	let fetching = st && st.fetching;

    return (
        <div>
        	<Toolbar></Toolbar>
            <div className='app-content'>

            {fetching &&
                <WaitIcon />
            }

            {!fetching &&
                <RootContent dispatch={this.props.dispatch} appState={this.props.state}>
                </RootContent>
            }

            </div>
        </div>
    );
  }
}


function mapStateToProps(appState) {
	return {state: appState};
}

export default connect(mapStateToProps)(App);