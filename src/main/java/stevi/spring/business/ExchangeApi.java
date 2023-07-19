package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Autowired;
import stevi.spring.core.anotations.Component;
import stevi.spring.core.anotations.Qualifier;

@Slf4j
@Component
public class ExchangeApi {

    @Autowired
    @Qualifier(beanName = "HttpApiClient")
    private ApiClient apiClient;

    public Double getCurrentRate() {
        return apiClient.fetchExchangeRate();
    }
}
