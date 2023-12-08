package de.maggiwuerze.xdccloader.persistence;

import de.maggiwuerze.xdccloader.model.entity.Channel;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long> {

	List<Channel> findAll();
}