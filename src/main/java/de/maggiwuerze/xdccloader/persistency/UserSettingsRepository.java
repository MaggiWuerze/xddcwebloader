package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserSettingsRepository extends PagingAndSortingRepository<UserSettings, Long> {

	List<UserSettings> findAll();
}