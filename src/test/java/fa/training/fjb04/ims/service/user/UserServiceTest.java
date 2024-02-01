package fa.training.fjb04.ims.service.user;

import fa.training.fjb04.ims.dto.user.ListUserDto;
import fa.training.fjb04.ims.dto.user.UserDto;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.enums.Department;
import fa.training.fjb04.ims.enums.Gender;
import fa.training.fjb04.ims.enums.UserStatus;
import fa.training.fjb04.ims.repository.user.UserRepository;
import fa.training.fjb04.ims.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .userId(1)
                .roles(new HashSet<>())
                .email("nnfdjg34@gmail.com")
                .gender(Gender.MALE)
                .userName("ThanhND")
                .dateOfBirth(LocalDate.parse("2020-01-01"))
                .department(Department.IT)
                .phoneNumber("0474374774")
                .status(UserStatus.valueOf("ACTIVE"))
                .address("HN")
                .fullName("Hoang Khanh")
                .note("ok")
                .build();
    }

    @Nested
    @SpringBootTest
    class saveUser {
        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UserServiceImpl userServiceImpl;

        @Test
        public void test_save_user_success() {
            when(userRepository.save(user)).thenReturn(user);
            userServiceImpl.saveUser(user);
            verify(userRepository, times(1)).save(eq(user));
        }

        @Test
        public void test_saveUser_WhenRepositoryReturnsNull() {
            when(userRepository.save(any(User.class))).thenReturn(null);
            Exception exception = assertThrows(Exception.class, () -> userServiceImpl.saveUser(user));
            System.out.println("Exception message: " + exception.getMessage());
            verify(userRepository, times(1)).save(eq(user));
        }

        @Test
        public void test_saveUser_HandleRepositoryException() {
            when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Save failed"));
            assertThrows(RuntimeException.class, () -> userServiceImpl.saveUser(user));
            verify(userRepository, times(1)).save(eq(user));
        }
    }

    @Nested
    @SpringBootTest
    public class findAll {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;

        @Test
        public void testFindAll() {
            List<ListUserDto> userList = new ArrayList<>();
            Pageable pageable = PageRequest.of(0, 10);
            when(userRepository.getUserList(anyString(), anyString(), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(userList, pageable, userList.size()));
            Page<ListUserDto> resultPage = userServiceImpl.getUserList("someKeyword", "someRole", pageable);
            assertEquals(userList.size(), resultPage.getTotalElements());
        }

        @Test
        void testGetUserList_WithNullKeywordAndRole() {
            Pageable pageable = PageRequest.of(0, 10);
            when(userRepository.getUserList(null, null, pageable))
                    .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
            Page<ListUserDto> resultPage = userServiceImpl.getUserList(null, null, pageable);
            assertEquals(0, resultPage.getTotalElements());
        }


        @Test
        void testGetUserList_WhenNoUsersFound() {
            String keyword = "nonexistent";
            String role = "nonexistentRole";
            Pageable pageable = PageRequest.of(0, 10);
            when(userRepository.getUserList(keyword, role, pageable))
                    .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
            Page<ListUserDto> resultPage = userServiceImpl.getUserList(keyword, role, pageable);
            assertEquals(0, resultPage.getTotalElements());
        }

        @Test
        void testGetUserList_WhenUsersFound() {
            String keyword = "nnfdjg34";
            String role = "ADMIN";
            Pageable pageable = PageRequest.of(0, 10);
            List<ListUserDto> userList = new ArrayList<>();
            when(userRepository.getUserList(keyword, role, pageable))
                    .thenReturn(new PageImpl<>(userList, pageable, userList.size()));
            Page<ListUserDto> resultPage = userServiceImpl.getUserList(keyword, role, pageable);
            assertEquals(userList.size(), resultPage.getTotalElements());
        }

    }

    @Nested
    @SpringBootTest
    public class getById {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;


        @Test
        void testFindById_WhenUserExists() {
            Integer userId = 1;
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            User resultUser = userServiceImpl.findById(userId);
            assertNotNull(resultUser);
            assertEquals(user.getUserId(), resultUser.getUserId());
            assertEquals(user.getUserName(), resultUser.getUserName());
            assertEquals(user.getEmail(), resultUser.getEmail());
            assertEquals(user.getRoles(), resultUser.getRoles());
            verify(userRepository, times(1)).findById(userId);
        }

        @Test
        void testFindById_WhenUserDoesNotExist() {
            Integer userId = 2;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            User resultUser = userServiceImpl.findById(userId);
            assertNull(resultUser);
            verify(userRepository, times(1)).findById(userId);
        }
    }

    @Nested
    @SpringBootTest
    public class testPhoneNumber {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;

        @Test
        void testExistsByPhoneNumber_WhenPhoneNumberExists() {
            // Arrange
            String existingPhoneNumber = "0474374774";
            when(userRepository.existsByPhoneNumber(existingPhoneNumber)).thenReturn(true);
            boolean result = userServiceImpl.existsByPhoneNumber(existingPhoneNumber);
            assertTrue(result);
            verify(userRepository, times(1)).existsByPhoneNumber(existingPhoneNumber);
        }

        @Test
        void testExistsByPhoneNumber_WhenPhoneNumberDoesNotExist() {
            String nonExistingPhoneNumber = "0987654321";
            when(userRepository.existsByPhoneNumber(nonExistingPhoneNumber)).thenReturn(false);
            boolean result = userServiceImpl.existsByPhoneNumber(nonExistingPhoneNumber);
            assertFalse(result);
            verify(userRepository, times(1)).existsByPhoneNumber(nonExistingPhoneNumber);
        }
    }

    @Nested
    @SpringBootTest
    public class testPhoneNumberWithId {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;


        @Test
        void testExistsByPhoneNumberAndUserId_WhenPhoneNumberAndUserIdExist() {
            // Arrange
            String existingPhoneNumber = "0474374774";
            Integer existingUserId = 1;
            when(userRepository.existsByPhoneNumberAndUserId(existingPhoneNumber, existingUserId)).thenReturn(true);
            boolean result = userServiceImpl.existsByPhoneNumberAndUserId(existingPhoneNumber, existingUserId);
            assertTrue(result);
            verify(userRepository, times(1)).existsByPhoneNumberAndUserId(existingPhoneNumber, existingUserId);
        }

        @Test
        void testExistsByPhoneNumberAndUserId_WhenPhoneNumberAndUserIdDoNotExist() {
            String nonExistingPhoneNumber = "987654321";
            Integer nonExistingUserId = 2;
            when(userRepository.existsByPhoneNumberAndUserId(nonExistingPhoneNumber, nonExistingUserId)).thenReturn(false);
            boolean result = userServiceImpl.existsByPhoneNumberAndUserId(nonExistingPhoneNumber, nonExistingUserId);
            assertFalse(result);
            verify(userRepository, times(1)).existsByPhoneNumberAndUserId(nonExistingPhoneNumber, nonExistingUserId);
        }
    }


    @Nested
    @SpringBootTest
    public class existsByEmail {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;


        @Test
        void testExistsByPhoneNumberAndUserId_WhenEmailAndUserIdExist() {
            String existingEmail = "nnfdjg34@gmail.com";
            when(userRepository.existsByEmail(existingEmail)).thenReturn(true);
            boolean result = userServiceImpl.existsByEmail(existingEmail);
            assertTrue(result);
            verify(userRepository, times(1)).existsByEmail(existingEmail);
        }

        @Test
        void testExistsByPhoneNumberAndUserId_WhenEmailDoNotExist() {
            String existingEmail = "nnfdjgyyt4@gmail.com";
            when(userRepository.existsByEmail(existingEmail)).thenReturn(true);
            boolean result = userServiceImpl.existsByEmail(existingEmail);
            assertTrue(result);
            verify(userRepository, times(1)).existsByEmail(existingEmail);
        }
    }

    @Nested
    @SpringBootTest
    public class findTopByAccount {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;

        @Test
        void testFindTopByAccount_WhenUserExists() {
            // Arrange
            String existingAccount = "ThanhND";
            UserDto existingUser = new UserDto();
            existingUser.setUserName("ThanhND");

            when(userRepository.findTopByAccount(existingAccount)).thenReturn(existingUser);
            UserDto resultDto = userServiceImpl.findTopByAccount(existingAccount);
            assertNotNull(resultDto);
            assertEquals(existingUser.getUserId(), resultDto.getUserId());
            assertEquals(existingUser.getUserName(), resultDto.getUserName());
            verify(userRepository, times(1)).findTopByAccount(existingAccount);
        }
    }


    @Nested
    @SpringBootTest
    public class existsByEmailAndUserId {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;

        @Test
        void testExistsByEmailAndUserId_WhenEmailAndUserIdExist() {
            String existingEmail = "nnfdjg34@gmail.com";
            Integer existingUserId = 1;
            when(userRepository.existsByEmailAndUserId(existingEmail, existingUserId)).thenReturn(true);
            boolean result = userServiceImpl.existsByEmailAndUserId(existingEmail, existingUserId);
            assertTrue(result);
            verify(userRepository, times(1)).existsByEmailAndUserId(existingEmail, existingUserId);
        }

        @Test
        void testExistsByEmailAndUserId_WhenEmailAndUserIdDoNotExist() {
            String nonExistingEmail = "non_existing@example.com";
            Integer nonExistingUserId = 2;
            when(userRepository.existsByEmailAndUserId(nonExistingEmail, nonExistingUserId)).thenReturn(false);
            boolean result = userServiceImpl.existsByEmailAndUserId(nonExistingEmail, nonExistingUserId);
            assertFalse(result);
            verify(userRepository, times(1)).existsByEmailAndUserId(nonExistingEmail, nonExistingUserId);
        }
    }

    @Nested
    @SpringBootTest
    public class getListUerById {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;

        @Test
        void testGetUserById_WhenUserExists() {
            Integer existingUserId = 2;
            ListUserDto existingUser = new ListUserDto();
            existingUser.setUserId(2);
            when(userRepository.getUserById(existingUserId)).thenReturn(Optional.of(existingUser));
            ListUserDto resultDto = userServiceImpl.getUserById(existingUserId);
            assertNotNull(resultDto);
            assertEquals(existingUser.getUserId(), resultDto.getUserId());
            assertEquals(existingUser.getUserName(), resultDto.getUserName());
            verify(userRepository, times(1)).getUserById(existingUserId);
        }

        @Test
        void testGetUserById_WhenUserDoesNotExist() {
            Integer nonExistingUserId = 2;
            when(userRepository.getUserById(nonExistingUserId)).thenReturn(Optional.empty());
            ListUserDto resultDto = userServiceImpl.getUserById(nonExistingUserId);
            assertNull(resultDto);
            verify(userRepository, times(1)).getUserById(nonExistingUserId);
        }
    }

    @Nested
    @SpringBootTest
    public class findUserByEmail {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;

        @Test
        void testFindUserByEmail_WhenUserExists() {
            // Arrange
            String existingEmail = "john.doe@example.com";
            User existingUser = new User();
            user.setEmail("nnfdjg34@gmail.com");
            when(userRepository.findByEmail(existingEmail)).thenReturn(existingUser);
            User resultUser = userServiceImpl.findUserByEmail(existingEmail);
            assertNotNull(resultUser);
            assertEquals(existingUser.getUserId(), resultUser.getUserId());
            assertEquals(existingUser.getUserName(), resultUser.getUserName());
            verify(userRepository, times(1)).findByEmail(existingEmail);
        }

        @Test
        void testFindUserByEmail_WhenUserDoesNotExist() {
            String nonExistingEmail = "non_existing@example.com";
            when(userRepository.findByEmail(nonExistingEmail)).thenReturn(null);
            User resultUser = userServiceImpl.findUserByEmail(nonExistingEmail);
            assertNull(resultUser);
            verify(userRepository, times(1)).findByEmail(nonExistingEmail);
        }

    }

    @Nested
    @SpringBootTest
    public class updateUser {
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;

        @Test
        void testUpdateUser() {
            // Arrange
            User existingUser =User.builder()
                    .userId(1)
                    .roles(new HashSet<>())
                    .email("nnfdjg34@gmail.com")
                    .gender(Gender.MALE)
                    .userName("ThanhND")
                    .dateOfBirth(LocalDate.parse("2020-01-01"))
                    .department(Department.IT)
                    .phoneNumber("0378829975")
                    .status(UserStatus.valueOf("ACTIVE"))
                    .address("HN")
                    .fullName("Hoang Khanh")
                    .note("ok")
                    .build();

            when(userRepository.save(existingUser)).thenReturn(existingUser);
            userServiceImpl.update(existingUser);
            verify(userRepository, times(1)).save(existingUser);
        }

        @Test
        void testUpdateUser_WhenUpdateIsSuccessful() {
            // Arrange
            User existingUser =User.builder()
                    .userId(1)
                    .roles(new HashSet<>())
                    .email("thanhnd@gmail.com")
                    .gender(Gender.MALE)
                    .userName("ThanhND")
                    .dateOfBirth(LocalDate.parse("2020-01-01"))
                    .department(Department.IT)
                    .phoneNumber("0378829975")
                    .status(UserStatus.valueOf("ACTIVE"))
                    .address("HN")
                    .fullName("Hoang Khanh")
                    .note("ok")
                    .build();

            User updatedUser = User.builder()
                    .userId(1)
                    .roles(new HashSet<>())
                    .email("thanhnd@gmail.com")
                    .gender(Gender.MALE)
                    .userName("ThanhND")
                    .dateOfBirth(LocalDate.parse("2020-01-01"))
                    .department(Department.IT)
                    .phoneNumber("0378829975")
                    .status(UserStatus.valueOf("ACTIVE"))
                    .address("HN")
                    .fullName("Hoang Khanh")
                    .note("ok")
                    .build();

            when(userRepository.save(updatedUser)).thenReturn(updatedUser);
            userServiceImpl.update(updatedUser);
            verify(userRepository, times(1)).save(updatedUser);
        }
    }

    @Nested
    @SpringBootTest
    public class testSavePassword{
        @Mock
        UserRepository userRepository;

        @InjectMocks
        UserServiceImpl userServiceImpl;

        @Test
        void testUpdatePassword() {
            // Arrange
            User user = new User();
            String newPassword = "newPassword123";
            when(userRepository.save(any(User.class))).thenReturn(user);
            userServiceImpl.updatePassword(user, newPassword);
            verify(userRepository, times(1)).save(argThat(savedUser -> {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                return passwordEncoder.matches(newPassword, savedUser.getPassword());
            }));
            verify(userRepository, times(1)).save(argThat(savedUser -> savedUser.getResetPasswordToken() == null));
        }
    }
}