package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusSchedule {
    NEW(0, "New"),
    INVITED(1, "Invited"),
    INTERVIEWED(2, "Interviewed"),
    CANCELLED(3, "Cancelled");

    private final int code;
    private final String name;

    public static StatusSchedule fromCode(Integer code) {
        for (StatusSchedule statusSchedule : StatusSchedule.values()) {
            if (statusSchedule.code == code) {
                return statusSchedule;
            }
        }
        throw new IllegalArgumentException("No constant with name " + code + " found in status schedule enum");
    }

    public static StatusSchedule fromValue(String value) {
        for (StatusSchedule statusSchedule : StatusSchedule.values()) {
            if (statusSchedule.name.equals(value)) {
                return statusSchedule;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in status schedule enum");

    }
}