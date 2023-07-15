package stevi.spring.business;

import stevi.spring.anotations.Autowired;
import stevi.spring.anotations.Component;

@Component
public class ExchangeApi {

    @Autowired
    private ApiClient apiClient;

    public Double getCurrentRate() {
        return apiClient.fetchExchangeRate();
    }
}
