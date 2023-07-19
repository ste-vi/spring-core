package stevi.spring.business;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotations.Async;

@Slf4j
public class BeanToCheckViaBeanAnnotation {

    private final String value;

    public BeanToCheckViaBeanAnnotation(String value) {
        this.value = value;
    }

    void someMethod() {
        log.info(value);
    }

    @Async
    void testAsyncFromAnotherAsync() {
        log.info(this.getClass().getName());

        log.info(Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
