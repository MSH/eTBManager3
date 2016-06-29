/**
 * This script considers that database was properly created using liquibase during system startup, and it is
 * executed during JUnit startup
 */

/** Erase all workspaces and user, in order to force initialization */
DELETE FROM workspace;
DELETE FROM sys_user;
DELETE FROM workspacelog;
DELETE FROM userlog;
DELETE FROM commandhistory;