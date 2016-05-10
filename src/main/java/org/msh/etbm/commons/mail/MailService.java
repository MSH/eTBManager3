package org.msh.etbm.commons.mail;

import java.util.Map;

/**
 * Component responsible for delivering e-mail messages using freemarker template engine to generate the messages
 *
 * Created by rmemoria on 16/2/16.
 */
public interface MailService {

    /**
     * Check if the mail service is enabled
     * @return true if the service is enabled, otherwise return false
     */
    boolean isEnabled();

    /**
     * Send an e-mail message to an address using a template file
     * @param to the destination e-mail address
     * @param subject the message subject
     * @param template the message content template
     * @param model the data model containing the variables of the template
     */
    void send(String to, String subject, String template, Map<String, Object> model);
}
