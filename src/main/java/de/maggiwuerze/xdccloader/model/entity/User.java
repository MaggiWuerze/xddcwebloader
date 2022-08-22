package de.maggiwuerze.xdccloader.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "userdetail")
@Data
public class User implements UserDetails {

	private static final String ROLE_PREFIX = "ROLE_";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;

	@Column(nullable = false)
	Boolean active = true;

	@Size(min=1)
	@Column(nullable = false, unique = true)
	String name;

	@Size(min=1)
	@Column(nullable = false, unique = true)
	String password;

	@Column(nullable = false)
	LocalDateTime creationDate = LocalDateTime.now();

	@Column(nullable = false)
	String userRole;

	@Column()
	Boolean locked = false;

	@Column()
	Boolean initialized = false;

	@Column()
	LocalDateTime expirationDate;

	@Column()
	LocalDateTime sessionValidUntil;

	@OneToOne
	UserSettings userSettings;

	@OneToMany
	List<Bot> bots = new ArrayList<>();

	public User(String name, String password, String userRole, boolean initialized, UserSettings userSettings) {
		this.name = name;
		this.password = password;
		this.userRole = userRole;
		this.initialized = initialized;
		this.userSettings = userSettings;
	}

	public User() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> list = new ArrayList();

		list.add(new SimpleGrantedAuthority(ROLE_PREFIX + userRole));

		return list;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return getExpirationDate() == null || getExpirationDate().isBefore(LocalDateTime.now());
	}

	@Override
	public boolean isAccountNonLocked() {
		return !getLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return getExpirationDate() == null || getExpirationDate().isBefore(LocalDateTime.now());
	}

	@Override
	public boolean isEnabled() {
		return getActive();
	}
}
