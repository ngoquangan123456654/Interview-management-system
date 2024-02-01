package fa.training.fjb04.ims.config.security;

import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.enums.UserStatus;
import fa.training.fjb04.ims.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SpringUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.findByUserNameIgnoreCaseAndStatus(username, UserStatus.INACTIVE).isPresent()) {
            throw new UsernameNotFoundException("Account is inactive");
        }

        Optional<User> userOpt = userRepository.findByUserNameIgnoreCaseAndStatus(username, UserStatus.ACTIVE);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Account is invalid");
        }

        User user = userOpt.get();

        List<GrantedAuthority> authorityList = user.getRoles()
                .stream().map(authority -> "ROLE_" + authority.getRoleName())
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new Users(user.getUserName(), user.getPassword(), user.getDepartment().getName(), authorityList);
    }
}

