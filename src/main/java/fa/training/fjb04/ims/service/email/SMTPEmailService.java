package fa.training.fjb04.ims.service.email;

import fa.training.fjb04.ims.dto.request.MailComplexRequest;
import fa.training.fjb04.ims.dto.request.MailReminderRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SMTPEmailService implements EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    /**
     * @param request
     */
    @Override
    @Async
    public void sendMailWithTemplateThymeleaf(MailComplexRequest request) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());

            // Create the Thymeleaf context
            Context context = new Context();
            context.setVariable("username", request.getUsername());
            context.setVariable("email", request.getEmail());
            context.setVariable("password", request.getPassword());
            context.setVariable("owner", request.getOwnerAccount());

            String htmlContent = templateEngine.process("email/email_create_user", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("Error sending email : " + e.getMessage());
        }
    }

    /**
     * @param request to, subject.
     */
    @Override
    @Async
    public void sendMailChangEmail(MailComplexRequest request) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());

            // Create the Thymeleaf context
            Context context = new Context();
            context.setVariable("email", request.getEmail());
            context.setVariable("username", request.getUsername());

            String htmlContent = templateEngine.process("email/change_email", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("Error sending email : " + e.getMessage());
        }
    }

    @Override
    public void sendMailRemindWithTemplateThymeleaf(MailReminderRequest request) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());

            // Create the Thymeleaf context
            Context context = new Context();
            context.setVariable("candidateName", request.getCandidateName());
            context.setVariable("candidatePosition", request.getCandidatePosition());
            context.setVariable("offerDueDate", request.getOfferDueDate());
            context.setVariable("url", request.getUrl());

            String htmlContent = templateEngine.process("email/email_remind_offer", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("Error sending email : " + e.getMessage());
            throw new RuntimeException("Ex: "+ e.getMessage());
        }
    }
}
