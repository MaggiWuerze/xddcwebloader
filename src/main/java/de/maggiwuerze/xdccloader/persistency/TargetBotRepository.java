package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TargetBotRepository extends PagingAndSortingRepository<Bot, Long> {

    List<Bot> findAll();

}