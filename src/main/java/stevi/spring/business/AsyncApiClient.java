package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Component;

@Slf4j
@Component
public class AsyncApiClient implements ApiClient {

    @Override
    public Double fetchExchangeRate() {
        return 8.0d;
    }
}
