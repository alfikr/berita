package id.eiris.newsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class NewsServiceApplication {
	private Environment environment;
	public NewsServiceApplication(Environment env){
		this.environment=env;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(NewsServiceApplication.class);
		Environment env = app.run(args).getEnvironment();
		String user = env.getProperty("spring.security.user.name");
		String passw = env.getProperty("spring.security.user.password");
		System.out.println(user+" "+passw);
		//SpringApplication.run(NewsServiceApplication.class, args);
	}

}
