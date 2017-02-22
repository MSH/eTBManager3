package org.msh.etbm.test.commons.commands;

import org.junit.Test;
import org.msh.etbm.commons.commands.CommandType;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.commands.formhandler.CmdTypeItem;
import org.msh.etbm.commons.commands.formhandler.CommandTypeFormRequest;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestService;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rmemoria on 29/6/16.
 */
public class CmdTypesFormRequestTest  extends AuthenticatedTest {

    private static final String ID = "anyID";

    @Autowired
    FormRequestService formRequestService;

    @Test
    public void execute() {
        List<FormRequest> reqs = new ArrayList<>();

        FormRequest req = new FormRequest();
        req.setCmd(CommandTypeFormRequest.FORM_COMMAND_NAME);
        req.setId(ID);
        reqs.add(req);

        Map<String, Object> res = formRequestService.processFormRequests(reqs);

        Object val = res.get(ID);

        assertNotNull(val);

        List<CmdTypeItem> lst = (List<CmdTypeItem>)val;

        assertEquals(CommandTypes.ROOT.getChildCount(), lst.size());

        for (CmdTypeItem item: lst) {
            assertNotNull(item.getId());
            assertNotNull(item.getName());

            CommandType cmd = CommandTypes.get(item.getId());
            assertNotNull(cmd);
            assertEquals(cmd.getId(), item.getId());

            int count = item.getChildren() != null ? item.getChildren().size() : 0;
            assertEquals("Failed for item " + cmd.getPath(), cmd.getChildCount(), count);
        }
    }
}
