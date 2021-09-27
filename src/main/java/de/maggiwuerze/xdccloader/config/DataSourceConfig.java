package de.maggiwuerze.xdccloader.config;


import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

public class DataSourceConfig {

	@Autowired
	Environment environment;

	public DataSource postgresDataSource() {

		String xdcc_dbuser = environment.getProperty("XDCC_DATASOURCE_USERNAME");
		String xdcc_dbpassword = environment.getProperty("XDCC_DATASOURCE_PASSWORD");
		String xdcc_jdbcurl = environment.getProperty("XDCC_DATASOURCE_JDBCURL");

		return DataSourceBuilder
			.create()
			.username(xdcc_dbuser)
			.password(xdcc_dbpassword)
			.url(xdcc_jdbcurl)
			.build();
	}
}
