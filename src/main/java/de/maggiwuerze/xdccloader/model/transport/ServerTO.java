package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
