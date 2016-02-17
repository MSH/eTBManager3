package org.msh.etbm.commons.mail;

import java.util.Map;

/**
 * Component responsible for delivering e-mail messages using freemarker template engine to generate the messages
 *
 * Created by rmemoria on 16/2/16.
 */
public interface MailService {

    void send(String to, String subject, String template, Map<String, Object> model);
}
