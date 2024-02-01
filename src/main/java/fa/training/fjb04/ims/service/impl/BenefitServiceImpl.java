package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.repository.common.BenefitRepository;
import fa.training.fjb04.ims.service.common.BenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private final BenefitRepository benefitRepository;

    @Override
    public List<String> findAllBenefit() {
        return benefitRepository.findAllBenefits();
    }
}
