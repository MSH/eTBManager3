# e-TB Manager 3

The SIAPS Program is funded by the U.S. Agency for International Development (USAID) under cooperative agreement AID-OAA-A-11-00021 and implemented by Management Sciences for Health. The information provided on this web site is not official U.S. Government information and does not represent the views or positions of the U.S. Agency for International Development or the U.S. Government. 

## Goal

This repository contains the source code of e-TB Manager 3.

e-TB Manager is a web-based tool for managing all the information needed by national TB control programs. It integrates data across all aspects of TB control, including information on suspects, patients, medicines, laboratory testing, diagnosis, treatment, and outcome.

## Getting started

In order to build e-TB Manager from the source code, you will need the following programs installed:

* Java JDK 1.8+
* Maven 3+

In order to work with client side development, it is highly recommended that you also install the following programs:

* Node 5.0+
* Gulp 3.8+

Development tools
* A development environment of your choice - Recommended: `IntelliJ` for server development, and `Sublime 3` for client development
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

All these frameworks are ***automatically*** loaded by the respective building tools (Maven and NPM), so the list is just a reference.

## Directories & files

Below is a list of the folders in the root directory of the source code:

* `src` - The server source code (used by Maven);
* `client` - The client source code (used by NodeJS);
* `resource` - Any file that is not part of the source, but it is important to keep them as templates, configuration examples, etc.

These are folders generated by the build tools (not to be included in the git repository:

* `target` - Folder where the final artifacts are generated (for instance, etbmanager.jar);
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


## Development environment

You need the following tools in order to start development:

* Git
* Java 1.8
* Maven 3+
* Node 5+
* Gulp 3.8+
* A development environment of your choice (IntelliJ recommended)

### Downloading the source code

The source code is stored in a Git repository, and the current Git URL is:

https://rmemoria@bitbucket.org/etbmanager/etbm3.git

To download the code, issue the git command

    git clone https://rmemoria@bitbucket.org/etbmanager/etbm3.git

You will need a user name and password for that.

Inside the repository, there are two main branches: `master` and `development`. The master contains the stable version, and should be merged with stable versions achieved in the development branch. So, if you want to change the code, move to the development branch:

    git checkout development

When you finish your changes, perform the following git sequence (as described in the git documentation):

1. Add all changed files to be committed

    git add .

2. Commit the changes providing a good description

    git commit -m "description of the changes"

3. Upload the changes to the remote server

    git push origin development



### Building from the source code

In order to generate a new version of e-TB Manager from the source code, you must issue the following Maven command:

    mvn clean package

This will install all necessary dependencies and generate a new version in `target/etbmanager-x.x.x.jar`, where x.x.x is the version number.

### Configuration file

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

or using Maven

    mvn clean package exec:exec

You must provide a configuration file etbmanager.properties, as described in the previous section.

When initialization finishes, open the URL below:

    http://localhost:8080/index.html

## Server side development

There is no restriction on the IDE in use, but the recommended one is IDEA IntelliJ.


### CheckStyle

The build tool uses [CheckStyle](http://checkstyle.sourceforge.net/) to guarantee that Java code follows code standards. CheckStyle rules are located in the `checkstyle.xml` file. To check if your code adheres to the rules, you may use maven for that:

    mvn checkstyle:check

**IDEA CheckStyle plugin** - There is a [CheckStyle plugin](https://plugins.jetbrains.com/plugin/1065) available, which makes it easier to display the CheckStyle issues.

Once installed, go to Preferences -> Other Setting -> CheckStyle, and include the file `checkstyle.xml` located in the project root folder.

## Client side development

e-TB Manager client side (browser code) uses `npm` and `gulp` as the building tool system. `npm` is used for dependency management, while `gulp` is used for running several tasks like the development server, testing the code, system building and much more.

Although not required, a group of tasks are available in `gulp` to make client side development easier. They are implemented in the `gulpfile.js` . The main command lines are:

**`npm run run`** - Prepare and run the client side on a proxy web server using gulp (the same as running `gulp run`). This proxy web-server has the following features:

* Provides client files (js, html, css, etc);
* Automatically update browser window when a file is changed;
* Proxy requests to the server-side (the server side must be running);
* Cache JS files in order to make quick rebuild on file changes;

**`gulp build`** - Prepare and build all client files and copy them to the folder `src\main\resources\static`.

**`gulp test`** - Run the UI tests (TO BE DONE);

### Sublime plugins

Sublime is a lightweight text editor widely used for Nodes and Javascript development. One of the strenght in Sublime is its plugin system. Although not required, it is recommended to use the following plugins:

* SublimeLinter
* SublimeLinter-contrib-eslint
* Babel
* ColorPicker
* DocBlockr
* LESS
* MarkdownEditing

`SublimeLinter` is a plugin for Sublime Text 3 that provides a framework for linting code, and `SublimeLinter-contrib-eslint` provides integration to the eslint tool, in order to provide JavaScript linting. Check how to install them and use them in Sublime.

 Inside the source code structure, `SublimeLinter-contrib-eslint` recognizes files called `.eslintrc` as the eslint configuration.

It is also necessary to install the following programs, in order to have the plugins working properly:

* [eslint](http://eslint.org/)
* [eslint-plugin-react](https://github.com/yannickcr/eslint-plugin-react)

### Atom plugins

Atom editor is another IDE that you can use to edit the client code. Please go to http://atom.io and follow instructions on how to install it.


## Testing

### JUnit tests

Server side tests are implemented using JUnit and Spring Boot under the `src/test/java` folder.

### Test configuration

When running test suites, the application will be started. When started in test mode, the application will search for the `etbmanager.properties` file in the `target/test` folder.

When executing tests using maven, it will automatically copy a configuration file from `resources/test/mysql/etbmanager.properties`.


### Executing the test
e-TB Manager follows the maven standard way of executing tests:

    mvn test

The source will be compiled and all tests will be executed.

### Other tests (TO BE DONE)

JUnit tests are not the only one available - The test suite also contains API call tests and UI tests.
