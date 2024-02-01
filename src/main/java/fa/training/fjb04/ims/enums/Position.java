package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
    BACKEND_DEVELOPER(0, "Backend Developer"),
    BUSINESS_ANALYST(1, "Business Analyst"),
    TESTER(2, "Tester"),
    HR(3, "HR"),
    PROJECT_MANAGER(4, "Project Manager"),
    NOT_AVAILABLE(5, "Not Available");

    private final int code;
    private final String name;

    public static Position fromCode(Integer code) {
        for (Position position : Position.values()) {
            if (position.code == code) {
                return position;
            }
        }
        throw new IllegalArgumentException("No constant with name " + code + " found in position enum");
    }

    public static Position fromValue(String value) {
        for (Position position : Position.values()) {
            if (position.name.equals(value)) {
                return position;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in position enum");

    }
}
