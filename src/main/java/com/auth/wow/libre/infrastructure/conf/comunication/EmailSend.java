package com.auth.wow.libre.infrastructure.conf.comunication;

import com.auth.wow.libre.domain.model.comunication.MailSenderVars;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailSend {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailSend.class);
  private static final String NEW_USER_TEMPLATE = "new-user.ftlh";
  private static final String EMAIL_DEFAULT = "mschitiva68@gmail.com";
  private final JavaMailSender mailSender;
  private final Configuration freeMakerConfiguration;

  public EmailSend(JavaMailSender mailSender, Configuration freeMakerConfiguration) {
    this.mailSender = mailSender;
    this.freeMakerConfiguration = freeMakerConfiguration;
  }

  @Async
  public void sendHTMLEmail(String emailFrom, String subject, MailSenderVars messageVars) {
    try {
      MimeMessage emailMessage = mailSender.createMimeMessage();
      String template = template();

      MimeMessageHelper mailBuilder = new MimeMessageHelper(emailMessage, true, "utf-8");
      String body = sendRegisterConfirmation(messageVars, template);

      mailBuilder.setText(body, true);
      mailBuilder.setTo(EMAIL_DEFAULT);
      mailBuilder.setFrom(emailFrom);
      mailBuilder.setSubject(subject);
      mailSender.send(emailMessage);
    } catch (Exception e) {
      LOGGER.error("It was not possible to send the communication message: [{}] Template [{}] ",
          e.getMessage(), template());
    }

  }

  private String sendRegisterConfirmation(MailSenderVars body, String template) {
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    model.put("body", body);
    buildBody(model, stringWriter, template);
    return stringWriter.getBuffer().toString();
  }

  private void buildBody(Map<String, Object> model, StringWriter stringWriter, String template) {
    try {
      freeMakerConfiguration.getTemplate(template).process(model, stringWriter);
    } catch (Exception e) {
      LOGGER.error("It was not possible to load the template: [{}] Message: [{}]", template(),
          e.getMessage());
    }
  }

  protected String template() {
    return NEW_USER_TEMPLATE;
  }

}
