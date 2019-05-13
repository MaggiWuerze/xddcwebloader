package de.maggiwuerze.xdccloader.persistency

import de.maggiwuerze.xdccloader.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
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

		this.repository.save(User(1L, "Baggins", LocalDateTime.now()))

	}
}