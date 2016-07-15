package org.msh.etbm.commons.mail;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import org.msh.etbm.Messages;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Support for i18n in e-mail messages used internally by {@link MailServiceImpl}
 * Created by rmemoria on 17/2/16.
 */
public class MessageResolverMethod implements TemplateMethodModelEx {

    private Messages messages;

    public MessageResolverMethod(Messages messages) {
        this.messages = messages;
    }

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() < 1) {
            throw new MailServiceException("Wrong number of arguments in msg function");
        }

        String key = ((TemplateScalarModel) list.get(0)).getAsString();

        if (key == null || key.isEmpty()) {
            throw new MailServiceException("No message key found in msg function");
        }

        // get the displayable string
        String msg = messages.get(key);

        // if there is just one argument, simply return the message
        if (list.size() == 1) {
            return msg;
        }

        // more than 1 argument, so it is expected that message contains arguments to be inserted
        List args = new ArrayList<>(list);
        args.remove(0);
        return MessageFormat.format(msg, args.toArray());
    }
}
