package pl.nawrockiit.catering3.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.nawrockiit.catering3.user.User;
import pl.nawrockiit.catering3.user.UserRepository;


import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername (username);
        if (user == null) {
            throw new UsernameNotFoundException("No username " + username + " found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getActive(), true, true, true,
                Set.of(new SimpleGrantedAuthority(user.getRole())));    }
}
