package com.robot.homeobot.services.user;

import com.robot.homeobot.model.User;
import com.robot.homeobot.repository.RoleRepository;
import com.robot.homeobot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


// Ovaj servis je namerno izdvojen kao poseban u ovom primeru.
// U opstem slucaju UserServiceImpl klasa bi mogla da implementira UserDetailService interfejs.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private LoginAttemptService loginAttemptService;


    // Funkcija koja na osnovu username-a iz baze vraca objekat User-a
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));

            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserDetails loadUserByUsernameAndCheckIp(String username, String ip) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

       else return loadUserByUsername(username);
    }

    public void checkIpBlocked(String ip)  {
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }
    }

}
