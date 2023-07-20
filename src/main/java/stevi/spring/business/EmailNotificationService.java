package stevi.spring.business;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Async;

@Slf4j
@NoArgsConstructor
public class EmailNotificationService {

    private String currency;

    public EmailNotificationService(String currency) {
        this.currency = currency;
    }

    @Async
    public void notifyUser(String message) {
        //log.info(this.getClass().toString());
        log.info(Thread.currentThread().getName());
        log.warn("New notification for you about {} => {}", currency, message);
    }

}
