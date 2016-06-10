package org.msh.etbm.services.sys.info;

/**
 * Possible system states
 */
public enum SystemState {
    NEW,        // is a new instance of the system and requires a initialization
    READY,      // the system is ready to be used
    AUTH_REQUIRED // authentication is required
}
