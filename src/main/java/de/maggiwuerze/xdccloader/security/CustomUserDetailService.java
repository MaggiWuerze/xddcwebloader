package de.maggiwuerze.xdccloader.security;

import de.maggiwuerze.xdccloader.model.User;
import de.maggiwuerze.xdccloader.persistency.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByName = userRepository.findUserByName(username);
        if (userByName.isPresent()) {

            return userByName.get();

        } else {

            throw new UsernameNotFoundException("No User found with name: " + username);

        }

    }

}

