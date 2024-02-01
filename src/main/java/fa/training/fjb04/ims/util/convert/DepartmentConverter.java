package fa.training.fjb04.ims.util.convert;

import fa.training.fjb04.ims.enums.Department;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DepartmentConverter implements AttributeConverter<Department, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Department department) {
        return (department != null) ? department.getCode() : null;
    }

    @Override
    public Department convertToEntityAttribute(Integer code) {
        return (code != null) ? Department.fromCode(code) : null;
    }
}