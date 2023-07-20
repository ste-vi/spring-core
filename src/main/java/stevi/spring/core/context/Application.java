package stevi.spring.core.context;

import stevi.spring.core.config.Config;
import stevi.spring.core.config.DefaultConfig;
import stevi.spring.core.factory.BeanFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class which starts an application.
 * Usually is used from java main method.
 */
public class Application {

    public static ExecutorService applicationFixedExecutorService;

    /**
     * Starts the application.
     * Scans for classes.
     *
     * @param packageToScan base package to scan beans.
     * @return {@link ApplicationContext}
     */
    public static ApplicationContext run(String packageToScan) {
        Config config = new DefaultConfig(packageToScan);
        AutowiredApplicationContext applicationContext = new AutowiredApplicationContext(config);
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);
        applicationContext.postInit();

        applicationFixedExecutorService = Executors.newFixedThreadPool(10);

        return applicationContext;
    }

    /**
     * Stops the application. Clears resources.
     */
    public static void stop() {
        applicationFixedExecutorService.shutdown();
    }
}
