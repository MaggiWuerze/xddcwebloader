package de.maggiwuerze.xdccloader.persistence;

import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SettingsRepository extends CrudRepository<UserSettings, Long> {

	UserSettings getFirstById(long id);

}