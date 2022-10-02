package com.herokuapp.blindjobs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "API Blind Jobs - Contratação as Cegas",
        version = "0.01",
        contact = @Contact(name = "BlindJobs", url = "https://github.com/FatecOhana"),
        description = "API usada para as Operações Server-Side. Tais como: Gerenciamento, Autenticação e Autorização, " +
                "Validações Internas e Operações no Banco de Dados"
))
public class BlindJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlindJobsApplication.class, args);
	}

}
