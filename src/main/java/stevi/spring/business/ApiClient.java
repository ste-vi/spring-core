package stevi.spring.business;


import stevi.spring.anotations.Component;
import stevi.spring.anotations.PostConstruct;
import stevi.spring.anotations.Value;

@Component
public class ApiClient {

    @Value
    private String rate;

    @PostConstruct
    public void init() {
        this.rate = rate + " 3";
    }

    public Double fetchExchangeRate() {
        return Double.valueOf(rate);
    }
}
