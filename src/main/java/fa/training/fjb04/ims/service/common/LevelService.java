package fa.training.fjb04.ims.service.common;

import fa.training.fjb04.ims.entity.common.Level;

import java.util.List;

public interface LevelService {

    List<String> findAllLevels();
    Level findLevelByName (String name);
}
