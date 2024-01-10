package com.example.spring_project;


import com.example.spring_project.facade.ClientFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;


@SpringBootApplication
public class SpringProjectApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx=SpringApplication.run(SpringProjectApplication.class, args);
	}

	@Bean
	public HashMap<String, ClientFacade> tokensStore(){
		return new HashMap<>();
	}
}
