package de.maggiwuerze.xdccloader.persistency

import de.maggiwuerze.xdccloader.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository : CrudRepository<User, Long> {


    fun findUserByName(name : String) : Optional<User>

}