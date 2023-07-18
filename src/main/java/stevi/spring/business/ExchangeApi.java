package stevi.spring.business;

import stevi.spring.core.anotations.Autowired;
import stevi.spring.core.anotations.Component;
import stevi.spring.core.anotations.Qualifier;

@Component
public class ExchangeApi {

    @Autowired
    @Qualifier(beanName = "HttpApiClient")
    private ApiClient apiClient;

    public Double getCurrentRate() {
        return apiClient.fetchExchangeRate();
    }
}
