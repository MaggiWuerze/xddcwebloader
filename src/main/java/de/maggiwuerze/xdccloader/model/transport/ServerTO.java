package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Value;

@Value
public class ServerTO {

	Long id;

	String name;

	String serverUrl;

	public ServerTO (Server server) {
		this.id = server.getId();
		this.name = server.getName();
		this.serverUrl = server.getServerUrl();
	}

}
