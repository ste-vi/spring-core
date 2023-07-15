package stevi.spring.business;

import lombok.NoArgsConstructor;
import stevi.spring.anotations.Autowired;

@NoArgsConstructor
public class NotifierService {

    @Autowired
    private ExchangeApi exchangeApi;

    @Autowired
    private EmailNotificationService emailNotificationService;

    public void sendExchangeNotification() {
        Double currentRate = exchangeApi.getCurrentRate();
        emailNotificationService.notifyUser(1L, "Hey bro! here is the exchange rate for today: %s UA grivna per US dollar".formatted(currentRate));
    }
}
