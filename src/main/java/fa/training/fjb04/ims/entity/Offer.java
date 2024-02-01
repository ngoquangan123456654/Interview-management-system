package fa.training.fjb04.ims.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.fjb04.ims.entity.common.Level;
import fa.training.fjb04.ims.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "department")
    @NotNull(message = "Department can not be empty")
    private Department department;

    @Column(name = "status")
    @NotNull(message = "Status can not be empty")
    private Status status;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "current_position", nullable = false)
    @NotNull(message = "Position can not be empty")
    private Position position;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "contract_period_to")
    @NotNull(message = "Contract Period To can not be empty")
    private LocalDate contractPeriodTo;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "contract_period_from")
    @NotNull(message = "Contract Period From can not be empty")
    private LocalDate contractPeriodFrom;

    @Column(name = "contract_type")
    @NotNull(message = "Contract type can not be empty")
    private ContractType contractType;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "due_date")
    @NotNull(message = "Due date can not be empty")
    private LocalDate dueDate;

    @Column(name = "basic_salary")
    @NotNull(message = "Basic salary can not be empty")
    private BigDecimal basicSalary;

    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @OneToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}


