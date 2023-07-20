package stevi.spring;

import org.junit.jupiter.api.Test;
import stevi.spring.core.context.Application;
import stevi.spring.core.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplicationTest {

    @Test
    void testApplicationContext() {
        ApplicationContext applicationContext = Application.run("stevi.spring");
        assertNotNull(applicationContext);
    }
}
