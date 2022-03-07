package jvreis.WebcrawlerLowestRatedMoviesIMDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jvreis.WebcrawlerLowestRatedMoviesIMDB.services.WebcrawlerConfiguration;

@SpringBootApplication
@RestController

//Favor abrir localhost:8080 no navegador
public class Application {
	static WebcrawlerConfiguration site;

	public static void main(String[] args) {
		site = new WebcrawlerConfiguration();
		
		SpringApplication.run(Application.class, args);
	}
	
	@GetMapping("/")
	public String index(){
		return site.getResultadoHTML();
	}
}
