package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.util.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface DownloadRepository extends PagingAndSortingRepository<Download, Long> {


    List<Download> findAllByStatusOrderByProgressDesc(State status);

    List<Download> findAllByStatusInOrderByProgress(@Param("states") Collection<State> states);

    List<Download> findAllByOrderByProgressDesc();
}