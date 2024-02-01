package fa.training.fjb04.ims.util.convert;

import fa.training.fjb04.ims.enums.ContractType;
import fa.training.fjb04.ims.enums.Department;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ContractTypeConvert implements AttributeConverter<ContractType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ContractType contractType) {
        return (contractType != null) ? contractType.getCode() : null;
    }

    @Override
    public ContractType convertToEntityAttribute(Integer code) {
        return (code != null) ? ContractType.fromCode(code) : null;
    }
}
