package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.entity.Channel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChannelRepository extends PagingAndSortingRepository<Channel, Long> {


    List<Channel> findAll();

}