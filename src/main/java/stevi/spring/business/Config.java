package stevi.spring.business;

import stevi.spring.anotations.Bean;
import stevi.spring.anotations.Configuration;

@Configuration
public class Config {

    @Bean
    public BeanToCheckViaBeanAnnotation beanToCheckViaBeanAnnotation() {
        return new BeanToCheckViaBeanAnnotation();
    }
}
