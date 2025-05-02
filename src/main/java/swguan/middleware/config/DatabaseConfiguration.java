
package swguan.middleware.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "swguan.middleware.persistence" })
public class DatabaseConfiguration {

	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddl;

	@Value("${hibernate.show_sql}")
	private String show_sql;

	@Value("${hibernate.dialect}")
	private String dialect;

	private static final String MAPPER_LOCATION = "classpath*:mapper/**/*.xml";

	private static final String CONFIG_LOCATION = "classpath:mybatis-config.xml";

	@Bean
	@Primary
	@ConfigurationProperties("spring.master.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource dataSource() {
		return dataSourceProperties().initializeDataSourceBuilder().build();
	}

	// for mybatis
	@Bean
	@Primary
	public PlatformTransactionManager transactionManager() {
		final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}

	// for mybatis
	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setMapperLocations(applicationContext.getResources(DatabaseConfiguration.MAPPER_LOCATION));
		sessionFactory.setConfigLocation(applicationContext.getResource(DatabaseConfiguration.CONFIG_LOCATION));
		return sessionFactory.getObject();
	}

	@Bean
	public MappingJackson2JsonView jsonViewer() {
		return new MappingJackson2JsonView();
	}

	// for mybatis
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	// for hibernate
	@Bean(name = "entityManagerFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "swguan.middleware.persistence.model" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	// for hibernate
	@Bean
	public PlatformTransactionManager hibernateTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
		properties.setProperty("hibernate.dialect", dialect);
		properties.setProperty("hibernate.show_sql", show_sql);
		return properties;
	}
}
