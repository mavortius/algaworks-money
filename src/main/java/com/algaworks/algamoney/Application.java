package com.algaworks.algamoney;

import com.algaworks.algamoney.configuration.property.AlgamoneyProperty;
import com.algaworks.algamoney.repository.querydsl.QuerydslRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyProperty.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = QuerydslRepositoryFactoryBean.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
