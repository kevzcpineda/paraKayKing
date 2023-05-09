package quest.phil.diagnostic.information.system.ws;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lowagie.text.FontFactory;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication

public class QuestPhilDiagnosticInformationSystemApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestPhilDiagnosticInformationSystemApplication.class);

	public static void main(String[] args) {
		LOGGER.info("Application Started Console");
		
		String path = "src/main/resources/garamond";
		File file = new File(path);
		String absolutePath = file.getAbsolutePath();
		FontFactory.register(absolutePath + File.separator + "Garamond.ttf", "GARAMOND");
		FontFactory.register(absolutePath + File.separator + "Garamond Bold font.ttf", "GARAMOND_BOLD");
		FontFactory.register(absolutePath + File.separator + "GARA.TTF", "GARA");
		FontFactory.register(absolutePath + File.separator + "GARABD.TTF", "GARABD");
		
		SpringApplication.run(QuestPhilDiagnosticInformationSystemApplication.class, args);
	}
//	public Docket apis(){
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("quest.phil.diagnostic.information.system.ws"))
//				.build();
//	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
