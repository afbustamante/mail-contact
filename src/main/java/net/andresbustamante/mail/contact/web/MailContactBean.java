package net.andresbustamante.mail.contact.web;

import net.andresbustamante.mail.contact.services.MailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.MessagingException;
import java.io.Serializable;
import java.util.ResourceBundle;

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

    private transient FacesContext ctx;

    private transient ResourceBundle messages;

    private final transient Log log = LogFactory.getLog(MailContactBean.class);

    public MailContactBean() {
        ctx = FacesContext.getCurrentInstance();
        messages = ctx.getApplication().getResourceBundle(ctx, "messages");
    }

    public String send() {
        log.info("A new message is going to be sent");
        FacesMessage response;

        try {
            mailService.sendMail(subject, content, name, email);

            response = new FacesMessage(FacesMessage.SEVERITY_INFO, messages.getString("email.sent"),
                    messages.getString("email.sent.detail"));
            ctx.addMessage(null, response);
        } catch (MessagingException e) {
            log.error("Error while sending message", e);
            response = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages.getString("email.not.sent"),
                    e.getMessage());
            ctx.addMessage(null, response);
        }
        return "result";
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
