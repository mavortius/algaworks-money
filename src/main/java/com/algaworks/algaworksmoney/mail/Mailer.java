package com.algaworks.algaworksmoney.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Component
public class Mailer {

    private final JavaMailSender mailSender;

    public Mailer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

   /* @EventListener
	private void teste(ApplicationReadyEvent event) {
		this.send("testes.algaworks@gmail.com",
				Arrays.asList("martinsmalmeida@gmail.com"),
				"Testando", "Olá!<br/>Teste ok.");
		System.out.println("Terminado o envio de e-mail...");
	}*/

    public void send(String from, List<String> to, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to.toArray(new String[to.size()]));
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("An error occurred while attempting to send mail!", e);
        }
    }
}
