package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.entity.common.Level;
import fa.training.fjb04.ims.repository.common.LevelRepository;
import fa.training.fjb04.ims.service.common.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository levelRepository;

    @Override
    public List<String> findAllLevels() {
        return levelRepository.findAllLevels();
    }
    @Override
    public Level findLevelByName(String name) {
        return levelRepository.findByName(name);
    }
}
