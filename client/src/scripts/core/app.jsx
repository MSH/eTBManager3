'use strict';

import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import Toolbar from './toolbar.jsx';
import Footer from './footer.jsx';
import WaitIcon from '../components/wait-icon.jsx';
import RootContent from './root-content.jsx';


class App extends Component {

  render() {
  	let appState = this.props.state;
  	let fetching = appState && appState.fetching;

    return (
        <div>
        	<Toolbar appState={appState}></Toolbar>
            <div className='app-content'>

            {fetching &&
                <WaitIcon />
            }

            {!fetching &&
                <RootContent dispatch={this.props.dispatch} appState={appState}>
                </RootContent>
            }

            </div>
            <Footer></Footer>
        </div>
    );
  }
}


function mapStateToProps(appState) {
	return {state: appState};
}

export default connect(mapStateToProps)(App);