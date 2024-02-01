package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.repository.user.UserScheduleRepository;
import fa.training.fjb04.ims.service.schedule.UserScheduleSevice;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class UserScheduleServiceImpl implements UserScheduleSevice {
    private final UserScheduleRepository userScheduleRepository;
    @Override
    public List<User> getUserByScheduleId(Integer id) {
        return userScheduleRepository.findByScheduleId(id);
    }

    @Override
    public void deleteAllUserScheduleById(Integer id) {
        userScheduleRepository.deleteAllById(id);
    }
}
