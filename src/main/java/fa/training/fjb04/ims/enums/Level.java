package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    FRESHER(0, "Fresher"),
    JUNIOR(1, "Junior"),
    SENIOR(2, "Senior"),
    LEADER(3, "Leader"),
    MANAGER(4, "Manager"),
    VICEHEAD(5, "Vice Head");

    private final int code;
    private final String name;

    public static Level fromCode(Integer code) {
        for (Level level : Level.values()) {
            if (level.code == code) {
                return level;
            }
        }
        throw new IllegalArgumentException("No constant with name " + code + " found in level enum");
    }

    public static Level fromValue(String value) {
        for (Level level : Level.values()) {
            if (level.name.equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in level enum");

    }
}