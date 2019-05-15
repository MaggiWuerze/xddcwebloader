package de.maggiwuerze.xdccloader.persistency

import de.maggiwuerze.xdccloader.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class DatabaseLoader : CommandLineRunner {

	private val repository : UserRepository

	@Autowired
	constructor(repository:UserRepository){
		this.repository = repository
	}

	@Throws(Exception::class)
	override fun run(vararg strings : String) {

		var username : String = "Bilbo"
		var password : String = BCryptPasswordEncoder(11).encode("Baggins")
		this.repository.save(User(1L, username, password, LocalDateTime.now()))

		username = "Frodo"
		password = BCryptPasswordEncoder(11).encode("Baggins")
		this.repository.save(User(2L, username, password, LocalDateTime.now()))

		username = "Samweis"
		password = BCryptPasswordEncoder(11).encode("Baggins")
		this.repository.save(User(3L, username, password, LocalDateTime.now()))

		username = "Merry"
		password = BCryptPasswordEncoder(11).encode("Baggins")
		this.repository.save(User(4L, username, password, LocalDateTime.now()))

		username = "Pippin"
		password = BCryptPasswordEncoder(11).encode("Baggins")
		this.repository.save(User(5L, username, password, LocalDateTime.now()))


	}
}