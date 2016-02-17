package org.msh.etbm.commons.mail;

/**
 * Exception generated when sending an e-mail message
 * Created by rmemoria on 16/2/16.
 */
public class MailSenderException extends RuntimeException {
    MailSenderException(Exception e) {
        super(e);
    }
}
