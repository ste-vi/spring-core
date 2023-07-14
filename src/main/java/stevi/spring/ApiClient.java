package stevi.spring;


import stevi.spring.anotations.Value;

public class ApiClient {

    @Value(
    private String rate;

    public Double fetchExchangeRate() {
        return Double.valueOf(rate);
    }
}
