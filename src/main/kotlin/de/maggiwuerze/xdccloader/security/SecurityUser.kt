package de.maggiwuerze.xdccloader.security

import de.maggiwuerze.xdccloader.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SecurityUser(

        val userName: String,
        val userPassword: String,
        val userRole: String,
        val user : User) : UserDetails {

    val ROLE_PREFIX : String = "ROLE_"

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return userName
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return this.userPassword
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {

        val list : MutableList<GrantedAuthority> = ArrayList()

        list.add(SimpleGrantedAuthority(ROLE_PREFIX + this.userRole))

        return list

    }


}