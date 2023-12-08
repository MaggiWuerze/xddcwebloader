package de.maggiwuerze.xdccloader.persistence;

import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserSettingsRepository extends CrudRepository<UserSettings, Long> {

	List<UserSettings> findAll();
}