package io.holunda.dmn.spike;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class LiteralExpressionSpikeApplication implements ApplicationRunner {

  public static void main(String[] args) {
    SpringApplication.run(LiteralExpressionSpikeApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    //logger.info("running application ...");
  }
}
