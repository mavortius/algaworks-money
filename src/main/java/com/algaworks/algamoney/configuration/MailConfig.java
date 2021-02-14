package com.algaworks.algamoney.configuration;

import com.algaworks.algamoney.configuration.property.AlgamoneyProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

  private final AlgamoneyProperty property;

  public MailConfig(AlgamoneyProperty property) {
    this.property = property;
  }

  @Bean
  public JavaMailSender mailSender() {
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.connectiontimeout", 10000);

    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setJavaMailProperties(props);
    sender.setHost(property.getMail().getHost());
    sender.setPort(property.getMail().getPort());
    sender.setUsername(property.getMail().getUsername());
    sender.setPassword(property.getMail().getPassword());

    return sender;
  }
}
