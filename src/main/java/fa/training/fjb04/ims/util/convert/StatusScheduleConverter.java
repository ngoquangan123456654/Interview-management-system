package fa.training.fjb04.ims.util.convert;

import fa.training.fjb04.ims.enums.Status;
import fa.training.fjb04.ims.enums.StatusSchedule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusScheduleConverter implements AttributeConverter<StatusSchedule, Integer> {
    @Override
    public Integer convertToDatabaseColumn(StatusSchedule status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public StatusSchedule convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return StatusSchedule.fromCode(code);
    }
}