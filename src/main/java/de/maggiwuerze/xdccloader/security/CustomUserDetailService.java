package de.maggiwuerze.xdccloader.security;

import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.persistence.UserRepository;
import de.maggiwuerze.xdccloader.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final UserService userService;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userByName = userService.findUserByName(username);
		if (userByName != null) {
			return userByName;
		} else {
			throw new UsernameNotFoundException("No User found with name: " + username);
		}
	}
}

