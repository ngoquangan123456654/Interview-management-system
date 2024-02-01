package fa.training.fjb04.ims.service.schedule;

import fa.training.fjb04.ims.entity.User;

import java.util.List;

public interface UserScheduleSevice {
    List<User> getUserByScheduleId (Integer id);
    void deleteAllUserScheduleById(Integer id);
}
