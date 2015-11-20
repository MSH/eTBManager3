/**
 * First script called during application execution
 */

// import React from 'react';
// import { createStore, applyMiddleware } from 'redux';
// import { Provider } from 'react-redux';
// import thunk from 'redux-thunk';

// include babel modules
require('babel-core/register');

// application main component
import App from './core/app.jsx';
// import Http from 'http';
// import { runApp } from './core/actions';
// import { appReducer } from './core/reducers';


// load the style sheet in use
require('../styles/theme.css');
require('../styles/app.css');

// load font awesome
require('font-awesome-webpack');

// run the application
var app = new App();
app.run();
