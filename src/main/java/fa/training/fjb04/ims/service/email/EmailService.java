package fa.training.fjb04.ims.service.email;

import fa.training.fjb04.ims.dto.request.MailComplexRequest;
import fa.training.fjb04.ims.dto.request.MailReminderRequest;

public interface EmailService {
    void sendMailWithTemplateThymeleaf(MailComplexRequest request);
    void sendMailChangEmail(MailComplexRequest request);

    void sendMailRemindWithTemplateThymeleaf(MailReminderRequest request);
}
