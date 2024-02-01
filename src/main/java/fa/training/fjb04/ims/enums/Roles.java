package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {
    ADMIN(0, "Admin"),
    RECRUITER(1, "Recruiter"),
    INTERVIEWER(2, "Interviewer"),
    MANAGER(3, "Manager");

    private final int code;
    private final String name;

    public static Roles fromCode(Integer code) {
        for (Roles roles : Roles.values()) {
            if (roles.code == code) {
                return roles;
            }
        }
        throw new IllegalArgumentException("No constant with name " + code + " found in roles enum");
    }

    public static Roles fromValue(String value) {
        for (Roles roles : Roles.values()) {
            if (roles.name.equals(value)) {
                return roles;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in roles enum");
    }
}
