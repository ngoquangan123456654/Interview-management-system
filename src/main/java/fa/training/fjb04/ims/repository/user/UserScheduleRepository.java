package fa.training.fjb04.ims.repository.user;

import fa.training.fjb04.ims.entity.Schedule;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.entity.intermediateTable.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Integer> {

    void deleteByScheduleId (Integer id);


    @Modifying
    @Transactional
    @Query("DELETE FROM UserSchedule us WHERE us.schedule.id = :id")
    void deleteAllById( Integer id);


    Boolean existsBySchedule(Schedule schedule);

    @Query(
            "SELECT u.fullName FROM User u " +
                    "JOIN UserSchedule us ON u.userId = us.user.userId " +
                    "JOIN Schedule s ON s.id = us.schedule.id " +
                    "WHERE s.id = :id"
    )
    List<String> getAllNameByScheduleId(Integer id);

    @Query ("SELECT u.fullName FROM User u")
    List<String> getAllInterviewerName ();

    @Query("SELECT us.user FROM UserSchedule us WHERE us.schedule.id = :scheduleId")
    List<User> findByScheduleId(@Param("scheduleId") Integer scheduleId);

}
