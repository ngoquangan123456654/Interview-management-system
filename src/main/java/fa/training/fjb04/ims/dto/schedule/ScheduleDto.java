package fa.training.fjb04.ims.dto.schedule;

import fa.training.fjb04.ims.entity.Candidate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ScheduleDto {

    private Integer scheduleId;

    @NotBlank(message = "Title can not be empty")
    private String title;

//    @NotBlank(message = "Candidate name can not be empty")

    private Candidate candidate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Schedule date can not be empty")
    private LocalDate scheduleDate;

    @NotNull(message = "Time can not be empty")
    private LocalTime scheduleTimeFrom;

    @NotNull(message = "Time can not be empty")
    private LocalTime scheduleTimeTo;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;

    private String result;

    private String status;

    @NotBlank(message = "Location can not be empty")
    private String location;

    private String meetingId;

    @NotBlank(message = "Recruiter can not be empty")
    private String recruiter;

    //    @NotBlank(message = "Main interviewer can not be empty")
    private String mainInterviewer;

    private List<String> interviewerList = new ArrayList<>();

    @NotBlank(message = "Job can not be empty")
    private String job;


    public ScheduleDto(Integer scheduleId, String title, Candidate candidate, String mainInterviewer, List<String> interviewerList, LocalDate scheduleDate, LocalTime scheduleTimeFrom, LocalTime scheduleTimeTo, String result, String status, String job) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.candidate = candidate;
        this.scheduleDate = scheduleDate;
        this.scheduleTimeFrom = scheduleTimeFrom;
        this.scheduleTimeTo = scheduleTimeTo;
        this.result = result;
        this.status = status;
        this.mainInterviewer = mainInterviewer;
        this.interviewerList = interviewerList;
        this.job = job;
    }
}
