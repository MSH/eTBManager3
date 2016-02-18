package org.msh.etbm.commons.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.msh.etbm.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the mail service implementation
 * Created by rmemoria on 16/2/16.
 */
@Service
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Value("${mail.from}")
    String mailFrom;

    @Value("${mail.host}")
    String mailHost;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    Configuration configuration;

    @Autowired
    Messages messages;


    @Override
    public boolean isEnabled() {
        // to be enabled, it must provide at least the mail server host and the recipient of the message
        return (mailHost != null && !mailHost.isEmpty()) &&
                (mailFrom != null && !mailFrom.isEmpty());
    }

    /**
     * Send an e-mail message based on a template, to a given e-mail address
     * @param to the destination e-mail address
     * @param subject the subject of the e-mail
     * @param template the e-mail template in freeMarker style
     * @param model the data to be used in the template
     */
    public void send(String to, String subject, String template, Map<String, Object> model) {
        if (mailFrom == null) {
            LOGGER.warn("mail.from parameter not informed in etbmanager.properties file");
            return;
        }

        try {
            String txt = loadMessageFromTemplate(template, model);

            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(msg, true, "UTF-8");
            message.setTo(to);
            message.setFrom(mailFrom);
            message.setSubject(subject);
            message.setText(txt, true); // true = HTML

            javaMailSender.send(msg);
        }
        catch (MailException e) {
            LOGGER.error("Error sending mail message", e);
            throw new MailServiceException(e);
        } catch (MessagingException e) {
            LOGGER.error("Error creating message", e);
            throw new MailServiceException(e);
        }
    }


    /**
     * Process the template and return the content in a string format. Mail templates are loaded
     * from the templates/mail folder
     * @param templateFile the template file name
     * @param model the data model with dynamic data to be replaced in the template and used by freemarker
     * @return the message in string format
     */
    protected String loadMessageFromTemplate(String templateFile, Map<String, Object> model) {
        try {
            Template templ = configuration.getTemplate("mail/template.html");

            Map<String, Object> data = new HashMap<>(model);
            data.put("msg", createMessageResolver());
            data.put("content", templateFile);

            return FreeMarkerTemplateUtils.processTemplateIntoString(templ, data);

        } catch (IOException e) {
            LOGGER.error("Error finding template " + templateFile, e);
            throw new MailServiceException(e);
        } catch (TemplateException e) {
            LOGGER.error("Error generating message", e);
            throw new MailServiceException(e);
        }
    }


    /**
     * Create message resolver to support i18n in e-mail messages. The return value will be
     * passed to the freemarker template page
     * @return instance of {@link MessageResolverMethod}
     */
    protected MessageResolverMethod createMessageResolver() {
        return new MessageResolverMethod(messages);
    }
}
