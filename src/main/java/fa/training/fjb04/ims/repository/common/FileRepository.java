package fa.training.fjb04.ims.repository.common;

import fa.training.fjb04.ims.entity.common.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
}
