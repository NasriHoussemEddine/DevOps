package com.esprit.examen;


import com.esprit.examen.config.DefaultProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;



@EnableScheduling
@SpringBootApplication
public class TpAchatProjectApplication {
	private static final Logger log = LoggerFactory.getLogger(TpAchatProjectApplication.class);

	private final Environment env;

	public TpAchatProjectApplication(Environment env) {
		this.env = env;
	}
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TpAchatProjectApplication.class);
		DefaultProfileUtil.addDefaultProfile(app);
		Environment env = app.run(args).getEnvironment();

		log.warn("\n---------------");
		log.warn("\n Application is running!");
		log.warn("Profile(s): {}", env.getActiveProfiles());
		log.warn("\n--------------------");
	}

}