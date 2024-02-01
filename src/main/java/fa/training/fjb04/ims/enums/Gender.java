package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE(0, "Female"),
    MALE(1, "Male"),
    OTHERS(2, "Others");

    private final int code;
    private final String name;

    public static Gender fromCode(Integer code) {
        for (Gender gender : Gender.values()) {
            if (gender.code == code) {
                return gender;
            }
        }
        throw new IllegalArgumentException("No constant with code " + code + " found in gender enum");
    }

    public static Gender fromValue(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.name.equals(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in gender enum");

    }

}