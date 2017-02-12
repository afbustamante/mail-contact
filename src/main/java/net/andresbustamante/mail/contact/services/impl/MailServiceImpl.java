package net.andresbustamante.mail.contact.services.impl;

import net.andresbustamante.mail.contact.services.MailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Named;

/**
 * @author andresbustamante
 */
@Named("mailService")
public class MailServiceImpl implements MailService {

    private final transient Log log = LogFactory.getLog(MailServiceImpl.class);

    @Override
    public void sendMail(String subject, String content, String name, String email) {
        log.info("Sending message with subject " + subject);
    }
}
