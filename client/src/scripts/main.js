/**
 * First script called during application execution
 */
'use strict';

import App from './core/app';

// load the style sheet in use
require('../styles/theme.css');
require('../styles/app.css');

/**
 * Run the main application
 */
App.run('content');

