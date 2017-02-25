package net.andresbustamante.mail.contact.web;

import net.andresbustamante.mail.contact.services.MailService;
import net.andresbustamante.mail.contact.util.ConfigProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonReader;
import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

    /**
     *
     * @return
     */
    public String send() {
        FacesMessage response;
        String captchaResponse = ctx.getExternalContext().getRequestParameterMap().get("g-recaptcha-response");

        if (isCaptchaValid(captchaResponse)) {
            sendMessage();
        } else {
            response = new FacesMessage(FacesMessage.SEVERITY_WARN, messages.getString("email.not.sent"),
                    messages.getString("email.not.sent"));
            ctx.addMessage(null, response);
        }
        return "result";
    }

    /**
     *
     */
    private void sendMessage() {
        FacesMessage response;
        log.info("A new message is going to be sent");

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
    }

    /**
     *
     * @param captchaResponse
     * @return
     */
    private boolean isCaptchaValid(String captchaResponse) {
        if (captchaResponse != null && !captchaResponse.isEmpty()) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(ConfigProperties.getValue("recaptcha.verify.url"));
                List<NameValuePair> parameters = new ArrayList<>();
                parameters.add(new BasicNameValuePair("secret", ConfigProperties.getValue("recaptcha.secret.key")));
                parameters.add(new BasicNameValuePair("response", captchaResponse));
                httpPost.setEntity(new UrlEncodedFormEntity(parameters));

                if (isCaptchaValid(httpClient, httpPost)) return true;
            } catch (IOException e) {
                log.error("An error occurred when validating captcha input.", e);
            }
        }
        return false;
    }

    /**
     *
     * @param httpClient
     * @param httpPost
     * @return
     * @throws IOException
     */
    private boolean isCaptchaValid(CloseableHttpClient httpClient, HttpPost httpPost) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            log.info("Captcha validation status : " + response.getStatusLine());

            HttpEntity captchaEntity = response.getEntity();

            if (captchaEntity != null && captchaEntity.getContent() != null) {
                Boolean verified = isCaptchaValid(captchaEntity);
                EntityUtils.consume(captchaEntity);

                log.info("Validated by captcha ? " + verified);
                return verified;
            }
        }
        return false;
    }

    /**
     *
     * @param captchaEntity
     * @return
     * @throws IOException
     */
    private boolean isCaptchaValid(HttpEntity captchaEntity) throws IOException {
        StringBuilder response = new StringBuilder();

        // Read the response from captcha server
        try (BufferedReader in = new BufferedReader(new InputStreamReader(captchaEntity.getContent()))) {
            String input;

            while ((input = in.readLine()) != null) {
                response.append(input);
            }
        }

        // Read the Json object with the validation result
        try (JsonReader reader = Json.createReader(new StringReader(response.toString()))) {
            return reader.readObject().getBoolean(ConfigProperties.getValue("recaptcha.verify.property"));
        }
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
