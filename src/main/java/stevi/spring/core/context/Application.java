package stevi.spring.core.context;

import stevi.spring.core.config.Config;
import stevi.spring.core.config.DefaultConfig;
import stevi.spring.core.factory.ObjectFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static ExecutorService applicationFixedExecutorService;

    public static ApplicationContext run(String packageToScan) {
        System.getProperty("spring.profile");

        Config config = new DefaultConfig(packageToScan);
        AutowiredApplicationContext applicationContext = new AutowiredApplicationContext(config);
        ObjectFactory objectFactory = new ObjectFactory(applicationContext);
        applicationContext.setObjectFactory(objectFactory);
        applicationContext.postInit();

        applicationFixedExecutorService = Executors.newFixedThreadPool(10);

        return applicationContext;
    }
}
