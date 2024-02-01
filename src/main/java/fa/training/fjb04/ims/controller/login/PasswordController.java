package fa.training.fjb04.ims.controller.login;

import fa.training.fjb04.ims.dto.user.UserChangePasswordDto;
import fa.training.fjb04.ims.dto.user.UserForgotPasswordDto;
import fa.training.fjb04.ims.dto.user.UserResetPasswordDto;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.entity.common.Utility;
import fa.training.fjb04.ims.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PasswordController {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("userForgotPasswordDto", new UserForgotPasswordDto());

        return "/login/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(Model model,
                                        @ModelAttribute("userForgotPasswordDto") @Valid UserForgotPasswordDto userForgotPasswordDto,
                                        BindingResult bindingResult,
                                        HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            return "/login/forgot_password_form";
        }

        User user = userService.findUserByEmail(userForgotPasswordDto.getEmail());

        if (user == null) {
            model.addAttribute("message", "The email address do not exist. Please try again.");
            return "/login/forgot_password_form";
        } else {
            model.addAttribute("message", "We've sent an email with the link to reset your password.");
        }

        String uuidString = UUID.randomUUID().toString();
        String token = uuidString.substring(0, Math.min(uuidString.length(), 45));
        userService.updateResetPasswordToken(token, userForgotPasswordDto.getEmail(), user);
        String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;

        user.setTimeToken(LocalDateTime.now());
        user.setLinkGmailHasUsed(0);
        userService.update(user);
        sendEmail(userForgotPasswordDto.getEmail(), resetPasswordLink);

        return "/login/forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String resetPasswordLink)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@admin.com", "Admin Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token,
                                        Model model) {
        model.addAttribute("userResetPasswordDto", new UserResetPasswordDto());

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("userForgotPasswordDto", new UserForgotPasswordDto());

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "/login/forgot_password_form";
        } else if (user.getLinkGmailHasUsed() > 0) {
            model.addAttribute("message", "This link has expired. Please go back to Homepage and try again");
            return "/login/forgot_password_form";
        } else if (Duration.between(user.getTimeToken(), LocalDateTime.now()).toHours() > 24){
            model.addAttribute("message", "This token too limited");
            return "/login/forgot_password_form";
        }

        userService.update(user);
        model.addAttribute("token", token);

        return "/login/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request,
                                       @ModelAttribute("userResetPasswordDto") @Valid UserResetPasswordDto userResetPasswordDto,
                                       BindingResult bindingResult,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        String token = request.getParameter("token");
        User user = userService.getByResetPasswordToken(token);

        if (bindingResult.hasErrors()) {
            model.addAttribute("token", token);
            return "login/reset_password_form";
        }

        if (!userResetPasswordDto.getPassword().equals(userResetPasswordDto.getRePassword())) {
            model.addAttribute("token", token);
            model.addAttribute("message", "Password and Confirm password don’t match. Please try again.");
            return "/login/reset_password_form";
        }

        userService.updatePassword(user, userResetPasswordDto.getPassword());
        user.setLinkGmailHasUsed(user.getLinkGmailHasUsed() + 1);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("token", token);

        redirectAttributes.addFlashAttribute("message", "Password has been updated");

        return "redirect:/login";
    }

    @GetMapping("/change_password")
    public String showChangePasswordFrom(Model model) {
        model.addAttribute("userChangePasswordDto", new UserChangePasswordDto());

        return "/login/change_password";
    }

    @PostMapping("/change_password")
    public String processChangePassword(@ModelAttribute @Valid UserChangePasswordDto userChangePasswordDto,
                                        BindingResult bindingResult,
                                        Model model,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/login/change_password";
        }

        User user = userService.findByUserName(userDetails.getUsername());

        if (!passwordEncoder.matches(userChangePasswordDto.getOldPassword(), user.getPassword())) {
            model.addAttribute("message1", "Don't found the account");
            return "login/change_password";
        } else if (Objects.equals(userChangePasswordDto.getOldPassword(), userChangePasswordDto.getNewPassword())) {
            model.addAttribute("message2", "The new password cannot be the same as the old password");
            return "login/change_password";
        } else if (!Objects.equals(userChangePasswordDto.getNewPassword(), userChangePasswordDto.getConfirmNewPassword())) {
            model.addAttribute("message3", "Confirm password is different");
            return "login/change_password";
        }

        user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "Password is updated");

        return "redirect:/login?logout";
    }
}
