package fa.training.fjb04.ims.util.convert;

import fa.training.fjb04.ims.enums.Level;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LevelConverter implements AttributeConverter<Level, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Level level) {
        return (level != null) ? level.getCode() : null;
    }

    @Override
    public Level convertToEntityAttribute(Integer code) {
        return (code != null) ? Level.fromCode(code) : null;
    }
}
