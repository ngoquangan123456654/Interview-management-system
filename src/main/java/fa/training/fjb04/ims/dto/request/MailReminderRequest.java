package fa.training.fjb04.ims.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailReminderRequest {

    private String to;
    private String subject;
    private String candidateName;
    private String candidatePosition;
    private String offerDueDate;

    private String url;
}
