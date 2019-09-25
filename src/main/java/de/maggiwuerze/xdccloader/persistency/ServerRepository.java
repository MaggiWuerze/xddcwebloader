package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.entity.Server;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServerRepository extends PagingAndSortingRepository<Server, Long> {

    List<Server> findAll();

}