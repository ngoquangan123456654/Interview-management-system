package fa.training.fjb04.ims.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.fjb04.ims.entity.intermediateTable.JobSchedule;
import fa.training.fjb04.ims.entity.intermediateTable.UserSchedule;
import fa.training.fjb04.ims.enums.Result;
import fa.training.fjb04.ims.enums.StatusSchedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Table
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title can not be empty")
    private String title;

    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @Column(name = "schedule_time_from")
    private LocalTime scheduleTimeFrom;

    @Column(name = "schedule_time_to")
    private LocalTime scheduleTimeTo;

    @Column(name = "note", length = 500)
    @NotBlank(message = "Note can not be empty")
    private String note;

    @Column(name = "location")
    @NotBlank(message = "Location can not be empty")
    private String location;

    @Column(name = "meeting_id")
    @NotBlank(message = "Meeting id can not be empty")
    private String meetingId;

    @ManyToOne
    @JoinColumn(name = "recruiter_owner")
    @JsonIgnore
    private User recruiter;

    @ManyToOne
    @JoinColumn(name = "main_interviewer")
    @JsonIgnore
    private User mainInterviewer;

    @OneToMany(mappedBy = "schedule")
    @JsonIgnore
    private List<UserSchedule> userScheduleList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonIgnore
    private Job job;

    @OneToOne(mappedBy = "schedule")
    private Offer offer;

    @Column(name = "result")
    private Result result;

    @Column(name = "status")
    private StatusSchedule statusSchedule;
}