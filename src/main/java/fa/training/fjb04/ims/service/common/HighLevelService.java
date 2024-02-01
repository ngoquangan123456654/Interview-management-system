package fa.training.fjb04.ims.service.common;

import fa.training.fjb04.ims.entity.common.HighLevel;

import java.util.List;

public interface HighLevelService {

    HighLevel findByName(String name);

    List<String> findAllHighLevel();
}
