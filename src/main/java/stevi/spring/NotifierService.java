package stevi.spring;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotifierService {

    private final ExchangeApi exchangeApi = ObjectFactory.getInstance().createObject(ExchangeApi.class);
    private final EmailNotificationService emailNotificationService = ObjectFactory.getInstance().createObject(EmailNotificationService.class);

    public void sendExchangeNotification() {
        Double currentRate = exchangeApi.getCurrentRate();
        emailNotificationService.notifyUser(1L, "Hey bro! here is the exchange rate for today: %s UA grivna per US dollar".formatted(currentRate));
    }
}
