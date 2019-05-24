package de.maggiwuerze.xdccloader.persistency

import de.maggiwuerze.xdccloader.model.Download
import de.maggiwuerze.xdccloader.util.State
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository


@Repository
interface DownloadRepository : PagingAndSortingRepository<Download, Long> {


    fun findAllByStatusOrderByProgress(status:State) : MutableList<Download>

}