package com.algaworks.algaworksmoney;

import com.algaworks.algaworksmoney.configuration.property.AlgamoneyProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyProperty.class)
public class AlgaworksMoneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgaworksMoneyApplication.class, args);
	}
}
