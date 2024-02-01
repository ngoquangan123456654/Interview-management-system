package fa.training.fjb04.ims.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailComplexRequest {
    private String to;
    private String subject;
    private String username;
    private String email;
    private String password;
    private String ownerAccount;
}
