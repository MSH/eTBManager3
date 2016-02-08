/**
 * This script considers that database was properly created using liquibase during system startup, and it is
 * executed during JUnit startup
 */

/** Erase all workspaces and user, in order to force initialization */
delete from workspace;
delete from sys_user;
delete from workspacelog;
delete from userlog;
delete from commandhistory;