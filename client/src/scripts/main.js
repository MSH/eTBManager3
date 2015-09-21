/**
 * First script called during application execution
 */
'use strict';

import React from 'react';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import thunk from 'redux-thunk';


// application main component
import App from './core/app.jsx';
import Http from 'http';
import { runApp } from './core/actions';
import { appReducer } from './core/reducers';


// load the style sheet in use
require('../styles/theme.css');
require('../styles/app.css');


/**
 * Create application store
 */
let createStoreWithMiddleware = applyMiddleware(thunk)(createStore);

let store = createStoreWithMiddleware(appReducer);


/**
 * Render application
 */
React.render(
  <Provider store={store}>
    {() => <App />}
  </Provider>,
  document.getElementById('content'));


/**
 * Run the application calling the run action
 */
store.dispatch(runApp());
