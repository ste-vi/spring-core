package stevi.spring.business;

import stevi.spring.anotations.Autowired;
import stevi.spring.anotations.Component;
import stevi.spring.anotations.Qualifier;

@Component
public class ExchangeApi {

    @Autowired
    @Qualifier(beanName = "AsyncApiClient")
    private ApiClient apiClient;

    public Double getCurrentRate() {
        return apiClient.fetchExchangeRate();
    }
}
