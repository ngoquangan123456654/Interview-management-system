package fa.training.fjb04.ims.repository.schedule;

import fa.training.fjb04.ims.entity.intermediateTable.JobSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JobScheduleRepository extends JpaRepository<JobSchedule, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM JobSchedule js WHERE js.schedule.id = :id")
    void deleteAllById( Integer id);
}
