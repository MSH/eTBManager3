# e-TB Manager 3

## Goal

This repository contains the source code of e-TB Manager 3.

e-TB Manager is a web-based tool for managing all the information needed by national TB control programs. It integrates data across all aspects of TB control, including information on suspects, patients, medicines, laboratory testing, diagnosis, treatment, and outcome.

## Getting started

In order to build e-TB Manager from the source code, you will need the following programs installed:

* Java JDK 1.8
* Maven 3+

In order to work with client side development, it is higly recommended that you also install the following programs:

* Node 0.12.7+
* Gulp 3.8+

Development tools
* A development environment of your choice - Recommended: IntelliJ for server development, and Sublime for client development
* eslint, for client linting


Installation instructions can be found in the site of each one of these programs.

## Frameworks and libraries

Below is the list of frameworks and libraries used in e-TB Manager.

### Server side
* Spring boot (currently 1.2.6);
* Spring JPA, using Hibernate;
* Freemarker, as template engine;
* Liquibase, for database initialization;
* Dozer, for object to object mapping;
* Google Guava;

### Client side
* Twitter Bootstrap 3;
* React framework;
* React-bootstrap, to link react and bootstrap;
* Superagent, for Server requests;
* Underscore;
* Font-awesome 4;


## Directories & files

Below is a list of the folders in the root directory of the source code:

* `src` - The server source code (used by Maven);
* `client` - The client source code (used by NodeJS);
* `resource` - Any file that is not part of the source, but it is important to keep as templates, configuration examples, etc.

These are folders generated by the build tools (not to be included in the git repository:

* `target` - Where the final arctfacts are generated (for instance, etbmanager.jar);
* `node` - Local version of NodeJS installed by Maven and used during the build process;
* `node_modules` - NodeJS modules installed by NPM and used in the client folder;
* `.idea` - Created by IntelliJ IDE, if used for development;

These are the main files in the root folder:

* `pom.xml` - Maven project file;
* `gulpfile.js` - Gulp project file, responsible for development, testing and build of the client code;
* `package.json` - List of dependencies used in the client side (required by NPM tool);
* `webpack.config.js` - Used by webpack tool for UI building and development;
* `.jshintrc` - Used by jshint for UI validation;
* `.gitignore` - List of files and folders to be ignored by git;


## Preparing the development environment

You need the following tools in order to start development:

* Java 1.8
* Maven 3+
* Node 0.12.7+
* Gulp 3.8+
* A development environment of your choice (IntelliJ recommended)


## Building from the source code

In order to generate a new version of e-TB Manager from the source code, you must issue the following Maven command:

    mvn clean package

This will install all necessary dependencies and generate a new version in `target/etbmanager-x.x.x.jar`, where x.x.x is the version number.

## Configuration file

Before running e-TB Manager, it is necessary to create a text-file called `etbmanager.properties` containing information about e-TB Manager  configuration.

This file is a text-based property file in the format value=key and must be in the same directory of the `ebmanager-xxx.jar` (or in the working directory, if set).

An example of this file can be found in the `resources` folder. These are the main properties available:

    db.url= # jdbc connection string
    db.user= # database user name
    db.password= # database password
    web.port = # the web server port to use

### Supported databases

By now, only two databases are supported:

* MySQL 5.5+ - Recommended when installing in a server computer;
* HSQLDB - Recommended when installing in a desktop computer for off-line and local usage;

Below are examples of connection strings:

#### MySQL

    db.url = jdbc:mysql://localhost/etbmanager

#### HSQLDB

    db.url = jdbc:hsqldb:file:database/etbmanager;default_schema=true

## Running e-TB Manager

In order to run e-TB Manager, just run it as any other java application:

    java -jar etbmanager-xxx.jar

You must provide a configuration file etbmanager.properties, as described in the previous section.

When initialization finishes, open the URL below:

    http://localhost:8080/index.html


## Development on the client side

e-TB Manager client side (HTML pages and code) uses `npm` and `gulp` as the building tool system. `npm` is used for dependency management, while `gulp` is used for running the development server, testing the code, system building and much more.

Although not required, a group of tasks are available in `gulp` to make client side development easier. They are implemented in the `gulpfile.js` . The main command lines are:

`gulp run` - Prepare and run the client side on a proxy web server. This proxy web-server has the following features:

* Provides client files (js, html, css, etc);
* Automatically update browser window when a file is changed;
* Proxy requests to the server-side (the server side must be running);
* Cache JS files in order to make quick rebuild on file changes;

Once you call `gulp run`, a browser window will be opened with the initial system URL.

`gulp build` - Prepare and build all client files and copy them to the folder `src\main\resources\static`.

`gulp test` - Run the UI tests (TO BE DONE);

### Sublime plugins

Sublime is a lightweight text editor widely used for Nodes and Javascript development. One of the strenght in Sublime is its plugin system. Although not required, it is recommended to use the following plugins:

* SublimeLinter
* SublimeLinter-contrib-eslint

`SublimeLinter` is a plugin for Sublime Text 3 that provides a framework for linting code, and `SublimeLinter-contrib-eslint` provides integration to the eslint tool, in order to provide JavaScript linting. Check how to install them and use in Sublime.

 Inside the source code structure, `SublimeLinter-contrib-eslint` recognizes files called `.eslintrc` as the eslint conguration.

It is also necessary to install the following programs:

* eslint
* eslint-plugin-react
