package net.andresbustamante.mail.contact.services;

/**
 * @author andresbustamante
 */
public interface MailService {

    void sendMail(String subject, String content, String name, String email);
}
