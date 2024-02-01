package fa.training.fjb04.ims.controller.user;

import fa.training.fjb04.ims.dto.request.MailComplexRequest;
import fa.training.fjb04.ims.dto.user.ListUserDto;
import fa.training.fjb04.ims.dto.user.UserDto;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.entity.common.Roles;
import fa.training.fjb04.ims.enums.Department;
import fa.training.fjb04.ims.enums.Gender;
import fa.training.fjb04.ims.enums.UserStatus;
import fa.training.fjb04.ims.service.common.RoleService;
import fa.training.fjb04.ims.service.email.EmailService;
import fa.training.fjb04.ims.service.user.UserService;
import fa.training.fjb04.ims.util.PasswordGenerator;
import fa.training.fjb04.ims.util.StringUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fa.training.fjb04.ims.util.StringUtil.newArrayAccount;
import static fa.training.fjb04.ims.util.StringUtil.normalizeAndRemoveDiacritics;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @GetMapping("/show-form-create")
    public String showFormCreate(Model model) {
        UserDto userDto = new UserDto();

        List<Roles> roles = roleService.findAll();
        model.addAttribute("createUser", userDto);
        model.addAttribute("roles", roles);
        model.addAttribute("statusUser", UserStatus.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("departments", Department.values());

        return "users/add_user";
    }

    @GetMapping("/view/{userId}")
    public String getUserById(Model model, @PathVariable Integer userId) {
        ListUserDto user = userService.getUserById(userId);
        if (user == null) {
            return "error/404";
        }
        model.addAttribute("userDetail", user);
        return "users/detail_user";
    }

    @PostMapping("/create-user")
    @Transactional
    public String createUser(@Valid @ModelAttribute("createUser") UserDto userDto,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {


        if (userService.existsByPhoneNumber(userDto.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "", "Phone number already exists");
        }

        if (userService.existsByEmail(userDto.getEmail())) {
            bindingResult.rejectValue("email", "", "Email address already exists. Please try again.");
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("statusUser", UserStatus.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("departments", Department.values());

            List<Roles> roles = roleService.findAll();
            model.addAttribute("roles", roles);


            model.addAttribute("errorMessage", "Failed to created user");
            return "users/add_user";
        }

        Set<String> roles = userDto.getRole();
        Set<Roles> rolesSet = new HashSet<>();

        for (String s : roles) {
            Roles findRole = roleService.findByRoleName(s);
            rolesSet.add(findRole);
        }

        User user = new User();

        //generate username
        StringBuilder sb = new StringBuilder();
        String fullName = userDto.getFullName();
        String[] words = fullName.trim().split("\\s+");

        String lastWord = words[words.length - 1];
        String username = lastWord.substring(0, 1).toUpperCase() +
                lastWord.substring(1).toLowerCase();

        String[] newArray = Arrays.copyOf(words, words.length - 1);
        String account = sb.append(username).append(newArrayAccount(newArray)).toString();
        String generatedAccount = normalizeAndRemoveDiacritics(account);
        UserDto topByAccount = userService.findTopByAccount(generatedAccount);

        if (topByAccount == null) {
            user.setUserName(generatedAccount);
        } else {
            String userTop = topByAccount.getUserName();
            int extractedNumber = Objects.equals(extractNumberUsingRegex(userTop), "") ? 0 : Integer.parseInt(extractNumberUsingRegex(userTop));
            user.setUserName(generatedAccount + (extractedNumber + 1));
        }

        user.setFullName(StringUtil.formatString(userDto.getFullName()));
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRoles(rolesSet);

        user.setStatus(UserStatus.valueOf(userDto.getStatus()));
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setGender(Gender.valueOf(userDto.getGender()));
        user.setDepartment(Department.valueOf(userDto.getDepartment()));
        String pass = PasswordGenerator.generatePassword();

        user.setPassword(passwordEncoder.encode(pass));
        user.setNote(userDto.getNote());
        userService.saveUser(user);

        //send email
        MailComplexRequest mailRequest = MailComplexRequest.builder().to(user.getEmail())
                .subject("no-reply-email-IMS-system").username(user.getUserName())
                .email(user.getEmail()).password(pass).ownerAccount("ADMIN").build();
        emailService.sendMailWithTemplateThymeleaf(mailRequest);

        redirectAttributes.addFlashAttribute("successMessage", "Successfully created user");
        return "redirect:/user";
    }

    private String extractNumberUsingRegex(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    @GetMapping("/edit/{id}")
    public String showFormEdit(@PathVariable Integer id, Model model) {
        User user = userService.findById(id);

        if (user == null) {
            return "error/404";
        }

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(user, userDto);

        userDto.setStatus(user.getStatus().getName());
        userDto.setGender(String.valueOf(user.getGender()));
        userDto.setDepartment(String.valueOf(user.getDepartment()));

        List<Roles> roles = roleService.findAll();

        String roleName = "";
        for (Roles r : user.getRoles()) {
            roleName = r.getRoleName();
        }

        Set<String> roleSet = new HashSet<>();
        for (Roles role : roles) {
            if (!Objects.equals(role.getRoleName(), roleName)) {
                roleSet.add(role.getRoleName());
            }

        }

        model.addAttribute("roleName", roleName);
        model.addAttribute("roleSet", roleSet);
        model.addAttribute("userEdit", userDto);

        return "users/edit_user";
    }

    @PostMapping("/edit/{id}")
    @Transactional
    public String saveUser(@ModelAttribute("userEdit") @Valid UserDto userDto, BindingResult bindingResult,
                           @PathVariable Integer id, Model model, RedirectAttributes redirectAttributes
    ) {

        if (userService.existsByPhoneNumber(userDto.getPhoneNumber()) && !userService.existsByPhoneNumberAndUserId(userDto.getPhoneNumber(), userDto.getUserId())) {
            bindingResult.rejectValue("phoneNumber", "", "Phone number already exists");
        }
        if (userService.existsByEmail(userDto.getEmail()) && !userService.existsByEmailAndUserId(userDto.getEmail(), userDto.getUserId())) {
            bindingResult.rejectValue("email", "", "Email already exists");
        }

        if (userDto.getNote().length() > 500) {
            bindingResult.rejectValue("note", "", "Note max length 500");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Update user error");
            return "users/edit_user";
        }

        User user = userService.findById(id);
        String username = user.getUserName();


        //send email when email changes
        if (!user.getEmail().equals(userDto.getEmail())) {
            MailComplexRequest mail = MailComplexRequest.builder().
                    to(user.getEmail()).username(user.getUserName())
                    .subject("no-reply-email-IMS-system").build();
            emailService.sendMailChangEmail(mail);
        }


        BeanUtils.copyProperties(userDto, user);

        user.setGender(Gender.valueOf(userDto.getGender()));
        user.setDepartment(Department.valueOf(userDto.getDepartment()));
        user.setUserName(username);

        Set<String> roles = userDto.getRole();
        Set<Roles> rolesSet = new HashSet<>();

        for (String s : roles) {
            Roles findRole = roleService.findByRoleName(s);
            rolesSet.add(findRole);
        }

        user.setRoles(rolesSet);


        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Update user successfully");
        return "redirect:/user";
    }

    @GetMapping
    public String getUsers(Model model) {
        List<Roles> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "users/list_user";
    }

    @GetMapping("/api/v1")
    @ResponseBody
    public ResponseEntity<Page<ListUserDto>> getUserList(
            @RequestParam(defaultValue = "0", required = false, value = "pageNo") int pageIndex,
            @RequestParam(defaultValue = "5", required = false, value = "pageSize") int pageSize,
            @RequestParam(required = false, value = "keyword") String keyword,
            @RequestParam(required = false, value = "role") String role) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<ListUserDto> userDtoPage = userService.getUserList(keyword, role, pageable);
        return new ResponseEntity<>(userDtoPage, HttpStatus.OK);
    }

    @GetMapping("/deactivate/{id}")
    public String postDeactivateUser(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return "error/404";
        }
        user.setStatus(UserStatus.INACTIVE);
        userService.saveUser(user);
        return "redirect:/user/view/" + id;
    }

    @GetMapping("/active/{id}")
    public String postActivateUser(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return "error/404";
        }
        user.setStatus(UserStatus.ACTIVE);
        userService.saveUser(user);
        return "redirect:/user/view/" + id;
    }
}
