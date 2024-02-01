package fa.training.fjb04.ims.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

    @Bean
    public SecurityFilterChain httpSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(header ->
                        header.contentTypeOptions(
                                HeadersConfigurer.ContentTypeOptionsConfig::disable))
                .authorizeHttpRequests(config -> config
                        .requestMatchers("/login/**","/forgot_password/**","/reset_password/**","/download/**","/libs/**","/assets/**").permitAll()
                        .requestMatchers("/css/**","/js/**","/img/**","/error", "/files/**").permitAll()
                        .requestMatchers("/candidate/**", "/job/**", "/schedule/**", "/offer/**").hasAnyRole("INTERVIEWER", "RECRUITER", "MANAGER", "ADMIN")
                        .requestMatchers("/user/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                ).formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/do-login")
                        .usernameParameter("user")
                        .passwordParameter("pass")
                        .defaultSuccessUrl("/home")
                        .permitAll()
                ).rememberMe(config -> config
                        .key("rememberCookieKey")
                        .tokenValiditySeconds(60 * 60 * 24)
                        .rememberMeParameter("rememberMe")
                ).logout(config -> config
                        .logoutUrl("/exit")
                        .logoutSuccessUrl("/login?logout").permitAll()
                ).exceptionHandling(config -> config
                        .accessDeniedPage("/no-permission")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(
            SpringUserDetailService accountDetailService) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        authenticationProvider.setUserDetailsService(accountDetailService);

        return authenticationProvider;
    }
}
