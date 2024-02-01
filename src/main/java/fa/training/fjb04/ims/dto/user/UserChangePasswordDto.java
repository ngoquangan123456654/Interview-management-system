package fa.training.fjb04.ims.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDto {
    @NotBlank(message = "Required field")
    private String oldPassword;

    @NotBlank(message = "Required field")
    private String newPassword;

    @NotBlank(message = "Required field")
    private String confirmNewPassword;
}
