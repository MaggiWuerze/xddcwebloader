package de.maggiwuerze.xdccloader.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

//@Configuration
//@PropertySource("classpath:mysql.properties")
public class DataSourceConfig {


    @Autowired
    private Environment environment;





}
