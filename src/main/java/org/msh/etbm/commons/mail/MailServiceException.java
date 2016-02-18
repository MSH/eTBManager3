package org.msh.etbm.commons.mail;

/**
 * Exception generated when sending an e-mail message
 * Created by rmemoria on 16/2/16.
 */
public class MailServiceException extends RuntimeException {
    public MailServiceException(Exception e) {
        super(e);
    }

    public MailServiceException(String message) {
        super(message);
    }
}
