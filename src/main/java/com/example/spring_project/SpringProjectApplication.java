package com.example.spring_project;


import com.example.spring_project.beans.Company;
import com.example.spring_project.facade.ClientFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;


@SpringBootApplication
public class SpringProjectApplication {

	public static void main(String[] args) {

		// run the Spring Application context!
		ConfigurableApplicationContext ctx=SpringApplication.run(SpringProjectApplication.class, args);
		Company comp = ctx.getBean(Company.class);
	}

	@Bean
	public HashMap<String, ClientFacade> tokensStore(){
		// Barak wrote a comment here
		return new HashMap<>();
	}
}
