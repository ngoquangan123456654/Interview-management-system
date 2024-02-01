package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
    PASSED(0, "Passed"),
    FAILED(1, "Failed"),
    NA(2,"N/A");

    private final int code;
    private final String name;

    public static Result fromCode(Integer code) {
        for (Result result : Result.values()) {
            if (result.code == code) {
                return result;
            }
        }
        throw new IllegalArgumentException("No constant with name " + code + " found in result enum");
    }

    public static Result fromValue(String value) {
        for (Result result : Result.values()) {
            if (result.name.equals(value)) {
                return result;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in result enum");

    }
}

