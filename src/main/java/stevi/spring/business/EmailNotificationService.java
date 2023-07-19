package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Async;
import stevi.spring.core.anotations.Autowired;
import stevi.spring.core.anotations.PostConstruct;
import stevi.spring.core.anotations.Service;

@Slf4j
@Service
public class EmailNotificationService {

    @Autowired
    private BeanToCheckViaBeanAnnotation beanToCheckViaBeanAnnotation;

    @PostConstruct
    public void init() {
        System.out.println(this.getClass());
    }

    @Async
    public void notifyUser(Long userId, String message) {
        log.info(this.getClass().getName());
        log.info(Thread.currentThread().getName());
        System.out.printf("New notification is here for userId %s : %s%n", userId, message);
        beanToCheckViaBeanAnnotation.testAsyncFromAnotherAsync();
    }

}
