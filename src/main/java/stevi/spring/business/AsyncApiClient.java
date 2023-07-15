package stevi.spring.business;

import stevi.spring.anotations.Component;

@Component
public class AsyncApiClient implements ApiClient {

    @Override
    public Double fetchExchangeRate() {
        return 8.0d;
    }
}
