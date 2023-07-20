package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Async;
import stevi.spring.core.anotations.Autowired;
import stevi.spring.core.anotations.Component;
import stevi.spring.core.anotations.PostConstruct;
import stevi.spring.core.anotations.Value;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Component
public class ExchangeApi {

    @Value
    private String rate;

    @Autowired
    private ApiClient apiClient;

    @PostConstruct
    public void init() {
        this.rate = rate + "$";
    }

    @Async
    public Future<String> getCurrentRate() {
        log.info(Thread.currentThread().getName());
        log.warn("getCurrentRate()");
        apiClient.getExchange();
        return CompletableFuture.supplyAsync(() -> rate);
    }
}
