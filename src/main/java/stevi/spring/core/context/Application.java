package stevi.spring.core.context;

import lombok.SneakyThrows;
import stevi.spring.core.config.Config;
import stevi.spring.core.config.DefaultConfig;
import stevi.spring.core.context.util.BannerUtils;
import stevi.spring.core.factory.BeanFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
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
        BannerUtils.printBanner();
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
