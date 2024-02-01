package fa.training.fjb04.ims.repository.user;

import fa.training.fjb04.ims.dto.user.ListUserDto;
import fa.training.fjb04.ims.dto.user.UserDto;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserNameIgnoreCaseAndStatus(String username, UserStatus status);

    User findByEmail(String email);

    Boolean existsByPhoneNumber(String phone);

    Boolean existsByPhoneNumberAndUserId(String phone, Integer id);

    User findByEmailAndPassword(String email, String password);

    User findByResetPasswordToken(String token);

    @Query(value = "SELECT u.userName FROM User u " +
            "JOIN u.roles r " +
            "WHERE r.roleName = 'RECRUITER' ")
    List<String> findAllRecruiter();

    @Query(value = "SELECT u FROM User u " +
            "JOIN u.roles r " +
            "WHERE u.userName = :fullName AND r.roleName = 'RECRUITER' ")
    User findByUserNameAndRole(@Param("fullName") String fullName);

    @Query("SELECT NEW fa.training.fjb04.ims.dto.user.UserDto(u.userName)  FROM User u WHERE u.userName LIKE  CONCAT(:accountPattern,'%') ORDER BY u.userName DESC LIMIT 1")
    UserDto findTopByAccount(String accountPattern);

    @Query("""
             SELECT NEW fa.training.fjb04.ims.dto.user.ListUserDto(u.userId, u.userName,u.email, u.phoneNumber,u.status, r.roleName) FROM User u JOIN u.roles r
                     WHERE 
                        (:keyword IS NULL OR 
                            LOWER(u.userName) LIKE CONCAT('%', :keyword, '%') OR 
                            LOWER(u.email) LIKE CONCAT('%', :keyword, '%') OR 
                            u.phoneNumber LIKE CONCAT('%', :keyword, '%') OR 
                            LOWER(u.status) LIKE CONCAT('%', :keyword, '%')
                        ) 
                       AND(:role IS NULL OR r.roleName = :role)  ORDER BY
                        CASE u.status
                            WHEN fa.training.fjb04.ims.enums.UserStatus.ACTIVE THEN 1
                            WHEN fa.training.fjb04.ims.enums.UserStatus.INACTIVE THEN 2
                            ELSE 10
                            END ASC,
                        u.createdDate DESC
            """
    )
    Page<ListUserDto> getUserList(String keyword, String role, Pageable pageable);

    @Query("""
            SELECT NEW fa.training.fjb04.ims.dto.user.ListUserDto(u.userId, u.phoneNumber, u.status, u.email, u.userName,r.roleName, u.fullName, u.dateOfBirth,u.address, u.gender, u.department,u.note)
             FROM User  u JOIN u.roles r
            WHERE u.userId = :id
            """
    )
    Optional<ListUserDto> getUserById(Integer id);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndUserId(String email, Integer id);

    @Query(value = "SELECT u.userName FROM User u " +
            "JOIN u.roles r " +
            "WHERE r.roleName = 'INTERVIEWER' ")
    List<String> findAllInterviewer();


    @Query(value = "select u from User u where u.userName = :userName")
    User findByUserName(@NotBlank(message = "UserName can not be empty") String userName);

    @Query(
            "SELECT u.userName FROM User u " +
                    "JOIN UserSchedule us ON u.userId = us.user.userId " +
                    "JOIN Schedule s ON s.id = us.schedule.id " +
                    "WHERE s.id = :id"
    )
    List<String> getAllNameByScheduleId(Integer id);

    @Query(
            "SELECT u.userName FROM User u " +
                    "JOIN u.roles r " +
                    "WHERE r.roleName = :role " +
                    "ORDER BY u.fullName")
    List<String> getAllUserNameByRoles(String role);

    User findByFullName(String name);

    @Query(value = "SELECT u.userName FROM User u " +
            "JOIN u.roles r " +
            "WHERE r.roleName = :userRole ")
    List<String> findAllUserRole(@Param("userRole") String userRole);

}
