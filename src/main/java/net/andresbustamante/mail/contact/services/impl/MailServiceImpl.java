package net.andresbustamante.mail.contact.services.impl;

import net.andresbustamante.mail.contact.services.MailService;
import net.andresbustamante.mail.contact.util.ConfigProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author andresbustamante
 */
@Named("mailService")
public class MailServiceImpl implements MailService {

    private Session mailSession;

    private final transient Log log = LogFactory.getLog(MailServiceImpl.class);

    @Override
    public void sendMail(String subject, String content, String name, String email) throws MessagingException {
        log.info("Sending message with subject " + subject);

        try {
            Message message = new MimeMessage(mailSession);
            Address[] contactAddress = {new InternetAddress(email, name)};
            Address[] toAddress = {new InternetAddress(ConfigProperties.getValue("mail.recipient.address"))};

            message.setFrom(contactAddress[0]);
            message.setReplyTo(contactAddress);
            message.setRecipients(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(content);

            Transport.send(message);
            log.info("Message sent");
        } catch (UnsupportedEncodingException e) {
            log.error("Error of encoding while creating message to send", e);
        }
    }

    @PostConstruct
    private void loadMailSession() {
        try {
            InitialContext ctx = new InitialContext();
            mailSession = (Session) ctx.lookup(ConfigProperties.getValue("mail.jndi.session"));
        } catch (NamingException e) {
            log.error("Error while finding JNDI JavaMail session", e);
        }
    }
}
