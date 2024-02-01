package fa.training.fjb04.ims.dto.offerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {

    private Integer offerDtoId;
    private String candidateName;
    private String emailCandidate;
    private String approver;
    private String department;
    private String note;
    private String status;

    private String position;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractPeriodFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractPeriodTo;

    private String interviewNote;
    private String contractType;
    private String level;
    private String recuiterOwner;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private String basicSalary;

    public OfferDTO(Integer offerDtoId, String candidateName, String emailCandidate, String approver,
                    String department, String note, String status){
        this.offerDtoId = offerDtoId;
        this.candidateName = candidateName;
        this.emailCandidate = emailCandidate;
        this.approver = approver;
        this.department = department;
        this.note = note;
        this.status = status;
    }

}
