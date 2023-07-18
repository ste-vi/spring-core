package stevi.spring.business;


import stevi.spring.core.anotations.Component;
import stevi.spring.core.anotations.PostConstruct;
import stevi.spring.core.anotations.Primary;
import stevi.spring.core.anotations.Value;

@Primary
@Component
public class HttpApiClient implements ApiClient {

    @Value
    private String rate;

    @PostConstruct
    public void init() {
        this.rate = rate + "3";
    }

    @Override
    public Double fetchExchangeRate() {
        return Double.valueOf(rate);
    }
}
