package net.andresbustamante.mail.contact.services;

import javax.mail.MessagingException;

/**
 * Mail composition and sending service
 *
 * @author andresbustamante
 */
public interface MailService {

    /**
     * Send a message to the email destination set up by default using contact information such as the name and the
     * email of the person writing the message as well as the subject and the content of the message
     *
     * @param subject Message's subject
     * @param content Message's content in plain text
     * @param name    Name of the author of the message
     * @param email   Email address of the author of the message
     * @throws MessagingException
     */
    void sendMail(String subject, String content, String name, String email) throws MessagingException;
}
