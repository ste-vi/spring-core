package stevi.spring.business;

import stevi.spring.core.anotations.Bean;
import stevi.spring.core.anotations.Configuration;
import stevi.spring.core.anotations.Value;

@Configuration
public class Config {

    @Value
    private String configProperty;

    @Bean
    public BeanToCheckViaBeanAnnotation beanToCheckViaBeanAnnotation() {
        return new BeanToCheckViaBeanAnnotation("@Configuration and @Bean works! %s".formatted(configProperty));
    }
}
