package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.dto.user.ListUserDto;
import fa.training.fjb04.ims.dto.user.UserDto;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.repository.common.PasswordTokenRepository;
import fa.training.fjb04.ims.repository.user.UserRepository;
import fa.training.fjb04.ims.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordTokenRepository passwordTokenRepository;

    @Override
    public void saveUser(User user) {
        User savedUser = userRepository.save(user);
        if (savedUser == null) {
            throw new RuntimeException("Save operation returned null");
        }
    }

    /**
     * @param keyword  Optional string search by all field
     * @param pageable Page index = 0, page size=10
     * @return Page userDto
     */
    @Override
    public Page<ListUserDto> getUserList(String keyword, String role, Pageable pageable) {
        String search = (keyword != null) ? keyword.toLowerCase() : null;
        return userRepository.getUserList(search, role, pageable);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * @param phone String phone number
     * @return t
     */
    @Override
    public Boolean existsByPhoneNumber(String phone) {
        return userRepository.existsByPhoneNumber(phone);
    }

    /**
     * @param phone String phone
     * @param id    Integer id
     * @return true|false
     */
    @Override
    public Boolean existsByPhoneNumberAndUserId(String phone, Integer id) {
        return userRepository.existsByPhoneNumberAndUserId(phone, id);
    }

    /**
     * @param account String account
     * @return username
     */
    @Override
    public UserDto findTopByAccount(String account) {
        return userRepository.findTopByAccount(account);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    public Boolean existsByEmailAndUserId(String email, Integer id) {
        return userRepository.existsByEmailAndUserId(email, id);
    }

    @Override
    public ListUserDto getUserById(Integer id) {
        return userRepository.getUserById(id).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateResetPasswordToken(String token, String email, User user) {

        user.setResetPasswordToken(token);
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);

        userRepository.save(user);
    }

    @Override
    public List<String> findAllRecruiter() {
        return userRepository.findAllRecruiter();
    }

    @Override
    public User findByFullNameAndRole(String fullName) {
        return userRepository.findByUserNameAndRole(fullName);
    }

    @Override
    public List<String> getAllUserNameByRole(String role) {
        return userRepository.getAllUserNameByRoles(role);
    }

    ;

    @Override
    public User findByName(String name) {
        return userRepository.findByFullName(name);
    }

    @Override
    public List<String> getAllNameByScheduleId(Integer id) {
        return userRepository.getAllNameByScheduleId(id);
    }

    @Override
    public List<String> userHaveUserManagerRole(String role) {
        return userRepository.findAllUserRole(role);
    }

    @Override
    public List<String> userHaveUserRecruiterRole(String role) {
        return userRepository.findAllUserRole(role);
    }

    @Override
    public User findByUserName(String name) {
        return userRepository.findByUserName(name);
    }
}
