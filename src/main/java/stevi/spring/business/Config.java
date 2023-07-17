package stevi.spring.business;

import stevi.spring.anotations.Bean;
import stevi.spring.anotations.Configuration;
import stevi.spring.anotations.Value;

@Configuration
public class Config {

    @Value
    private String configProperty;

    @Bean
    public BeanToCheckViaBeanAnnotation beanToCheckViaBeanAnnotation() {
        return new BeanToCheckViaBeanAnnotation("@Configuration and @Bean works! %s".formatted(configProperty));
    }
}
