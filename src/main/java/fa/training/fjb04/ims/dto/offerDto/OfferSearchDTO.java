package fa.training.fjb04.ims.dto.offerDto;

import fa.training.fjb04.ims.enums.Department;
import fa.training.fjb04.ims.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferSearchDTO {
    private Integer offerDtoId;
    private String candidateName;
    private String emailCandidate;
    private String approver;
    private String department;
    private String note;
    private String status;
    public OfferSearchDTO(Integer offerDtoId, String candidateName, String emailCandidate, String user,
                          Department department, String note, Status status) {
        this.offerDtoId = offerDtoId;
        this.candidateName = candidateName;
        this.emailCandidate = emailCandidate;
        this.approver = user;
        this.department = department.name();
        this.note = note;
        this.status = status.getName();
    }
}
