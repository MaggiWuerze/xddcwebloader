package de.maggiwuerze.xdccloader.persistence;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IrcUserRepository extends CrudRepository<Bot, Long> {

	List<Bot> findAll();
}