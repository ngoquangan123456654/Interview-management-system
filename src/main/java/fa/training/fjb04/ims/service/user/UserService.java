package fa.training.fjb04.ims.service.user;

import fa.training.fjb04.ims.dto.user.ListUserDto;
import fa.training.fjb04.ims.dto.user.UserDto;
import fa.training.fjb04.ims.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User findUserByEmail(String email);

    void updateResetPasswordToken(String token, String email, User user);

    void update(User user);

    User getByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);

    List<String> findAllRecruiter();

    User findByFullNameAndRole(String fullName);

    void saveUser(User user);

    Page<ListUserDto> getUserList(String keyword, String role, Pageable pageable);

    User findById(Integer id);

    Boolean existsByPhoneNumber(String phone);

    Boolean existsByPhoneNumberAndUserId(String phone, Integer id);

    UserDto findTopByAccount(String account);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndUserId(String email, Integer id);

    ListUserDto getUserById(Integer id);

    List<String> getAllUserNameByRole(String role);

    User findByName(String name);

    List<String> getAllNameByScheduleId(Integer id);

    List<String> userHaveUserManagerRole(String role);

    List<String> userHaveUserRecruiterRole(String role);

    User findByUserName(String name);


}
