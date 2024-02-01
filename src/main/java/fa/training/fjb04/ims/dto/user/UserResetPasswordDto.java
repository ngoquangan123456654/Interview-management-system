package fa.training.fjb04.ims.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResetPasswordDto {

    @NotBlank(message = "Required field")
    private String password;

    @NotBlank (message = "Required field")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}$", message = "Password must contain at least one number, one numeral, and seven characters.")
    private String rePassword;
}
