package fa.training.fjb04.ims.controller.user;

import fa.training.fjb04.ims.config.security.Users;
import fa.training.fjb04.ims.dto.user.ListUserDto;
import fa.training.fjb04.ims.dto.user.UserDto;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.entity.common.Roles;
import fa.training.fjb04.ims.enums.Department;
import fa.training.fjb04.ims.enums.Gender;
import fa.training.fjb04.ims.enums.UserStatus;
import fa.training.fjb04.ims.repository.common.RoleRepository;
import fa.training.fjb04.ims.repository.user.UserRepository;
import fa.training.fjb04.ims.service.common.RoleService;
import fa.training.fjb04.ims.service.email.EmailService;
import fa.training.fjb04.ims.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private WebApplicationContext applicationContext;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @Autowired
    private UserController userController;

    private UserDetails userDetails;

    private MockMvc mockMvc;

    private void buildMockMvc(final Object controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Mock
    private BindingResult bindingResult;

    @Nested
    @DisplayName("User has role AMDIN")
    public class Authenticate {
        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                    .apply(springSecurity()).build();
            User user = new User();
            user.setUserName("duongduc");
            user.setPassword("11111");
            Roles roles = new Roles();
            roles.setId(1);
            roles.setRoleName("ADMIN");
            user.setRoles(Set.of(roles));
            user.setDepartment(Department.HR);

            when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                    .thenReturn(Optional.of(user));

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

            userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

        }

        @Test
        @DisplayName("TC01 - Test user have role ADMIN")
        void showUserList() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/user").with(user(userDetails)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }


    @Nested
    @DisplayName("User doesn't role ADMIN")
    public class unAuthenticate {
        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                    .apply(springSecurity()).build();
            User user = new User();
            user.setUserName("duongduc1");
            user.setPassword("22222");
            Roles roles = new Roles();
            roles.setId(1);
            roles.setRoleName("INTERVIEWER");
            user.setRoles(Set.of(roles));
            user.setDepartment(Department.HR);

            when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                    .thenReturn(Optional.of(user));

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

            userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

        }

        @Test
        @DisplayName("TC02 - Test user have role INTERVIEWER return 403")
        void showUserList() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/user").with(user(userDetails)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }


    @Nested
    @DisplayName("Display form create user")
    public class createUser {
        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                    .apply(springSecurity()).build();
            User user = new User();
            user.setUserName("duongduc");
            user.setPassword("11111");
            Roles roles = new Roles();
            roles.setId(1);
            roles.setRoleName("ADMIN");
            user.setRoles(Set.of(roles));
            user.setDepartment(Department.HR);

            when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                    .thenReturn(Optional.of(user));

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

            userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

        }

        @Test
        public void testGetUserCreate() throws Exception {
            List<Roles> roles = List.of(new Roles());
            when(roleService.findAll()).thenReturn(roles);

            UserDto userDto = new UserDto();
            mockMvc.perform(MockMvcRequestBuilders.get("/user/show-form-create")
                            .with(user(userDetails)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("createUser", userDto))
                    .andExpect(view().name("users/add_user"));

        }
    }


    @Nested
    @DisplayName("Create user success")
    public class createUserSuccess {
        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                    .apply(springSecurity()).build();
            User user = new User();
            user.setUserName("duongduc");
            user.setPassword("11111");
            Roles roles = new Roles();
            roles.setId(1);
            roles.setRoleName("ADMIN");
            user.setRoles(Set.of(roles));
            user.setDepartment(Department.HR);

            when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                    .thenReturn(Optional.of(user));

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

            userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

        }

        @Test
        public void testAddUserSuccess() throws Exception {
            Roles roles = new Roles();
            roles.setRoleName("ADMIN");
            Mockito.when(roleService.findByRoleName("ADMIN")).thenReturn(roles);

            mockMvc.perform(
                            post("/user/create-user")
                                    .param("fullName", "Nguyen Van An")
                                    .param("dateOfBirth", "2000-06-07")
                                    .param("phoneNumber", "0378828875")
                                    .param("status", "ACTIVE")
                                    .param("email", "abc123re@gmail.com")
                                    .param("address", "HN")
                                    .param("gender", "MALE")
                                    .param("department", "IT")
                                    .param("role", roles.getRoleName())
                                    .with(user(userDetails)))
                    .andExpect(status().is3xxRedirection());


            verify(userService).existsByEmail("abc123re@gmail.com");
            verify(userService).saveUser(any(User.class));
        }


        @Test
        void testCreateFailedBindingResultHasError() throws Exception {
            Roles roles = new Roles();
            roles.setRoleName("RECRUITER");
            when(roleService.findByRoleName("RECRUITER")).thenReturn(roles);
            mockMvc.perform(
                            post("/user/create-user")
                                    .param("fullName", "Nguyen Van B")
                                    .param("dateOfBirth", "200-02-02")
                                    .param("address", "HN")
                                    .param("phoneNumber", "0372298765")
                                    .param("gender", "MALE")
                                    .param("status", "ACTIVE")
                                    .param("note", "")
                                    .param("department", "1")
                                    .param("role", roles.getRoleName())
                                    .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                    ).andDo(print())
                    .andExpect(view().name("users/add_user"));
        }

        @Nested
        public class testUserDoNotHaveAccess {
            @BeforeEach
            void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                        .apply(springSecurity()).build();
                User user = new User();
                user.setUserName("duongduc3");
                user.setPassword("11111");
                Roles roles = new Roles();
                roles.setId(1);
                roles.setRoleName("INTERVIEWER");
                user.setRoles(Set.of(roles));
                user.setDepartment(Department.HR);

                when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                        .thenReturn(Optional.of(user));

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

                userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

            }

            @Test
            public void testAddUserFail() throws Exception {
                Roles roles = new Roles();
                roles.setRoleName("ADMIN");
                Mockito.when(roleService.findByRoleName("ADMIN")).thenReturn(roles);
                when(userService.existsByEmail("abc123re@gmail.com")).thenReturn(true);

                mockMvc.perform(
                                post("/user/create-user")
                                        .param("fullName", "Nguyen Van An")
                                        .param("dateOfBirth", "2000-06-07")
                                        .param("phoneNumber", "0378828875")
                                        .param("status", "ACTIVE")
                                        .param("email", "abc123re@gmail.com")
                                        .param("address", "HN")
                                        .param("gender", "MALE")
                                        .param("department", "IT")
                                        .param("role", roles.getRoleName())
                                        .with(user(userDetails)))
                        .andExpect(status().is4xxClientError());
            }
        }
    }


    @Nested
    @DisplayName("Detail user ")
    public class viewUserDetail {
        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                    .apply(springSecurity()).build();
            User user = new User();
            user.setUserName("duongduc");
            user.setPassword("11111");
            Roles roles = new Roles();
            roles.setId(1);
            roles.setRoleName("ADMIN");
            user.setRoles(Set.of(roles));
            user.setDepartment(Department.HR);

            when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                    .thenReturn(Optional.of(user));

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

            userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

        }

        @Test
        public void testReturnViewPage() throws Exception {
            Roles roles = new Roles();
            roles.setRoleName("ADMIN");

            ListUserDto user = new ListUserDto();
            user.setUserId(1);
            user.setFullName("Nguyen Dinh Thanh");
            user.setStatus(String.valueOf(UserStatus.ACTIVE));
            user.setGender(String.valueOf(Gender.FEMALE));
            user.setEmail("tn255508@gmail.com");
            user.setUserName("Thanh");
            user.setPhoneNumber("0378829976");
            user.setDateOfBirth(LocalDate.of(2000, 12, 12));
            user.setRole(Set.of(roles).toString());
            user.setDepartment(String.valueOf(Department.IT));

            when(userService.getUserById(1)).thenReturn(Optional.of(user).orElse(null));

            mockMvc.perform(get("/user/view/1").with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("users/detail_user"));

        }


        @Test
        public void testReturnViewNotFound() throws Exception {
            ListUserDto user = new ListUserDto();
            user.setUserId(1);
            user.setRole("ADMIN");

            when(userService.getUserById(20)).thenReturn(null);

            mockMvc.perform(get("/user/view/20").with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                    .andDo(print())
                    .andExpect(view().name("error/404"));
        }
    }

    @Nested
    @DisplayName("Edit User")
    public class testEditUser {

        @Nested
        public class TestDoNotHavePermissionToEditUser {
            @BeforeEach
            void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                        .apply(springSecurity()).build();
                User user = new User();
                user.setUserName("duongduc");
                user.setPassword("11111");
                Roles roles = new Roles();
                roles.setId(1);
                roles.setRoleName("INTERVIEWER");
                user.setRoles(Set.of(roles));
                user.setDepartment(Department.HR);

                when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                        .thenReturn(Optional.of(user));

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

                userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

            }

            @Test
            public void shouldReturnStatusForbidden() throws Exception {
                mockMvc.perform(get("/user/edit/1")
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        ).andDo(print())
                        .andExpect(status().isForbidden());
            }

        }

        @Nested
        public class testHavePermissionToEditUser {
            @BeforeEach
            void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                        .apply(springSecurity()).build();
                User user = new User();
                user.setUserName("duongduc");
                user.setPassword("11111");
                Roles roles = new Roles();
                roles.setId(1);
                roles.setRoleName("ADMIN");
                user.setRoles(Set.of(roles));
                user.setDepartment(Department.HR);

                when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                        .thenReturn(Optional.of(user));

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

                userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

            }

            @Test
            public void returnStatusOkToAccessEditPage() throws Exception {
                Integer userId = 1;
                User user = new User();
                user.setStatus(UserStatus.ACTIVE);
                when(userService.findById(userId)).thenReturn(user);

                mockMvc.perform(get("/user/edit/1")
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        ).andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(view().name("users/edit_user"));
                verify(userService, times(1)).findById(userId);
            }

            @Test
            public void testShowFormEdit_UserNotExists_ReturnsError404Page() throws Exception {
                // Arrange
                Integer userId = 100;
                when(userService.findById(userId)).thenReturn(null);

                mockMvc.perform(get("/user/edit/100")
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                        .andExpect(view().name("error/404"));

                verify(userService, times(1)).findById(userId);
            }

            @Test
            public void testEditUserSuccess() throws Exception {
                Roles roles = new Roles();
                roles.setRoleName("ADMIN");
                User user = new User();
                user.setEmail("tn2558806@gmail.com");
                Mockito.when(roleService.findByRoleName("ADMIN")).thenReturn(roles);
                Mockito.when(userService.findById(1)).thenReturn(user);

                mockMvc.perform(
                                post("/user/edit/1")
                                        .param("fullName", "Nguyen Van An")
                                        .param("dateOfBirth", "2000-06-07")
                                        .param("phoneNumber", "0378828875")
                                        .param("status", "ACTIVE")
                                        .param("email", "abc123re@gmail.com")
                                        .param("address", "HN")
                                        .param("gender", "MALE")
                                        .param("department", "IT")
                                        .param("role", roles.getRoleName())
                                        .param("note", "aaaa")
                                        .with(user(userDetails)))
                        .andExpect(status().is3xxRedirection());


                verify(userService, times(1)).findById(1);
                verify(userService).saveUser(any(User.class));
            }

            @Test
            public void testEditUserFail_NoValid() throws Exception {
                Roles roles = new Roles();
                roles.setRoleName("ADMIN");
                User user = new User();
                user.setEmail("tn2558806@gmail.com");
                Mockito.when(roleService.findByRoleName("ADMIN")).thenReturn(roles);
                Mockito.when(userService.findById(1)).thenReturn(user);

                mockMvc.perform(
                                post("/user/edit/1")
                                        .param("fullName", "Nguyen Van An")
                                        .param("dateOfBirth", "2000-06-07")
                                        .param("phoneNumber", "03788288758")
                                        .param("email", "tn2558806@gmail.com")
                                        .param("address", "HN")
                                        .param("gender", "MALE")
                                        .param("department", "IT")
                                        .param("role", roles.getRoleName())
                                        .param("note", "aaaa")
                                        .with(user(userDetails)))
                        .andExpect(view().name("users/edit_user"))
                        .andExpect(status().isOk());
            }
        }

    }


    @Nested
    @DisplayName("Test active/deactive")
    public class testActive {
        @Nested
        public class TestDoNotHaveActiveUser {
            @BeforeEach
            void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                        .apply(springSecurity()).build();
                User user = new User();
                user.setUserName("duongduc");
                user.setPassword("11111");
                Roles roles = new Roles();
                roles.setId(1);
                roles.setRoleName("INTERVIEWER");
                user.setRoles(Set.of(roles));
                user.setDepartment(Department.HR);

                when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                        .thenReturn(Optional.of(user));

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

                userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

            }

            @Test
            public void shouldReturnStatusForbidden() throws Exception {
                mockMvc.perform(get("/user/active/1")
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        ).andDo(print())
                        .andExpect(status().isForbidden());
            }

            @Test
            public void testReturnStatusForbidden() throws Exception {
                mockMvc.perform(get("/user/deactive/1")
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        ).andDo(print())
                        .andExpect(status().isForbidden());
            }

        }

        @Nested
        public class testSetStatusSuccess {
            @BeforeEach
            void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                        .apply(springSecurity()).build();
                User user = new User();
                user.setUserName("duongduc");
                user.setPassword("11111");
                Roles roles = new Roles();
                roles.setId(1);
                roles.setRoleName("ADMIN");
                user.setRoles(Set.of(roles));
                user.setDepartment(Department.HR);

                when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                        .thenReturn(Optional.of(user));

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

                userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

            }

            @Test
            void testDeActiveSuccess() throws Exception {
                Integer userId = 1;
                User mockUser = new User();
                mockUser.setStatus(UserStatus.ACTIVE);
                when(userService.findById(userId)).thenReturn(mockUser);
                mockMvc.perform(get("/user/deactivate/{id}", userId)
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                        .andExpect(status().is3xxRedirection());
            }

            @Test
            void testActiveSuccess() throws Exception {
                Integer userId = 1;
                User mockUser = new User();
                mockUser.setStatus(UserStatus.INACTIVE);
                when(userService.findById(userId)).thenReturn(mockUser);
                mockMvc.perform(get("/user/active/{id}", userId)
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                        .andExpect(status().is3xxRedirection());
            }

            @Test
            void testUserIdNotFound() throws Exception {
                User user = new User();
                user.setUserId(1);
                when(userService.findById(10)).thenReturn(null);
                mockMvc.perform(get("/user/deactivate/10")
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                        .andExpect(view().name("error/404"));
            }

            @Test
            void testUserIdActiveNotFound() throws Exception {
                User user = new User();
                user.setUserId(1);
                when(userService.findById(10)).thenReturn(null);
                mockMvc.perform(get("/user/active/10")
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                        .andExpect(view().name("error/404"));
            }
        }
    }


    @Nested
    @DisplayName("Test view list")
    public class testVIewList {
        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                    .apply(springSecurity()).build();
            User user = new User();
            user.setUserName("duongduc");
            user.setPassword("11111");
            Roles roles = new Roles();
            roles.setId(1);
            roles.setRoleName("ADMIN");
            user.setRoles(Set.of(roles));
            user.setDepartment(Department.HR);

            when(userRepository.findByUserNameIgnoreCaseAndStatus(anyString(), any(UserStatus.class)))
                    .thenReturn(Optional.of(user));

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));

            userDetails = new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorities);

        }

        @Test
        void viewListUser() throws Exception {
            List<Roles> mockRoles = roleService.findAll();
            mockMvc.perform(get("/user")
                            .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                    .andExpect(view().name("users/list_user"))
                    .andExpect(MockMvcResultMatchers.model().attribute("roles", mockRoles))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Find and Paging")
    class testPaginateAndSearch {

        @Nested
        @DisplayName("test success ")
        class WhenRequested {

            @BeforeEach
            void setUp(){
                buildMockMvc(userController);
            }
            @Test
            void searchUserByUserNameList_Success() throws Exception {
                int pageIndex = 0;
                int pageSize = 3;
                String keyword = "thanh";
                String role = "ADMIN";

                Pageable pageable = PageRequest.of(pageIndex, pageSize);
                ListUserDto userDto = new ListUserDto();
                userDto.setUserId(1);
                userDto.setPhoneNumber("0378829976");
                userDto.setUserName("abc");
                userDto.setFullName("an");
                userDto.setDateOfBirth(LocalDate.of(2000,9,9));
                userDto.setGender("MALE");
                userDto.setDepartment(String.valueOf(Department.ACCOUNTING));
                userDto.setNote("abc");
                userDto.setRole("ADMIN");
                userDto.setUserName(keyword);
                userDto.setEmail("thanhnd44@gmail.com");
                List<ListUserDto> listUserDto = new ArrayList<>();
                listUserDto.add(userDto);


                when(userService.getUserList(keyword, role, pageable))
                        .thenReturn(new PageImpl<>(listUserDto, pageable, listUserDto.size()));


                ResultActions resultActions = mockMvc.perform(get("/user/api/v1")
                                .param("pageNo", String.valueOf(pageIndex))
                                .param("pageSize", String.valueOf(pageSize))
                                .param("keyword", keyword)
                                .param("role", role)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

                // Assertions on the response
                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userName").value("thanh"));
            }

            @Test
            void searchUserList_Success() throws Exception {
                int pageIndex = 0;
                int pageSize = 3;
                String keyword = "a";
                String role = "ADMIN";

                Pageable pageable = PageRequest.of(pageIndex, pageSize);
                ListUserDto userDto = new ListUserDto();
                Roles roles = new Roles();
                roles.setId(1);
                roles.setRoleName("ADMIN");
                userDto.setRole("ADMIN");

                userDto.setUserName(keyword);
                List<ListUserDto> listUserDto = new ArrayList<>();
                listUserDto.add(userDto);
                Mockito.when(userService.getUserList(keyword, role, pageable))
                        .thenReturn(new PageImpl<>(listUserDto, pageable, listUserDto.size()));

                ResultActions resultActions = mockMvc.perform(get("/user/api/v1")
                        .param("pageNo", String.valueOf(pageIndex))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("keyword", keyword)
                        .param("role", role));

                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userName").value("a"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].role").value("ADMIN"));
            }
        }

    }

}


