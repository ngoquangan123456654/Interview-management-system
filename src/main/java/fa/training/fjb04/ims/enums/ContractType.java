package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractType {
    TRIAL_TWO_MONTHS(0, "Trial 2 months"),
    TRAINEE_THREE_MONTHS(1, "Trainee 3 months"),
    ONE_YEAR(2, "1 year"),
    THREE_YEARS_AND_UNLIMITED(3, "3 years and Unlimited");

    private final int code;
    private final String name;

    public static ContractType fromCode(Integer code) {
        for (ContractType contractType : ContractType.values()) {
            if (contractType.code == code) {
                return contractType;
            }
        }
        throw new IllegalArgumentException("No constant with name " + code + " found in contract type enum");
    }

    public static ContractType fromValue(String value) {
        for (ContractType contractType : ContractType.values()) {
            if (contractType.name.equals(value)) {
                return contractType;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in contract type enum");

    }
}

