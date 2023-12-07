package de.maggiwuerze.xdccloader.persistence;

import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SettingsRepository extends PagingAndSortingRepository<UserSettings, Long> {

	UserSettings getFirstById(long id);

}