package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Bean;
import stevi.spring.core.anotations.Configuration;
import stevi.spring.core.anotations.Value;

@Slf4j
@Configuration
public class Config {

    @Value
    private String configProperty;

    @Bean
    public BeanToCheckViaBeanAnnotation beanToCheckViaBeanAnnotation() {
        return new BeanToCheckViaBeanAnnotation("@Configuration and @Bean works! %s".formatted(configProperty));
    }
}
