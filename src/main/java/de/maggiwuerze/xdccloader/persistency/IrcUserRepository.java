package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.IrcUser;
import de.maggiwuerze.xdccloader.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IrcUserRepository extends PagingAndSortingRepository<IrcUser, Long> {

    List<IrcUser> findAll();

}