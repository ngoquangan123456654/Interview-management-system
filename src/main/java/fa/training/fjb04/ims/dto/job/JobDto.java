package fa.training.fjb04.ims.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobDto {

    private Integer jobId;

    @NotBlank(message = "Title can not be blank")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private BigDecimal salaryFrom;

    @NotNull(message = "Salary to can not be blank")
    private BigDecimal salaryTo;

    @NotBlank(message = "Working Address can not be blank")
    private String workingAddress;

    @NotEmpty(message = "Skills can not be blank")
    private List<String> skill = new ArrayList<>();

    @NotEmpty(message = "Benefits can not be blank")
    private List<String> benefits = new ArrayList<>();

    @NotEmpty(message = "Levels can not be blank")
    private List<String> level = new ArrayList<>();

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String description;

    private String status;

    public JobDto(Integer jobId, String title, LocalDate startDate, LocalDate endDate, List<String> skillsList, List<String> levelList, String status) {
        this.jobId = jobId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.skill = skillsList;
        this.level = levelList;
        this.status = status;
    }

}
