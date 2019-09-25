package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserSettingsRepository extends PagingAndSortingRepository<UserSettings, Long> {

    List<UserSettings> findAll();

}