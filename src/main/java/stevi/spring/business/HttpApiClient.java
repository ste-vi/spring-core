package stevi.spring.business;


import stevi.spring.anotations.Component;
import stevi.spring.anotations.PostConstruct;
import stevi.spring.anotations.Primary;
import stevi.spring.anotations.Value;

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
