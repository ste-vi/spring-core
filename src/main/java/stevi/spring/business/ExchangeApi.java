package stevi.spring.business;

import lombok.NoArgsConstructor;
import stevi.spring.anotations.Autowired;

@NoArgsConstructor
public class ExchangeApi {

    @Autowired
    private ApiClient apiClient;

    public Double getCurrentRate() {
        return apiClient.fetchExchangeRate();
    }
}
