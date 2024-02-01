package fa.training.fjb04.ims.dto.schedule;

import fa.training.fjb04.ims.entity.Candidate;
import fa.training.fjb04.ims.enums.Result;
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
public class ScheduleInfoDto {

    private Integer scheduleId;

    private String title;

    private Candidate candidate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduleDate;

    private LocalTime scheduleTimeFrom;

    private LocalTime scheduleTimeTo;

    private String note;

    private Result result;

    private String status;

    private String location;

    private String meetingId;

    private String recruiter;

    private String mainInterviewer;

    private List<String> interviewerList = new ArrayList<>();

    private String job;

    private String submit;

    public ScheduleInfoDto(Integer scheduleId, String title, Candidate candidate, String mainInterviewer, List<String> interviewerList, LocalDate scheduleDate, LocalTime scheduleTimeFrom, LocalTime scheduleTimeTo, Result result, String status, String job) {
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
