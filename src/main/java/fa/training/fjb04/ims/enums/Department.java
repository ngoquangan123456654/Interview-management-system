package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Department {
    IT(0, "IT"),
    HR(1, "HR"),
    FINANCE(2, "Finance"),
    COMMUNICATION(3, "Communication"),
    MARKETING(4, "Marketing"),
    ACCOUNTING(5, "Accounting");
    private final int code;
    private final String name;
    public static Department fromCode(Integer code) {
        for (Department department : Department.values()) {
            if (department.code == code) {
                return department;
            }
        }
        throw new IllegalArgumentException("No constant with name " + code + " found in department enum");
    }
    public static Department fromValue(String value) {
        for (Department department : Department.values()) {
            if (department.name.equals(value)) {
                return department;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in department enum");
    }
}
