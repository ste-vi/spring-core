package stevi.spring.business;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Autowired;
import stevi.spring.core.anotations.Service;

@Slf4j
@Service
public class NotifierService {

    @Autowired
    private ExchangeApi exchangeApi;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @SneakyThrows
    public void sendExchangeNotification() {
        log.info(Thread.currentThread().getName());
        exchangeApi.getCurrentRate().get();
        emailNotificationService.notifyUser("Hey bro! here is the exchange rate for today: %s".formatted("null"));
    }
}
