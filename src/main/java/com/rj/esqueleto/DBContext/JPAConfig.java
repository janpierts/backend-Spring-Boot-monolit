package com.rj.esqueleto.DBContext;

import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class JPAConfig {
    @Value("${spring.datasource.url}")
    private String defaultUrl;
    @Value("${spring.datasource.username}")
    private String defaultUser;
    @Value("${spring.datasource.password}")
    private String defaultPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String defaultDriver;

    @Value("${spring.datasource1.url:#{null}}")
    private String ds1Url;
    @Value("${spring.datasource1.username:#{null}}")
    private String ds1User;
    @Value("${spring.datasource1.password:#{null}}")
    private String ds1Pass;

    @Value("${spring.datasource2.url:#{null}}")
    private String ds2Url;
    @Value("${spring.datasource2.username:#{null}}")
    private String ds2User;
    @Value("${spring.datasource2.password:#{null}}")
    private String ds2Pass;

    public EntityManager buildEntityManager(String url, String user, String pass, String driver,List<String> packagesToScan){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url != null ? url : defaultUrl);
        config.setUsername(user != null ? user : defaultUser);
        config.setPassword(pass != null ? pass : defaultPassword);
        config.setDriverClassName(driver != null ? driver : defaultDriver);
        config.setMaximumPoolSize(5); 
        config.setPoolName("HikariPool-Dinamico-");

        DataSource dataSource = new HikariDataSource(config);
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        List<String> finalPackages = (packagesToScan == null || packagesToScan.isEmpty()) 
                                     ? List.of("com.rj.esqueleto") 
                                     : packagesToScan;
        emfb.setDataSource(dataSource);
        emfb.setPackagesToScan(finalPackages.toArray(new String[0]));
        emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        emfb.setJpaProperties(props);
        emfb.afterPropertiesSet();

        return emfb.getObject().createEntityManager();
    }

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(defaultUrl);
        config.setUsername(defaultUser);
        config.setPassword(defaultPassword);
        config.setDriverClassName(defaultDriver);
        config.setMaximumPoolSize(20);
        config.setPoolName("HikariPool-Principal");
        emfb.setDataSource(new HikariDataSource(config));
        emfb.setPackagesToScan("com.rj.esqueleto");
        emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        emfb.setJpaProperties(props);
        emfb.afterPropertiesSet();

        return emfb.getObject();
    }
   
    @Bean
    @Primary
    public EntityManager entityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }
    // @Bean
    // @Primary
    // public EntityManager entityManager() {
    //     return buildEntityManager(null, null, null, null, List.of("com.rj.esqueleto"));
    // }

    // -> Método específico para obtener un EntityManager con configuración personalizada
    public EntityManager getEntityManagerForHistorical(List<String> packagesToScan) {
        if (ds1Url == null) throw new IllegalStateException("La BD historical no está configurada.");
        return buildEntityManager(ds1Url, ds1User, ds1Pass, defaultDriver, packagesToScan);
    }

    public EntityManager getEntityManagerForAudit(List<String> packagesToScan) {
        if (ds2Url == null) throw new IllegalStateException("La BD audit no está configurada.");
        return buildEntityManager(ds2Url, ds2User, ds2Pass, defaultDriver, packagesToScan);
    }

}
