package de.maggiwuerze.xdccloader.config;


import de.maggiwuerze.xdccloader.config.configConditions.UserDataSourceCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

//@Configuration
//@ConfigurationProperties(prefix = "xdcc")
//@EnableConfigurationProperties(DataSourceConfig.class)
public class DataSourceConfig {

    @Autowired
    Environment environment;


//    @Bean
//    @Conditional(UserDataSourceCondition.class)
//    @ConfigurationProperties(prefix = "xdcc-datasource")
    public DataSource postgresDataSource() {

        String xdcc_dbuser = environment.getProperty("XDCC_DATASOURCE_USERNAME");
        String xdcc_dbpassword = environment.getProperty("XDCC_DATASOURCE_PASSWORD");
        String xdcc_jdbcurl = environment.getProperty("XDCC_DATASOURCE_JDBCURL");
//
//        String url = String.format("jdbc:postgresql://%s:%s/%s", xdcc_dbhost, xdcc_dbport, xdcc_dbname);

        return DataSourceBuilder
                .create()
                .username(xdcc_dbuser)
                .password(xdcc_dbpassword)
                .url(xdcc_jdbcurl)
                .build();
    }
//    @Bean
//    @ConfigurationProperties(prefix = "xdcc-datasource")
//    @ConditionalOnProperty(
//            prefix = "xdcc",
//            name = "datasource",
//            havingValue = "MYSQL")
//    public DataSource mysqlDataSource() {
//
//        String xdcc_dbuser = environment.getProperty("XDCC_DATASOURCE_USERNAME");
//        String xdcc_dbpassword = environment.getProperty("XDCC_DATASOURCE_PASSWORD");
//        String xdcc_jdbcurl = environment.getProperty("XDCC_DATASOURCE_JDBCURL");
////
////        String url = String.format("jdbc:postgresql://%s:%s/%s", xdcc_dbhost, xdcc_dbport, xdcc_dbname);
//
//        return DataSourceBuilder
//                .create()
//                .username(xdcc_dbuser)
//                .password(xdcc_dbpassword)
//                .url(xdcc_jdbcurl)
//                .build();
//    }

}
