package fa.training.fjb04.ims.dto.candidate;

import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.enums.Position;
import fa.training.fjb04.ims.enums.Status;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDto {

    private Integer candidateId;
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of Birth must be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email address")
    private String email;
    private String address;

    @Pattern(regexp = "^(\\+84|0)(3[2-9]|5[2689]|7[06789]|8[0-9]|9[0-4])\\d{7}$", message = "Invalid Phone Number")
    private String phoneNumber;
    private String gender;
    private String position;
    private String status;

    private MultipartFile cvAttachment;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;
    private Integer yearOfExperience;
    private String highLevel;
    private List<String> skill;
    private String user;
    private String submit;
    private LocalDateTime createdDate;
    private String createdBy;

    public CandidateDto(Integer candidateId, String fullName, String email, String phoneNumber, Status status, Position position, User user) {
        this.candidateId = candidateId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status.getName();
        this.position = position.getName();
        this.user = user.getFullName();
    }


}
