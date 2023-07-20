package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Bean;
import stevi.spring.core.anotations.Configuration;
import stevi.spring.core.anotations.Value;

@Slf4j
@Configuration
public class AppConfig {

    @Value
    private String currency;

    @Bean
    public EmailNotificationService emailNotificationService() {
        return new EmailNotificationService(currency);
    }
}
