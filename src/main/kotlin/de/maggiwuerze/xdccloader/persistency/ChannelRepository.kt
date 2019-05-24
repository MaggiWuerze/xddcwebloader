package de.maggiwuerze.xdccloader.persistency

import de.maggiwuerze.xdccloader.model.Channel
import de.maggiwuerze.xdccloader.model.User
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface ChannelRepository : PagingAndSortingRepository<Channel, Long> {


}