package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Autowired;
import stevi.spring.core.anotations.Service;
import stevi.spring.core.env.Environment;

@Slf4j
@Service
public class NotifierService {

    @Autowired
    private ExchangeApi exchangeApi;

    @Autowired
    private BeanToCheckViaBeanAnnotation beanToCheckViaBeanAnnotation;

    @Autowired
    private Environment environment;

    private final EmailNotificationService emailNotificationService;

    public NotifierService(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    public void sendExchangeNotification() {
        log.info(Thread.currentThread().getName());
        Double currentRate = exchangeApi.getCurrentRate();
        emailNotificationService.notifyUser(1L, "Hey bro! here is the exchange rate for today: %s UA grivna per US dollar".formatted(currentRate));
        beanToCheckViaBeanAnnotation.someMethod();
    }
}
