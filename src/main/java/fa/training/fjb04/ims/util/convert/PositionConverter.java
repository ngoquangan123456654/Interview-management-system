package fa.training.fjb04.ims.util.convert;

import fa.training.fjb04.ims.enums.Position;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PositionConverter implements AttributeConverter<Position, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Position position) {
        return (position != null) ? position.getCode() : null;
    }

    @Override
    public Position convertToEntityAttribute(Integer code) {
        return (code != null) ? Position.fromCode(code) : null;
    }
}