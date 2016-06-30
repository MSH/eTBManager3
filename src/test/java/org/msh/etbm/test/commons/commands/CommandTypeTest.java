package org.msh.etbm.test.commons.commands;

import org.junit.Test;
import org.msh.etbm.commons.commands.CommandType;
import org.msh.etbm.commons.commands.CommandTypes;

import static org.junit.Assert.*;

/**
 * Check if command type navigation is working properly
 *
 * Created by rmemoria on 29/6/16.
 */
public class CommandTypeTest {

    @Test
    public void testFind() {
        // get root command types
        CommandType cmdRoot = CommandTypes.ROOT;

        // check children count
        assertTrue(cmdRoot.getChildCount() > 0);
        assertNotNull(cmdRoot.getChildren());

        // check find command
        CommandType cmd = cmdRoot.find(CommandTypes.ADMIN);
        assertNotNull(cmd);
        assertTrue(cmd.getChildCount() > 0);

        // find inside admin command type
        cmd = cmd.find("users");
        assertNotNull(cmd);
        assertEquals(cmd.getChildCount(), cmd.getChildren().size());

        // find from root using full path
        String path = CommandTypes.ADMIN_USERS;
        cmd = cmdRoot.find(path);
        assertEquals("users", cmd.getId());
        assertTrue(cmd.getChildCount() > 0);

        // using CRUD commands
        String pathCrud = path + "." + CommandTypes.CMD_CREATE;
        cmd = cmdRoot.find(pathCrud);
        assertNotNull(cmd);
        assertEquals(pathCrud, cmd.getPath());
        assertEquals(0, cmd.getChildCount());

        pathCrud = path + "." + CommandTypes.CMD_UPDATE;
        cmd = cmdRoot.find(pathCrud);
        assertNotNull(cmd);
        assertEquals(pathCrud, cmd.getPath());
        assertEquals(0, cmd.getChildCount());

        pathCrud = path + "." + CommandTypes.CMD_DELETE;
        cmd = cmdRoot.find(pathCrud);
        assertNotNull(cmd);
        assertEquals(pathCrud, cmd.getPath());
        assertEquals(0, cmd.getChildCount());
    }
}
