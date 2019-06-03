package de.maggiwuerze.xdccloader.events;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration{
     
    public RepositoryConfiguration(){
        super();
    }
 
    @Bean
    EventHandler downloadEventHandler() {
        return new EventHandler();
    }
 
}