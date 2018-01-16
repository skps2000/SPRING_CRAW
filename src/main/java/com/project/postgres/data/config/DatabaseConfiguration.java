package com.project.postgres.data.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class DatabaseConfiguration {
	
	public static final Logger log = LogManager.getLogger(DatabaseConfiguration.class);
//	public static final String BASE_PACKAGE = "com.project.postgres.data";
	public static final String MAPPER_LOCATIONS_PATH = "classpath:mappers/*.xml";
	
	protected void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
		sessionFactoryBean.setDataSource(dataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		sessionFactoryBean.setConfigLocation(resolver.getResource(CONFIG_LOCATION_PATH));
		sessionFactoryBean.setMapperLocations(resolver.getResources(MAPPER_LOCATIONS_PATH));		
	}
}

@Configuration
//@MapperScan(basePackages = DatabaseConfiguration.BASE_PACKAGE, sqlSessionFactoryRef = "db1SqlSessionFactory")
class Db1DatabaseConfig extends DatabaseConfiguration {
	
	@Bean(name = "db1DataSource", destroyMethod = "close")
	@Primary
	@ConfigurationProperties(prefix = "spring.db1.datasource")
	public DataSource db1DataSource() {
	    return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "db1SqlSessionFactory")
	@Primary
	public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1DataSource") DataSource db1DataSource, ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		log.info("||||||||||||||||||||||| dataSource Login TimeOut |||||||||||||||||||||||");
		log.info(db1DataSource.getLoginTimeout());
		configureSqlSessionFactory(sessionFactoryBean, db1DataSource);
	    return sessionFactoryBean.getObject();
	} 
	
	@Bean
	public PlatformTransactionManager transactionManager(@Qualifier("db1DataSource") DataSource db1DataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(db1DataSource);
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}
}

