//package de.maggiwuerze.xdccloader.config.datasources;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
////@PropertySource("classpath:mysql.properties")
//public class MySQLDataSourceConfig {
//
//
//
//    @Bean(name = "dataSource")
//    @ConfigurationProperties(prefix = "xdcc")
//    @ConditionalOnProperty(
//        prefix = "xdcc",
//        name = "DATASOURCE",
//        havingValue = "POSTGRESQL")
//    @ConditionalOnMissingBean
//    public DataSource postgreSqlDatasource(){
//
//
//        System.out.println("test postgreSql");
//        return null;
//
//    }
//
//}
