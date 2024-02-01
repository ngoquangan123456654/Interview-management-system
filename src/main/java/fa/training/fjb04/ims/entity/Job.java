package fa.training.fjb04.ims.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.fjb04.ims.entity.intermediateTable.JobBenefit;
import fa.training.fjb04.ims.entity.intermediateTable.JobLevel;
import fa.training.fjb04.ims.entity.intermediateTable.JobSkill;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Table
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Integer id;

    @Column(name = "job_title",unique = true)
    @NotBlank(message = "title can not be empty")
    private String title;

    @Column(name = "start_date")
    @NotNull(message = "StartDate can not be empty")
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull(message = "EndDate can not be empty")
    private LocalDate endDate;

    @Column(name = "salary_from")
    private BigDecimal salaryFrom;

    @Column(name = "salary_to")
    @NotNull(message = "SalaryTo can not be empty")
    private BigDecimal salaryTo;

    @Column(name = "working_address")
    @NotBlank(message = "WorkingAddress can not be empty")
    private String workingAddress;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<JobBenefit> jobBenefitList = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<JobLevel> jobLevelList = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<JobSkill> jobSkillList = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<Schedule> scheduleList = new ArrayList<>();


}
