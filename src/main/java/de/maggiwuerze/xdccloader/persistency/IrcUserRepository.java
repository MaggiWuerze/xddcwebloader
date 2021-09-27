package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IrcUserRepository extends PagingAndSortingRepository<Bot, Long> {

	List<Bot> findAll();
}