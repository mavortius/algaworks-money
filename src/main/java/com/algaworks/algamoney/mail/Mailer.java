package com.algaworks.algamoney.mail;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;

@Component
public class Mailer {
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  public Mailer(JavaMailSender mailSender, TemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  public void send(String from, List<String> to, String subject, String template, Map<String, Object> values) {
    Context context = new Context(LocaleContextHolder.getLocale());
    values.forEach(context::setVariable);
    String message = templateEngine.process(template, context);
    send(from, to, subject, message);
  }

  public void send(String from, List<String> to, String subject, String message) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

      helper.setFrom(from);
      helper.setTo(to.toArray(new String[0]));
      helper.setSubject(subject);
      helper.setText(message, true);

      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new RuntimeException("An error occurred while attempting to send mail!", e);
    }
  }
}
