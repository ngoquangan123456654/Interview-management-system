package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.entity.common.HighLevel;
import fa.training.fjb04.ims.repository.common.HighLevelRepository;
import fa.training.fjb04.ims.service.common.HighLevelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HighLevelServiceImpl implements HighLevelService {

    private final HighLevelRepository highLevelRepository;

    @Override
    public HighLevel findByName(String name) {
        return highLevelRepository.findByName(name);
    }

    @Override
    public List<String> findAllHighLevel() {
        return highLevelRepository.findAllHighLevel();
    }
}
