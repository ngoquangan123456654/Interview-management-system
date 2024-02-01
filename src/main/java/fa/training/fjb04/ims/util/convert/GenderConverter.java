package fa.training.fjb04.ims.util.convert;

import fa.training.fjb04.ims.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Gender, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return (gender != null) ? gender.getCode() : null;
    }

    @Override
    public Gender convertToEntityAttribute(Integer code) {
        return (code != null) ? Gender.fromCode(code) : null;
    }
}