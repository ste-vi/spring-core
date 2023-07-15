package stevi.spring.business;


import stevi.spring.anotations.Component;
import stevi.spring.anotations.Value;

@Component
public class ApiClient {

    @Value
    private String rate;

    public Double fetchExchangeRate() {
        return Double.valueOf(rate);
    }
}
