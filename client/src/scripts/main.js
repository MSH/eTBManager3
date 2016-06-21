/**
 * Client application entry-point
 */

// include babel modules
import 'babel-polyfill';

// application main component
import { App, init, app } from './core/app';

// load the style sheet in use
import '../styles/theme.css';

// load font awesome
import 'font-awesome-webpack';

// init application class
init(new App());

app.run();
