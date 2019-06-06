package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.TargetBot;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TargetBotRepository extends PagingAndSortingRepository<TargetBot, Long> {

    List<TargetBot> findAll();

}