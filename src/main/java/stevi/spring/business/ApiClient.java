package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Async;
import stevi.spring.core.anotations.Component;

@Slf4j
@Component
public class ApiClient {

    @Async
    public void getExchange() {
        log.info(Thread.currentThread().getName());
        log.warn("getExchange()");
    }
}
