package org.msh.etbm.commons.commands.formhandler;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.commands.CommandType;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Responds form requests used in form operations in the client side
 *
 * Created by rmemoria on 29/6/16.
 */
@Component
public class CommandTypeFormRequest implements FormRequestHandler<List<CmdTypeItem>> {

    public static final String FORM_COMMAND_NAME = "command-types";

    @Autowired
    Messages messages;

    @Override
    public String getFormCommandName() {
        return FORM_COMMAND_NAME;
    }

    @Override
    public List<CmdTypeItem> execFormRequest(FormRequest req) {
        List<CommandType> lst = CommandTypes.ROOT.getChildren();

        List<CmdTypeItem> items = new ArrayList<>();
        for (CommandType cmd: lst) {
            items.add(generateClientItem(cmd));
        }

        return items;
    }

    protected CmdTypeItem generateClientItem(CommandType cmd) {
        CmdTypeItem item = new CmdTypeItem(cmd.getPath(), messages.get(cmd.resolveMessageKey()));

        if (cmd.getChildCount() == 0) {
            return item;
        }

        List<CmdTypeItem> lst = new ArrayList<>();

        for (CommandType child: cmd.getChildren()) {
            lst.add(generateClientItem(child));
        }

        item.setChildren(lst);

        return item;
    }
}
