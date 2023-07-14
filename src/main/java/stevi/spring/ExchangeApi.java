package stevi.spring;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExchangeApi {

    private final ApiClient apiClient = ObjectFactory.getInstance().createObject(ApiClient.class);

    public Double getCurrentRate() {
        return apiClient.fetchExchangeRate();
    }
}
