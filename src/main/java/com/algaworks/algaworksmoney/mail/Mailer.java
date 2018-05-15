package com.algaworks.algaworksmoney.mail;

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

/*	@EventListener
	private void teste(ApplicationReadyEvent event) {
		String template = "mail/aviso-lancamentos-vencidos";
		
		List<Lancamento> lista = repo.findAll();
		
		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", lista);
		
		this.send("testes.algaworks@gmail.com",
				Arrays.asList("m.a.martins@live.com"),
				"Testando", template, variaveis);
		System.out.println("Terminado o envio de e-mail...");
	}*/

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
            helper.setTo(to.toArray(new String[to.size()]));
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("An error occurred while attempting to send mail!", e);
        }
    }
}
