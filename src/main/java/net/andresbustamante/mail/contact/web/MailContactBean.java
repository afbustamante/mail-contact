package net.andresbustamante.mail.contact.web;

import net.andresbustamante.mail.contact.services.MailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author andresbustamante
 */
@ManagedBean
@RequestScoped
public class MailContactBean implements Serializable {

    @Inject
    private MailService mailService;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private String subject;

    /**
     *
     */
    private String content;

    private final transient Log log = LogFactory.getLog(MailContactBean.class);

    public MailContactBean() {}

    public void send() {
        log.info("A new message is going to be sent");
        mailService.sendMail(subject, content, name, email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
