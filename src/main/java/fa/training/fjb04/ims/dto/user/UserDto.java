package fa.training.fjb04.ims.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Integer userId;

    @NotBlank(message = "{user.error.fullName}")
    private String fullName;

    @Past(message = "{user.error.dateOfBirthPast}")
    @NotNull(message = "{user.error.dateOfBirth}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(\\+84|0)(3[2-9]|5[2689]|7[06789]|8[0-9]|9[0-4])\\d{7}$", message = "{user.error.phoneMaxLength}")
    private String phoneNumber;

    @NotNull(message = "{user.error.role}")
    private Set<String> role ;

    @NotNull(message = "{user.error.status}")
    private String status;

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{user.error.emailFormat}")
    private String email;

    @NotBlank(message = "{user.error.address}")
    private String address;

    @NotBlank(message = "{user.error.gender}")
    private String gender;

    @NotBlank(message = "{user.error.department}")
    private String department;

    private String note;

    private String userName;

    public UserDto(String userName) {
        this.userName = userName;
    }

}
