package de.maggiwuerze.xdccloader.security

import de.maggiwuerze.xdccloader.model.User
import de.maggiwuerze.xdccloader.persistency.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority


@Service
class CustomUserDetailService(

        @Autowired
        val userRepository: UserRepository) : UserDetailsService {

//    override fun loadUserByUsername (username : String) : UserDetails {
//
//        val user : Optional<User> = userRepository.findUserByName(username)
//
//        if (user.isPresent){
//
//            val userPrincipal = CustomUserPrincipal(user.get())
//            return userPrincipal
//
//        }
//
//        throw UsernameNotFoundException(username)
//
//    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails? {
        val userByName = userRepository.findUserByName(username)
        if (userByName.isPresent) {

            var user = userByName.get()
            val password = user.password

            if (username == "admin") {
//                auth = AuthorityUtils
//                        .commaSeparatedStringToAuthorityList("ROLE_ADMIN")

                val springUser = SecurityUser(username, password, "ADMIN", user)

                return springUser
            }

            val springUser = SecurityUser(username, password, "USER", user)

            return springUser

        } else {

            return null

        }

    }

}

