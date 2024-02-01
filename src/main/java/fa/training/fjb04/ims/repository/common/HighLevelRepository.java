package fa.training.fjb04.ims.repository.common;

import fa.training.fjb04.ims.entity.common.HighLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighLevelRepository extends JpaRepository<HighLevel, Integer> {
    @Query(value = "SELECT h.name FROM HighLevel h")
    List<String> findAllHighLevel();

    HighLevel findByName(String name);
}
