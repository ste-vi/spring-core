package stevi.spring.business;

import stevi.spring.anotations.Autowired;
import stevi.spring.anotations.Service;

@Service
public class NotifierService {

    @Autowired
    private ExchangeApi exchangeApi;

    @Autowired
    private BeanToCheckViaBeanAnnotation beanToCheckViaBeanAnnotation;

    private final EmailNotificationService emailNotificationService;

    public NotifierService(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    public void sendExchangeNotification() {
        Double currentRate = exchangeApi.getCurrentRate();
        emailNotificationService.notifyUser(1L, "Hey bro! here is the exchange rate for today: %s UA grivna per US dollar".formatted(currentRate));
        beanToCheckViaBeanAnnotation.someMethod();
    }
}
